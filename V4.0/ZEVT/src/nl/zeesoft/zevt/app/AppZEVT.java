package nl.zeesoft.zevt.app;

import nl.zeesoft.zevt.ZEVTConfig;
import nl.zeesoft.zevt.app.handler.HtmlZEVTEntityTranslatorHandler;
import nl.zeesoft.zevt.app.handler.HtmlZEVTIndexHandler;
import nl.zeesoft.zevt.app.handler.JavaScriptZEVTEntityTranslatorHandler;
import nl.zeesoft.zevt.app.handler.JsonZEVTLanguagesHandler;
import nl.zeesoft.zevt.app.handler.JsonZEVTRequestHandler;
import nl.zeesoft.zevt.app.handler.JsonZEVTTypesHandler;
import nl.zeesoft.zevt.trans.Translator;
import nl.zeesoft.zevt.trans.TranslatorClient;
import nl.zeesoft.zevt.trans.TranslatorClientListener;
import nl.zeesoft.zevt.trans.TranslatorRequestHandler;
import nl.zeesoft.zevt.trans.TranslatorRequestResponse;
import nl.zeesoft.zevt.trans.TranslatorStateListener;
import nl.zeesoft.zodb.app.AppObject;
import nl.zeesoft.zodb.app.handler.JsonAppTestResultsHandler;

public class AppZEVT extends AppObject implements TranslatorStateListener {
	public static final String		NAME			= "ZEVT";
	public static final String		DESC			= 
		"The Zeesoft Entity Value Translator provides a simple JSON API to translate sentences to and from language specific entity values.";
	
	private Translator				translator		= null;
	
	public AppZEVT(ZEVTConfig config) {
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
		handlers.add(new JsonAppTestResultsHandler(configuration,this));
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
		client.handleRequest(request,configuration.getApplicationUrl(NAME) + JsonZEVTRequestHandler.PATH,listener);
	}
	
	protected Translator getNewEntityValueTranslator() {
		return new Translator(configuration);
	}
	
	protected ZEVTTester getNewTester() {
		return new ZEVTTester(configuration,configuration.getApplicationUrl(NAME) + JsonZEVTRequestHandler.PATH);
	}
}
