package nl.zeesoft.zevt.trans;

import nl.zeesoft.zdk.ZStringSymbolParser;

public class EntityRequestHandler {
	private EntityValueTranslator translator = null;
	
	public EntityRequestHandler(EntityValueTranslator translator) {
		this.translator = translator;
	}
	
	public void handleRequest(EntityRequestResponse request) {
		if (request.sequence.length()>0) {
			request.entityValueTranslation = translator.translateToInternalValues(request.sequence);
			request.sequence = new ZStringSymbolParser();
		} else if (request.entityValueTranslation.length()>0) {
			request.sequence = translator.translateToExternalValues(request.entityValueTranslation);
			request.entityValueTranslation = new ZStringSymbolParser();
		}
	}
}
