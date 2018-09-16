package nl.zeesoft.zodb.db;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.json.JsClientListener;
import nl.zeesoft.zdk.json.JsClientResponse;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zodb.Config;

public abstract class InitializerObject extends Locker implements JsClientListener {
	private Config							configuration	= null;
	private String							namePrefix		= "";
	private int								maxObjects		= 1000;
	
	private List<StateListener>				listeners		= new ArrayList<StateListener>();
	
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
		stateChanged(true);
	}
	
	public void initialize() {
		lockMe(this);
		if (!initializing && !initialized) {
			initializing = true;
			listObjectsInDatabaseNoLock();
		}
		unlockMe(this);
	}
	
	public void destroy() {
		stateChanged(false);
		lockMe(this);
		objects.clear();
		unlockMe(this);
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
				if (res.request.type.equals(DatabaseRequest.TYPE_GET)) {
					lockMe(this);
					todo--;
					unlockMe(this);
				}
			} else {
				if (res.request.type.equals(DatabaseRequest.TYPE_LIST)) {
					if (res.results.size()==0) {
						install();
						stateChanged(true);
					} else {
						lockMe(this);
						todo = 0;
						for (DatabaseResult result: res.results) {
							String objectName = result.name.substring(namePrefix.length());
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
					String objectName = res.request.name.substring(namePrefix.length());
					InitializerDatabaseObject object = getObjectByNameNoLock(objectName);
					if (object!=null && res.results.size()>0 && res.results.get(0).obj!=null) {
						object.fromJson(res.results.get(0).obj);
						configuration.debug(this,"Loaded " + res.request.name);
					}
					unlockMe(this);
				}
			}
			if (res.request.type.equals(DatabaseRequest.TYPE_GET)) {
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

	protected abstract InitializerDatabaseObject getNewObjectNoLock(String name);
	
	protected void setMaxObjectsNoLock(int max) {
		maxObjects = max;
	}
	
	protected void addObjectNoLock(InitializerDatabaseObject object) {
		objects.add(object);
	}

	protected InitializerDatabaseObject getObjectByNameNoLock(String name) {
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
		for (InitializerDatabaseObject object: objects) {
			DatabaseRequest request = new DatabaseRequest(DatabaseRequest.TYPE_ADD);
			request.name = namePrefix + object.getObjectName();
			request.obj = object.toJson();
			configuration.handleDatabaseRequest(request,this);
		}
	}

	private void listObjectsInDatabaseNoLock() {
		DatabaseRequest request = new DatabaseRequest(DatabaseRequest.TYPE_LIST);
		request.startsWith = namePrefix;
		request.max = maxObjects;
		configuration.handleDatabaseRequest(request,this);
	}

	private void getObjectsFromDatabaseNoLock() {
		for (InitializerDatabaseObject object: objects) {
			DatabaseRequest request = new DatabaseRequest(DatabaseRequest.TYPE_GET);
			request.name = namePrefix + object.getObjectName();
			configuration.handleDatabaseRequest(request,this);
		}
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
