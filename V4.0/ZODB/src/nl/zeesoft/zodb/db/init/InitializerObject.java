package nl.zeesoft.zodb.db.init;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringEncoder;
import nl.zeesoft.zdk.json.JsClientListener;
import nl.zeesoft.zdk.json.JsClientResponse;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.DatabaseRequest;
import nl.zeesoft.zodb.db.DatabaseResponse;
import nl.zeesoft.zodb.db.DatabaseResult;
import nl.zeesoft.zodb.db.StateListener;

public abstract class InitializerObject extends Locker implements JsClientListener {
	private Config							configuration	= null;
	private String							namePrefix		= "";
	private int								maxObjects		= 1000;
	
	private List<StateListener>				listeners		= new ArrayList<StateListener>();
	private int								timeoutSeconds	= 0;
	
	private List<InitializerDatabaseObject>	objects			= new ArrayList<InitializerDatabaseObject>();
	
	private boolean							initializing	= false;
	private boolean							initialized		= false;
	private int								todo			= 0;
	
	public InitializerObject(Config config,String prefix) {
		super(config.getMessenger());
		configuration = config;
		namePrefix = prefix;
	}
	
	public Config getConfiguration() {
		return configuration;
	}
	
	public String getNamePrefix() {
		return namePrefix;
	}
	
	public void setTimeoutSeconds(int timeoutSeconds) {
		lockMe(this);
		this.timeoutSeconds = timeoutSeconds;
		unlockMe(this);
	}
	
	public void addListener(StateListener listener) {
		lockMe(this);
		listeners.add(listener);
		unlockMe(this);
	}
	
	public void install() {
		lockMe(this);
		initializeDatabaseObjectsNoLock();
		configuration.debug(this,"Install: " + objects.size());
		addObjectsToDatabaseNoLock();
		unlockMe(this);
	}
	
	public void initialize() {
		lockMe(this);
		if (!initializing && !initialized) {
			initializing = true;
			listObjectsInDatabaseNoLock();
		}
		unlockMe(this);
	}

	public void reinitialize() {
		boolean reinitialize = false;
		lockMe(this);
		if (!initializing && initialized) {
			objects.clear();
			reinitialize = true;
			initialized = false;
		}
		unlockMe(this);
		if (reinitialize) {
			stateChanged(false);
			initialize();
		}
	}
	
	public void destroy() {
		stateChanged(false);
		lockMe(this);
		objects.clear();
		unlockMe(this);
	}
	
	public boolean isInitializing() {
		boolean r = false;
		lockMe(this);
		r = initializing;
		unlockMe(this);
		return r;
	}
	
	public boolean isInitialized() {
		boolean r = false;
		lockMe(this);
		r = initialized;
		unlockMe(this);
		return r;
	}
	
	public void initializeDatabaseObjects() {
		lockMe(this);
		initializeDatabaseObjectsNoLock();
		unlockMe(this);
	}

	@Override
	public void handledRequest(JsClientResponse response) {
		DatabaseResponse res = configuration.handledDatabaseRequest(response);
		if (response.error.length()>0) {
			configuration.error(this,response.error.toString(),response.ex);
		}
		if (res!=null) {
			if (response.error.length()>0) {
				if (res.request.type.equals(DatabaseRequest.TYPE_GET) || res.request.type.equals(DatabaseRequest.TYPE_ADD)) {
					lockMe(this);
					todo--;
					unlockMe(this);
				}
			} else {
				if (res.request.type.equals(DatabaseRequest.TYPE_LIST)) {
					if (res.results.size()==0) {
						install();
					} else {
						lockMe(this);
						todo = 0;
						for (DatabaseResult result: res.results) {
							ZStringBuilder objectName = result.name.substring(namePrefix.length());
							InitializerDatabaseObject object = getObjectByNameNoLock(objectName);
							if (object==null) {
								object = getNewObjectNoLock(objectName);
								objects.add(object);
							}
							todo++;
						}
						configuration.debug(this,"Load: " + todo);
						getObjectsFromDatabaseNoLock();
						unlockMe(this);
					}
				} else if (res.request.type.equals(DatabaseRequest.TYPE_GET)) {
					lockMe(this);
					todo--;
					ZStringBuilder objectName = res.request.name.substring(namePrefix.length());
					InitializerDatabaseObject object = getObjectByNameNoLock(objectName);
					if (object!=null && res.results.size()>0 && res.results.get(0).encoded!=null && res.results.get(0).encoded.length()>0) {
						ZStringEncoder encoder = new ZStringEncoder(res.results.get(0).encoded);
						encoder.decodeKey(configuration.getZODBKey(),0);
						JsFile obj = new JsFile();
						obj.fromStringBuilder(encoder);
						if (obj.rootElement!=null && obj.rootElement.children.size()>0) {
							object.fromJson(obj);
							configuration.debug(this,"Loaded " + res.request.name);
						} else {
							configuration.debug(this,"Failed to load " + res.request.name);
						}
					}
					unlockMe(this);
					loadedObject(object);
				} else if (res.request.type.equals(DatabaseRequest.TYPE_ADD)) {
					lockMe(this);
					todo--;
					unlockMe(this);
				}
			}
			if (res.request.type.equals(DatabaseRequest.TYPE_GET) || res.request.type.equals(DatabaseRequest.TYPE_ADD)) {
				boolean open = false;
				lockMe(this);
				if (todo==0) {
					open = true;
				}
				unlockMe(this);
				if (open) {
					stateChanged(true);
				}
			}
		}
	}
	
	protected abstract void initializeDatabaseObjectsNoLock();

	protected abstract InitializerDatabaseObject getNewObjectNoLock(ZStringBuilder name);
	
	protected void loadedObject(InitializerDatabaseObject object) {
		// Override to extend
	}
	
	protected void setMaxObjectsNoLock(int max) {
		maxObjects = max;
	}
	
	protected void addObjectNoLock(InitializerDatabaseObject object) {
		if (object!=null) {
			objects.add(object);
		}
	}

	protected InitializerDatabaseObject getObjectByNameNoLock(ZStringBuilder name) {
		InitializerDatabaseObject r = null;
		for (InitializerDatabaseObject object: objects) {
			if (object.getObjectName().equals(name)) {
				r = object;
				break;
			}
		}
		return r;
	}
	
	protected List<InitializerDatabaseObject> getObjectsNoLock() {
		return new ArrayList<InitializerDatabaseObject>(objects);
	}
	
	private void addObjectsToDatabaseNoLock() {
		todo = objects.size();
		for (InitializerDatabaseObject object: objects) {
			DatabaseRequest request = new DatabaseRequest(DatabaseRequest.TYPE_ADD);
			request.name = getFullObjectName(object.getObjectName());
			request.encoding = DatabaseRequest.ENC_KEY;
			ZStringEncoder encoder = new ZStringEncoder(object.toJson().toStringBuilder());
			encoder.encodeKey(configuration.getZODBKey(),0);
			request.encoded = encoder;
			configuration.handleDatabaseRequest(request,this);
		}
	}

	private void listObjectsInDatabaseNoLock() {
		DatabaseRequest request = new DatabaseRequest(DatabaseRequest.TYPE_LIST,namePrefix);
		request.max = maxObjects;
		configuration.handleDatabaseRequest(request,this);
	}

	private void getObjectsFromDatabaseNoLock() {
		for (InitializerDatabaseObject object: objects) {
			DatabaseRequest request = new DatabaseRequest(DatabaseRequest.TYPE_GET);
			request.name = getFullObjectName(object.getObjectName());
			request.encoding = DatabaseRequest.ENC_KEY;
			configuration.handleDatabaseRequest(request,this,timeoutSeconds);
		}
	}
	
	private ZStringBuilder getFullObjectName(ZStringBuilder objectName) {
		ZStringBuilder r = new ZStringBuilder(namePrefix);
		r.append(objectName);
		return r;
	}
	
	private void stateChanged(boolean open) {
		lockMe(this);
		List<StateListener> lst = new ArrayList<StateListener>(listeners);
		if (open) {
			initializing = false;
			initialized = true;
		} else {
			initialized = false;
		}
		unlockMe(this);
		for (StateListener listener: lst) {
			listener.stateChanged(this,open);
		}
	}

}
