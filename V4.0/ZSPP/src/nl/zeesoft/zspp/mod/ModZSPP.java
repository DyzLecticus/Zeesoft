package nl.zeesoft.zspp.mod;

import nl.zeesoft.zdk.json.JsAbleClient;
import nl.zeesoft.zdk.json.JsAbleClientRequest;
import nl.zeesoft.zdk.json.JsClientListener;
import nl.zeesoft.zdk.json.JsClientResponse;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.JsonModTestResultsHandler;
import nl.zeesoft.zspp.ZSPPConfig;
import nl.zeesoft.zspp.mod.handler.HtmlZSPPIndexHandler;
import nl.zeesoft.zspp.mod.handler.JsonZSPPRequestHandler;
import nl.zeesoft.zspp.prepro.Preprocessor;
import nl.zeesoft.zspp.prepro.PreprocessorRequestHandler;
import nl.zeesoft.zspp.prepro.PreprocessorRequestResponse;
import nl.zeesoft.zspp.prepro.PreprocessorStateListener;

public class ModZSPP extends ModObject implements PreprocessorStateListener {
	public static final String		NAME			= "ZSPP";
	public static final String		DESC			= 
		"The Zeesoft Sequence Preprocessor provides a simple JSON API for language specific sentence preprocessing.";

	private Preprocessor			preprocessor	= null;
	
	public ModZSPP(ZSPPConfig config) {
		super(config);
		name = NAME;
		desc.append(DESC);
	}
	
	@Override
	public void install() {
		Preprocessor prepro = getNewPreprocessor();
		prepro.install();
	}
	
	@Override
	public void initialize() {
		handlers.add(new HtmlZSPPIndexHandler(configuration,this));
		handlers.add(new JsonZSPPRequestHandler(configuration,this));
		handlers.add(new JsonModTestResultsHandler(configuration,this));
		preprocessor = getNewPreprocessor();
		preprocessor.addListener(this);
		preprocessor.initialize();
		tester = getNewTester();
		super.initialize();
	}
	
	@Override
	public void destroy() {
		if (selfTest) {
			tester.stop();
		}
		preprocessor.destroy();
	}

	@Override
	public void preprocessorStateChanged(boolean open) {
		if (open && selfTest) {
			tester.start();
		}
	}
	
	public void handleRequest(PreprocessorRequestResponse request) {
		PreprocessorRequestHandler handler = getNewPreprocessorRequestHandler();
		handler.handleRequest(request);
	}
	
	public void handleRequest(PreprocessorRequestResponse request,JsClientListener listener) {
		JsAbleClient client = new JsAbleClient(configuration.getMessenger(),configuration.getUnion());
		client.addJsClientListener(listener);
		client.handleRequest(request,configuration.getModuleUrl(NAME) + JsonZSPPRequestHandler.PATH,request);
	}
	
	public PreprocessorRequestResponse handledRequest(JsClientResponse response) {
		PreprocessorRequestResponse r = null;
		if (response.request instanceof JsAbleClientRequest &&
			((JsAbleClientRequest) response.request).resObject instanceof PreprocessorRequestResponse 
			) {
			r = (PreprocessorRequestResponse) ((JsAbleClientRequest) response.request).resObject;
		}
		return r;
	}
	
	public Preprocessor getPreprocessor() {
		return preprocessor;
	}
	
	protected Preprocessor getNewPreprocessor() {
		return new Preprocessor(configuration);
	}
	
	protected ZSPPTester getNewTester() {
		return new ZSPPTester(configuration,configuration.getModuleUrl(NAME) + JsonZSPPRequestHandler.PATH);
	}
	
	protected PreprocessorRequestHandler getNewPreprocessorRequestHandler() {
		return new PreprocessorRequestHandler(preprocessor);
	}
}
