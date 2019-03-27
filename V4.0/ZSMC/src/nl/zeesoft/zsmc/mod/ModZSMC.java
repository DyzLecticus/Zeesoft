package nl.zeesoft.zsmc.mod;

import nl.zeesoft.zdk.json.JsAbleClient;
import nl.zeesoft.zdk.json.JsAbleClientRequest;
import nl.zeesoft.zdk.json.JsClientListener;
import nl.zeesoft.zdk.json.JsClientResponse;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.StateListener;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.JsonModTestResultsHandler;
import nl.zeesoft.zsmc.db.ConfabulatorManager;
import nl.zeesoft.zsmc.db.ConfabulatorSet;
import nl.zeesoft.zsmc.db.ConfabulatorSetLoader;
import nl.zeesoft.zsmc.db.KnowledgeBaseConfabulator;
import nl.zeesoft.zsmc.mod.handler.HtmlZSMCIndexHandler;
import nl.zeesoft.zsmc.mod.handler.HtmlZSMCStateHandler;
import nl.zeesoft.zsmc.mod.handler.JavaScriptZSMCStateHandler;
import nl.zeesoft.zsmc.mod.handler.JsonZSMCRequestHandler;
import nl.zeesoft.zsmc.mod.handler.JsonZSMCStateHandler;
import nl.zeesoft.zsmc.request.ConfabulatorRequest;
import nl.zeesoft.zsmc.request.ConfabulatorRequestHandler;
import nl.zeesoft.zsmc.request.ConfabulatorResponse;

public class ModZSMC extends ModObject implements StateListener {
	public static final String		NAME						= "ZSMC";
	public static final String		DESC						= 
		"Zeesoft Symbolic Multithreaded Confabulators provides a simple JSON API to manage multiple symbolic confabulators.";

	private ConfabulatorSetLoader	confabulatorSetLoader		= null;
	private ConfabulatorManager		confabulatorManager			= null;
	
	public ModZSMC(Config config) {
		super(config);
		name = NAME;
		desc.append(DESC);
		confabulatorSetLoader = getNewConfabulatorSetLoader();
		confabulatorSetLoader.addListener(this);
		confabulatorManager = getNewConfabulatorManager();
		confabulatorManager.addListener(this);
	}
	
	@Override
	public void install() {
		ConfabulatorSet cs = getNewConfabulatorSet();
		
		ConfabulatorSetLoader csl = getNewConfabulatorSetLoader();
		csl.setConfabulatorSet(cs);
		csl.install();
		
		ConfabulatorManager cm = getNewConfabulatorManager();
		cm.setConfabulatorSet(cs);
		cm.install();
	}
	
	@Override
	public void initialize() {
		handlers.add(new HtmlZSMCIndexHandler(configuration,this));
		handlers.add(new JsonModTestResultsHandler(configuration,this));
		handlers.add(new HtmlZSMCStateHandler(configuration,this));
		handlers.add(new JsonZSMCRequestHandler(configuration,this));
		handlers.add(new JsonZSMCStateHandler(configuration,this));
		handlers.add(new HtmlZSMCStateHandler(configuration,this));
		handlers.add(new JavaScriptZSMCStateHandler(configuration,this));
		testers.add(getNewTester());
		confabulatorSetLoader.setConfabulatorSet(getNewConfabulatorSet());
		confabulatorSetLoader.initialize();
		super.initialize();
	}
	
	@Override
	public void destroy() {
		confabulatorSetLoader.destroy();
		confabulatorManager.destroy();
		super.destroy();
	}

	@Override
	public void stateChanged(Object source, boolean open) {
		if (open) {
			if (source==confabulatorSetLoader) {
				confabulatorManager.setConfabulatorSet(confabulatorSetLoader.getConfabulatorSet());
				if (confabulatorManager.isInitialized()) {
					confabulatorManager.reinitialize();
				} else {
					confabulatorManager.initialize();
				}
			} else if (source==confabulatorManager) {
				startTesters();
			}
		}
	}

	public ConfabulatorResponse handleRequest(ConfabulatorRequest request) {
		ConfabulatorResponse r = null;
		KnowledgeBaseConfabulator conf = confabulatorManager.getConfabulator(request.name);
		if (conf!=null) {
			ConfabulatorRequestHandler handler = new ConfabulatorRequestHandler(conf);
			r = handler.handleRequest(request);
		} else {
			r = new ConfabulatorResponse();
			r.request = request;
			if (request.name.length()==0) {
				r.error.append("Request confabulator name is mandatory");
			} else {
				r.error.append("Request confabulator not found: " + request.name);
			}
		}
		return r;
	}

	public void handleRequest(ConfabulatorRequest request,JsClientListener listener) {
		JsAbleClient client = new JsAbleClient(configuration.getMessenger(),configuration.getUnion());
		client.addJsClientListener(listener);
		client.handleRequest(request,configuration.getModuleUrl(NAME) + JsonZSMCRequestHandler.PATH,request);
	}
	
	public ConfabulatorResponse handledRequest(JsClientResponse response) {
		ConfabulatorResponse r = null;
		if (response.request instanceof JsAbleClientRequest &&
			((JsAbleClientRequest) response.request).resObject instanceof ConfabulatorResponse 
			) {
			r = (ConfabulatorResponse) ((JsAbleClientRequest) response.request).resObject;
		}
		return r;
	}

	public ConfabulatorSetLoader getConfabulatorSetLoader() {
		return confabulatorSetLoader;
	}
	
	public ConfabulatorManager getConfabulatorManager() {
		return confabulatorManager;
	}
	
	protected ConfabulatorSet getNewConfabulatorSet() {
		return new ConfabulatorSet(configuration);
	}
	
	protected ConfabulatorSetLoader getNewConfabulatorSetLoader() {
		return new ConfabulatorSetLoader(configuration);
	}
	
	protected ConfabulatorManager getNewConfabulatorManager() {
		return new ConfabulatorManager(configuration);
	}
	
	protected ZSMCTester getNewTester() {
		return new ZSMCTester(configuration,configuration.getModuleUrl(NAME) + JsonZSMCRequestHandler.PATH);
	}
	
	/* TODO: Add tester
	/*
	public ModZSMC(Config config) {
		super(config);
		name = NAME;
		desc.append(DESC);
	}
	
	@Override
	public void install() {
	}
	
	@Override
	public void initialize() {
		handlers.add(new HtmlZSMCIndexHandler(configuration,this));
		handlers.add(new JsonModTestResultsHandler(configuration,this));
		handlers.add(new HtmlZSMCStateHandler(configuration,this));
		super.initialize();
	}
	
	@Override
	public void destroy() {
		super.destroy();
	}

	@Override
	public void stateChanged(Object source, boolean open) {
		
	}
	*/
}
