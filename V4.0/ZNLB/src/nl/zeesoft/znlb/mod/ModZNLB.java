package nl.zeesoft.znlb.mod;

import nl.zeesoft.zdk.json.JsAbleClient;
import nl.zeesoft.zdk.json.JsAbleClientRequest;
import nl.zeesoft.zdk.json.JsClientListener;
import nl.zeesoft.zdk.json.JsClientResponse;
import nl.zeesoft.znlb.context.ContextConfig;
import nl.zeesoft.znlb.lang.Languages;
import nl.zeesoft.znlb.mod.handler.HtmlZNLBIndexHandler;
import nl.zeesoft.znlb.mod.handler.JavaScriptZNLBLanguagesHandler;
import nl.zeesoft.znlb.mod.handler.JsonZNLBContextConfigurationHandler;
import nl.zeesoft.znlb.mod.handler.JsonZNLBLanguagesHandler;
import nl.zeesoft.znlb.mod.handler.JsonZNLBRequestHandler;
import nl.zeesoft.znlb.prepro.Preprocessor;
import nl.zeesoft.znlb.prepro.PreprocessorRequestHandler;
import nl.zeesoft.znlb.prepro.PreprocessorRequestResponse;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.StateListener;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.JsonModTestResultsHandler;

public class ModZNLB extends ModObject implements StateListener {
	public static final String		NAME					= "ZNLB";
	public static final String		DESC					= 
		"The Zeesoft Natural Language Base provides a simple JSON API for language specific sentence preprocessing.";

	private Languages				languages				= null;
	private Preprocessor			preprocessor			= null;
	private ContextConfig			contextConfiguration	= null;
	
	public ModZNLB(Config config) {
		super(config);
		name = NAME;
		desc.append(DESC);
		
		languages = getNewLanguages();
		languages.initialize();
	}
	
	@Override
	public void install() {
		Preprocessor prepro = getNewPreprocessor();
		prepro.install();
		ContextConfig config = getNewContextConfig();
		config.install();
	}
	
	@Override
	public void initialize() {
		handlers.add(new JavaScriptZNLBLanguagesHandler(configuration,this));
		handlers.add(new HtmlZNLBIndexHandler(configuration,this));
		handlers.add(new JsonZNLBRequestHandler(configuration,this));
		handlers.add(new JsonModTestResultsHandler(configuration,this));
		handlers.add(new JsonZNLBLanguagesHandler(configuration,this));
		handlers.add(new JsonZNLBContextConfigurationHandler(configuration,this));
		preprocessor = getNewPreprocessor();
		preprocessor.addListener(this);
		preprocessor.initialize();
		contextConfiguration = getNewContextConfig();
		contextConfiguration.addListener(this);
		contextConfiguration.initialize();
		testers.add(getNewTester());
		super.initialize();
	}
	
	@Override
	public void destroy() {
		super.destroy();
		preprocessor.destroy();
		contextConfiguration.destroy();
	}

	@Override
	public void stateChanged(Object source,boolean open) {
		if (open &&
			(source instanceof Preprocessor || source instanceof ContextConfig)
			) {
			if (preprocessor.isInitialized() && contextConfiguration.isInitialized()) {
				startTesters();
			}
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
	
	public Languages getLanguages() {
		return languages;
	}
	
	public Preprocessor getPreprocessor() {
		return preprocessor;
	}
	
	public ContextConfig getContextConfiguration() {
		return contextConfiguration;
	}
	
	protected Languages getNewLanguages() {
		return new Languages();
	}
	
	protected Preprocessor getNewPreprocessor() {
		return new Preprocessor(configuration);
	}
	
	protected ContextConfig getNewContextConfig() {
		return new ContextConfig(configuration);
	}
	
	protected ZNLBTester getNewTester() {
		return new ZNLBTester(configuration,configuration.getModuleUrl(NAME) + JsonZNLBRequestHandler.PATH);
	}
	
	protected PreprocessorRequestHandler getNewPreprocessorRequestHandler() {
		return new PreprocessorRequestHandler(preprocessor);
	}
}
