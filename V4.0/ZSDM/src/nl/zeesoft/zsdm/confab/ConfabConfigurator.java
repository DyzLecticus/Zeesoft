package nl.zeesoft.zsdm.confab;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.json.JsClientListener;
import nl.zeesoft.zdk.json.JsClientResponse;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.znlb.context.Language;
import nl.zeesoft.znlb.context.MasterContext;
import nl.zeesoft.znlb.lang.Languages;
import nl.zeesoft.znlb.mod.ModZNLB;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.DatabaseRequest;
import nl.zeesoft.zodb.db.DatabaseResponse;
import nl.zeesoft.zodb.db.DatabaseResult;
import nl.zeesoft.zsc.confab.Confabulator;
import nl.zeesoft.zsc.confab.ConfabulatorSet;
import nl.zeesoft.zsc.mod.ModZSC;

public class ConfabConfigurator extends Locker implements JsClientListener {
	private Config				configuration		= null;
	
	private ConfabulatorSet		confabSet			= null;
	private long				confabSetId			= 0;
	private List<Language>		languages			= null;
	private boolean				reinitialize		= false;
	
	public ConfabConfigurator(Config config) {
		super(config.getMessenger());
		configuration = config;
	}

	public boolean getReinitialize() {
		boolean r = false;
		lockMe(this);
		r = reinitialize;
		unlockMe(this);
		return r;
	}

	public void configureConfabulators() {
		lockMe(this);
		confabSet = null;
		languages = null;
		unlockMe(this);
		
		DatabaseRequest request = new DatabaseRequest(DatabaseRequest.TYPE_GET);
		request.name = ModZSC.NAME + "/Configuration/ConfabulatorSet";
		configuration.handleDatabaseRequest(request,this);
		
		request = new DatabaseRequest(DatabaseRequest.TYPE_GET);
		request.startsWith = ModZNLB.NAME + "/Contexts/";
		configuration.handleDatabaseRequest(request,this);
	}
	
	@Override
	public void handledRequest(JsClientResponse response) {
		boolean done = false;
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
						if (languages!=null) {
							done = true;
						}
						unlockMe(this);
					} else {
						configuration.error(this,"Failed to load confabulator configuration");
					}
				} else {
					Languages langs = ((ModZNLB) configuration.getModule(ModZNLB.NAME)).getLanguages();
					for (DatabaseResult result: res.results) {
						Language lang = new Language();
						lang.fromJson(result.obj);
						lang.name = langs.getNameForCode(lang.code);
						if (lang.code.length()>0) {
							lockMe(this);
							if (languages==null) {
								languages = new ArrayList<Language>();
							}
							languages.add(lang);
							if (confabSet!=null) {
								done = true;
							}
							unlockMe(this);
						}
					}
				}
			} else if (res.request.type.equals(DatabaseRequest.TYPE_SET)) {
				lockMe(this);
				reinitialize = true;
				unlockMe(this);
				/* TODO: Expand with trainingSet initialization?
				configuration.debug(this,"Changed confabulator configuration");
				JsFile request = new JsFile();
				request.rootElement = new JsElem();
				request.rootElement.children.add(new JsElem("action","reinitialize",true));
				JsClient client = new JsClient(configuration.getMessenger(),configuration.getUnion());
				client.addJsClientListener(this);
				client.handleRequest(request,configuration.getModuleUrl(ModZSC.NAME) + JsonZSCStateHandler.PATH);
				*/
			}
		}
		if (done) {
			lockMe(this);
			if (checkConfabulatorSetNoLock()) {
				DatabaseRequest request = new DatabaseRequest(DatabaseRequest.TYPE_SET);
				request.id = confabSetId;
				request.obj = confabSet.toJson();
				configuration.handleDatabaseRequest(request,this);
			}
			unlockMe(this);
		}
	}
	
	protected boolean checkConfabulatorSetNoLock() {
		boolean r = false;
		boolean added = addConfabulatorIfNotExistsNoLock("Language");
		if (added) {
			r = true;
		}
		for (Language lang: languages) {
			added = addConfabulatorIfNotExistsNoLock(lang.name + "MasterContext");
			if (added) {
				r = true;
			}
			for (MasterContext mc: lang.masterContexts) {
				added = addConfabulatorIfNotExistsNoLock(lang.name + "Context" + mc.name);
				if (added) {
					r = true;
				}
			}
		}
		return r;
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
}
