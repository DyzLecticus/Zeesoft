package nl.zeesoft.zsc.mod;

import nl.zeesoft.zdk.json.JsAbleClient;
import nl.zeesoft.zdk.json.JsAbleClientRequest;
import nl.zeesoft.zdk.json.JsClientListener;
import nl.zeesoft.zdk.json.JsClientResponse;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.StateListener;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.JsonModTestResultsHandler;
import nl.zeesoft.zsc.confab.Confabulator;
import nl.zeesoft.zsc.confab.ConfabulatorManager;
import nl.zeesoft.zsc.confab.ConfabulatorRequest;
import nl.zeesoft.zsc.confab.ConfabulatorRequestHandler;
import nl.zeesoft.zsc.confab.ConfabulatorResponse;
import nl.zeesoft.zsc.confab.ConfabulatorSet;
import nl.zeesoft.zsc.confab.ConfabulatorSetLoader;
import nl.zeesoft.zsc.mod.handler.HtmlZSCIndexHandler;
import nl.zeesoft.zsc.mod.handler.HtmlZSCStateHandler;
import nl.zeesoft.zsc.mod.handler.JavaScriptZSCStateHandler;
import nl.zeesoft.zsc.mod.handler.JsonZSCRequestHandler;
import nl.zeesoft.zsc.mod.handler.JsonZSCStateHandler;

public class ModZSC extends ModObject implements StateListener {
	public static final String		NAME						= "ZSC";
	public static final String		DESC						= 
		"Zeesoft Symbolic Confabulators provides a simple JSON API to manage multiple symbolic confabulators.";
	
	private ConfabulatorSetLoader	confabulatorSetLoader		= null;
	private ConfabulatorManager		confabulatorManager			= null;
	
	public ModZSC(Config config) {
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
		handlers.add(new HtmlZSCIndexHandler(configuration,this));
		handlers.add(new JsonModTestResultsHandler(configuration,this));
		handlers.add(new JsonZSCStateHandler(configuration,this));
		handlers.add(new HtmlZSCStateHandler(configuration,this));
		handlers.add(new JavaScriptZSCStateHandler(configuration,this));
		handlers.add(new JsonZSCRequestHandler(configuration,this));
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
		Confabulator conf = confabulatorManager.getConfabulator(request.name);
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
		client.handleRequest(request,configuration.getModuleUrl(NAME) + JsonZSCRequestHandler.PATH,request);
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
	
	protected ZSCTester getNewTester() {
		return new ZSCTester(configuration,configuration.getModuleUrl(NAME) + JsonZSCRequestHandler.PATH);
	}
}
