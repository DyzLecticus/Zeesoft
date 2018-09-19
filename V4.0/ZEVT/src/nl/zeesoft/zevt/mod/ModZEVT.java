package nl.zeesoft.zevt.mod;

import nl.zeesoft.zdk.json.JsAbleClient;
import nl.zeesoft.zdk.json.JsAbleClientRequest;
import nl.zeesoft.zdk.json.JsClientListener;
import nl.zeesoft.zdk.json.JsClientResponse;
import nl.zeesoft.zevt.mod.handler.HtmlZEVTEntityTranslatorHandler;
import nl.zeesoft.zevt.mod.handler.HtmlZEVTIndexHandler;
import nl.zeesoft.zevt.mod.handler.JavaScriptZEVTEntityTranslatorHandler;
import nl.zeesoft.zevt.mod.handler.JsonZEVTRequestHandler;
import nl.zeesoft.zevt.mod.handler.JsonZEVTTypesHandler;
import nl.zeesoft.zevt.trans.Translator;
import nl.zeesoft.zevt.trans.TranslatorRequestHandler;
import nl.zeesoft.zevt.trans.TranslatorRequestResponse;
import nl.zeesoft.zevt.type.Types;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.StateListener;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.JsonModTestResultsHandler;

public class ModZEVT extends ModObject implements StateListener {
	public static final String		NAME			= "ZEVT";
	public static final String		DESC			= 
		"The Zeesoft Entity Value Translator provides a simple JSON API to translate sentences to and from language specific entity values.";
	
	private Types					types			= null;
	private Translator				translator		= null;
	
	public ModZEVT(Config config) {
		super(config);
		name = NAME;
		desc.append(DESC);
		types = new Types();
		types.initialize();
		translator = getNewTranslator();
		translator.listeners.add(this);
	}
	
	@Override
	public void install() {
		Translator entityValueTranslator = getNewTranslator();
		entityValueTranslator.install();
	}
	
	@Override
	public void initialize() {
		handlers.add(new HtmlZEVTIndexHandler(configuration,this));
		handlers.add(new HtmlZEVTEntityTranslatorHandler(configuration,this));
		handlers.add(new JavaScriptZEVTEntityTranslatorHandler(configuration,this));
		handlers.add(new JsonZEVTRequestHandler(configuration,this));
		handlers.add(new JsonZEVTTypesHandler(configuration,this));
		handlers.add(new JsonModTestResultsHandler(configuration,this));
		translator.initialize();
		testers.add(getNewTester());
		super.initialize();
	}
	
	@Override
	public void destroy() {
		super.destroy();
		translator.destroy();
	}

	@Override
	public void stateChanged(Object source,boolean open) {
		if (source instanceof Translator && open) {
			startTesters();
		}
	}
	
	public Types getTypes() {
		return types;
	}
	
	public Translator getTranslator() {
		return translator;
	}
	
	public void handleRequest(TranslatorRequestResponse request) {
		TranslatorRequestHandler handler = new TranslatorRequestHandler(translator);
		handler.handleRequest(request);
	}

	public void handleRequest(TranslatorRequestResponse request,JsClientListener listener) {
		JsAbleClient client = new JsAbleClient(configuration.getMessenger(),configuration.getUnion());
		client.addJsClientListener(listener);
		client.handleRequest(request,configuration.getModuleUrl(NAME) + JsonZEVTRequestHandler.PATH,request);
	}
	
	public TranslatorRequestResponse handledRequest(JsClientResponse response) {
		TranslatorRequestResponse r = null;
		if (response.request instanceof JsAbleClientRequest &&
			((JsAbleClientRequest) response.request).resObject instanceof TranslatorRequestResponse 
			) {
			r = (TranslatorRequestResponse) ((JsAbleClientRequest) response.request).resObject;
		}
		return r;
	}
	
	protected Translator getNewTranslator() {
		return new Translator(configuration);
	}
	
	protected ZEVTTester getNewTester() {
		return new ZEVTTester(configuration,configuration.getModuleUrl(NAME) + JsonZEVTRequestHandler.PATH);
	}
}
