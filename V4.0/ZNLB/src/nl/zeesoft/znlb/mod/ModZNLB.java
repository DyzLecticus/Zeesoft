package nl.zeesoft.znlb.mod;

import nl.zeesoft.zdk.json.JsAbleClient;
import nl.zeesoft.zdk.json.JsAbleClientRequest;
import nl.zeesoft.zdk.json.JsClientListener;
import nl.zeesoft.zdk.json.JsClientResponse;
import nl.zeesoft.znlb.ZNLBConfig;
import nl.zeesoft.znlb.mod.handler.HtmlZNLBIndexHandler;
import nl.zeesoft.znlb.mod.handler.JavaScriptZNLBLanguagesHandler;
import nl.zeesoft.znlb.mod.handler.JsonZNLBLanguagesHandler;
import nl.zeesoft.znlb.mod.handler.JsonZNLBRequestHandler;
import nl.zeesoft.znlb.prepro.Preprocessor;
import nl.zeesoft.znlb.prepro.PreprocessorRequestHandler;
import nl.zeesoft.znlb.prepro.PreprocessorRequestResponse;
import nl.zeesoft.znlb.prepro.PreprocessorStateListener;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.JsonModTestResultsHandler;

public class ModZNLB extends ModObject implements PreprocessorStateListener {
	public static final String		NAME			= "ZNLB";
	public static final String		DESC			= 
		"The Zeesoft Natural Language Base provides a simple JSON API for language specific sentence preprocessing.";

	private Preprocessor			preprocessor	= null;
	
	public ModZNLB(ZNLBConfig config) {
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
		handlers.add(new JavaScriptZNLBLanguagesHandler(configuration,this));
		handlers.add(new HtmlZNLBIndexHandler(configuration,this));
		handlers.add(new JsonZNLBRequestHandler(configuration,this));
		handlers.add(new JsonModTestResultsHandler(configuration,this));
		handlers.add(new JsonZNLBLanguagesHandler(configuration,this));
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
		client.handleRequest(request,configuration.getModuleUrl(NAME) + JsonZNLBRequestHandler.PATH,request);
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
	
	protected ZNLBTester getNewTester() {
		return new ZNLBTester(configuration,configuration.getModuleUrl(NAME) + JsonZNLBRequestHandler.PATH);
	}
	
	protected PreprocessorRequestHandler getNewPreprocessorRequestHandler() {
		return new PreprocessorRequestHandler(preprocessor);
	}
}
