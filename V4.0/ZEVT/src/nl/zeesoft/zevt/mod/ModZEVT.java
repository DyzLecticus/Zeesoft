package nl.zeesoft.zevt.mod;

import nl.zeesoft.zevt.ZEVTConfig;
import nl.zeesoft.zevt.mod.handler.HtmlZEVTEntityTranslatorHandler;
import nl.zeesoft.zevt.mod.handler.HtmlZEVTIndexHandler;
import nl.zeesoft.zevt.mod.handler.JavaScriptZEVTEntityTranslatorHandler;
import nl.zeesoft.zevt.mod.handler.JsonZEVTLanguagesHandler;
import nl.zeesoft.zevt.mod.handler.JsonZEVTRequestHandler;
import nl.zeesoft.zevt.mod.handler.JsonZEVTTypesHandler;
import nl.zeesoft.zevt.trans.Translator;
import nl.zeesoft.zevt.trans.TranslatorClient;
import nl.zeesoft.zevt.trans.TranslatorClientListener;
import nl.zeesoft.zevt.trans.TranslatorRequestHandler;
import nl.zeesoft.zevt.trans.TranslatorRequestResponse;
import nl.zeesoft.zevt.trans.TranslatorStateListener;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.JsonModTestResultsHandler;

public class ModZEVT extends ModObject implements TranslatorStateListener {
	public static final String		NAME			= "ZEVT";
	public static final String		DESC			= 
		"The Zeesoft Entity Value Translator provides a simple JSON API to translate sentences to and from language specific entity values.";
	
	private Translator				translator		= null;
	
	public ModZEVT(ZEVTConfig config) {
		super(config);
		name = NAME;
		desc.append(DESC);
	}
	
	@Override
	public void install() {
		Translator entityValueTranslator = getNewEntityValueTranslator();
		entityValueTranslator.install();
	}
	
	@Override
	public void initialize() {
		translator = getNewEntityValueTranslator();
		translator.listeners.add(this);
		handlers.add(new HtmlZEVTIndexHandler(configuration,this));
		handlers.add(new HtmlZEVTEntityTranslatorHandler(configuration,this));
		handlers.add(new JavaScriptZEVTEntityTranslatorHandler(configuration,this));
		handlers.add(new JsonZEVTRequestHandler(configuration,this));
		handlers.add(new JsonZEVTLanguagesHandler(configuration,this));
		handlers.add(new JsonZEVTTypesHandler(configuration,this));
		handlers.add(new JsonModTestResultsHandler(configuration,this));
		translator.initialize();
		tester = getNewTester();
		super.initialize();
	}
	
	@Override
	public void destroy() {
		if (selfTest) {
			tester.stop();
		}
		translator.destroy();
	}

	@Override
	public void translatorStateChanged(boolean open) {
		if (open && selfTest) {
			tester.start();
		}
	}
	
	public Translator getTranslator() {
		return translator;
	}
	
	public void handleRequest(TranslatorRequestResponse request) {
		TranslatorRequestHandler handler = new TranslatorRequestHandler(translator);
		handler.handleRequest(request);
	}

	public void handleRequest(TranslatorRequestResponse request,TranslatorClientListener listener) {
		TranslatorClient client = new TranslatorClient(configuration);
		client.handleRequest(request,configuration.getModuleUrl(NAME) + JsonZEVTRequestHandler.PATH,listener);
	}
	
	protected Translator getNewEntityValueTranslator() {
		return new Translator(configuration);
	}
	
	protected ZEVTTester getNewTester() {
		return new ZEVTTester(configuration,configuration.getModuleUrl(NAME) + JsonZEVTRequestHandler.PATH);
	}
}
