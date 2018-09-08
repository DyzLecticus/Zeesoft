package nl.zeesoft.zevt.app;

import nl.zeesoft.zevt.ZEVTConfig;
import nl.zeesoft.zevt.app.handler.HtmlZEVTEntityTranslatorHandler;
import nl.zeesoft.zevt.app.handler.HtmlZEVTIndexHandler;
import nl.zeesoft.zevt.app.handler.JavaScriptZEVTEntityTranslatorHandler;
import nl.zeesoft.zevt.app.handler.JsonZEVTRequestHandler;
import nl.zeesoft.zevt.trans.EntityRequestHandler;
import nl.zeesoft.zevt.trans.EntityRequestResponse;
import nl.zeesoft.zevt.trans.EntityValueTranslator;
import nl.zeesoft.zodb.app.AppObject;

public class AppZEVT extends AppObject {
	public static final String		NAME					= "ZEVT";
	public static final String		DESC					= 
		"The Zeesoft Entity Value Translator provides a simple JSON API to translate sentences to and from language specific entity values.";
	
	private EntityValueTranslator	entityValueTranslator	= null;
	
	public AppZEVT(ZEVTConfig config) {
		super(config);
		name = NAME;
		desc.append(DESC);
	}
	
	@Override
	public void install() {
		EntityValueTranslator entityValueTranslator = getNewEntityValueTranslator();
		entityValueTranslator.install();
	}
	
	@Override
	public void initialize(boolean write) {
		handlers.add(new HtmlZEVTIndexHandler(configuration,this));
		handlers.add(new HtmlZEVTEntityTranslatorHandler(configuration,this));
		handlers.add(new JavaScriptZEVTEntityTranslatorHandler(configuration,this));
		handlers.add(new JsonZEVTRequestHandler(configuration,this));
		entityValueTranslator = getNewEntityValueTranslator();
		entityValueTranslator.initialize();
		super.initialize(write);
	}
	
	@Override
	public void destroy() {
		entityValueTranslator.destroy();
	}
	
	public EntityValueTranslator getEntityValueTranslator() {
		return entityValueTranslator;
	}
	
	public void handleRequest(EntityRequestResponse request) {
		EntityRequestHandler handler = new EntityRequestHandler(entityValueTranslator);
		handler.handleRequest(request);
	}
	
	protected EntityValueTranslator getNewEntityValueTranslator() {
		return new EntityValueTranslator(configuration);
	}
}
