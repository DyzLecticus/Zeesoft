package nl.zeesoft.zals.confab;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zals.env.Animal;
import nl.zeesoft.zals.env.Environment;
import nl.zeesoft.zdk.json.JsClient;
import nl.zeesoft.zdk.json.JsClientListener;
import nl.zeesoft.zdk.json.JsClientResponse;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.DatabaseRequest;
import nl.zeesoft.zodb.db.DatabaseResponse;
import nl.zeesoft.zodb.db.StateListener;
import nl.zeesoft.zsc.confab.Confabulator;
import nl.zeesoft.zsc.confab.ConfabulatorSet;
import nl.zeesoft.zsc.confab.TrainingSet;
import nl.zeesoft.zsc.mod.ModZSC;
import nl.zeesoft.zsc.mod.handler.JsonZSCStateHandler;

public class ConfabConfigurator extends Locker implements JsClientListener {
	private Config				configuration		= null;
	private Environment 		environment			= null;
	
	private boolean				configuring			= false;
	private ConfabulatorSet		confabSet			= null;
	private long				confabSetId			= 0;
	
	private boolean				reinitialize		= false;
	private int					todo				= 0;

	private List<StateListener>	listeners			= new ArrayList<StateListener>();
	
	public ConfabConfigurator(Config config) {
		super(config.getMessenger());
		configuration = config;
	}

	public void addListener(StateListener listener) {
		lockMe(this);
		listeners.add(listener);
		unlockMe(this);
	}
	
	public boolean isConfiguring() {
		boolean r = false;
		lockMe(this);
		r = configuring;
		unlockMe(this);
		return r;
	}

	public boolean getReinitialize() {
		boolean r = false;
		lockMe(this);
		r = reinitialize;
		unlockMe(this);
		return r;
	}

	public void configureConfabulators(Environment env) {
		boolean stateChanged = false;
		lockMe(this);
		if (!configuring) {
			environment = env;
			configuring = true;
			confabSet = null;
			confabSetId = 0;
			reinitialize = false;
			todo = 0;
			
			DatabaseRequest request = new DatabaseRequest(DatabaseRequest.TYPE_GET);
			request.name = ModZSC.NAME + "/Configuration/ConfabulatorSet";
			configuration.handleDatabaseRequest(request,this);
		}
		unlockMe(this);
		if (stateChanged) {
			stateChanged();
		}
	}

	public void reinitializeConfabulators() {
		if (getReinitialize()) {
			lockMe(this);
			reinitialize = false;
			unlockMe(this);
			JsFile request = new JsFile();
			request.rootElement = new JsElem();
			request.rootElement.children.add(new JsElem("action","reinitialize",true));
			JsClient client = new JsClient(configuration.getMessenger(),configuration.getUnion());
			client.addJsClientListener(this);
			client.handleRequest(request,configuration.getModuleUrl(ModZSC.NAME) + JsonZSCStateHandler.PATH);
		}
	}

	@Override
	public void handledRequest(JsClientResponse response) {
		boolean stateChanged = false;
		if (response.error.length()>0) {
			configuration.error(this,response.error.toString(),response.ex);
		}
		DatabaseResponse res = configuration.handledDatabaseRequest(response);
		if (res!=null) {
			if (res.request.type.equals(DatabaseRequest.TYPE_GET)) {
				if (res.request.name.equals(ModZSC.NAME + "/Configuration/ConfabulatorSet")) {
					if (res.results.size()>0) {
						lockMe(this);
						confabSetId = res.results.get(0).id;
						confabSet = new ConfabulatorSet(configuration);
						confabSet.fromJson(res.results.get(0).obj);
						List<String> added = checkConfabulatorSetNoLock();
						if (added.size()>0) {
							todo = added.size();
							DatabaseRequest request = new DatabaseRequest(DatabaseRequest.TYPE_SET);
							request.id = confabSetId;
							request.obj = confabSet.toJson();
							configuration.handleDatabaseRequest(request,this);
							for (String name: added) {
								TrainingSet ts = new TrainingSet();
								ts.setName(name);
								request = new DatabaseRequest(DatabaseRequest.TYPE_ADD);
								request.name = ModZSC.NAME + "/TrainingSets/" + name;
								request.obj = ts.toJson();
								configuration.handleDatabaseRequest(request,this);
							}
						} else {
							configuring = false;
							stateChanged = true;
						}
						unlockMe(this);
					} else {
						configuration.error(this,"Failed to load confabulator configuration");
						lockMe(this);
						configuring = false;
						stateChanged = true;
						unlockMe(this);
					}
				}
			} else if (res.request.type.equals(DatabaseRequest.TYPE_SET)) {
				lockMe(this);
				reinitialize = true;
				unlockMe(this);
			} else if (res.request.type.equals(DatabaseRequest.TYPE_ADD)) {
				lockMe(this);
				todo--;
				if (todo==0) {
					configuring = false;
					stateChanged = true;
				}
				unlockMe(this);
			}
		}
		if (stateChanged) {
			stateChanged();
		}
	}
	
	protected List<String> checkConfabulatorSetNoLock() {
		List<String> added = new ArrayList<String>();
		List<String> names = new ArrayList<String>();
		for (Animal ani: environment.animals) {
			names.add(ani.name);
		}
		for (String name: names) {
			if (addConfabulatorIfNotExistsNoLock(name)) {
				added.add(name);
			}
		}
		return added;
	}
	
	protected boolean addConfabulatorIfNotExistsNoLock(String name) {
		boolean r = false;
		Confabulator test = confabSet.getConfabulator(name);
		if (test==null) {
			confabSet.addConfabulator(name);
			r = true;
		}
		return r;
	}
	
	private void stateChanged() {
		lockMe(this);
		boolean open = !configuring;
		List<StateListener> list = new ArrayList<StateListener>(listeners);
		unlockMe(this);
		for (StateListener listener: list) {
			listener.stateChanged(this,open);
		}
	}
}
