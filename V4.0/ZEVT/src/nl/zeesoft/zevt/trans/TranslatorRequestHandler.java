package nl.zeesoft.zevt.trans;

import nl.zeesoft.zdk.ZStringSymbolParser;

public class TranslatorRequestHandler {
	private Translator translator = null;
	
	public TranslatorRequestHandler(Translator translator) {
		this.translator = translator;
	}
	
	public void handleRequest(TranslatorRequestResponse request) {
		if (request.sequence.length()>0) {
			request.entityValueTranslation = translator.translateToInternalValues(request.sequence);
			request.sequence = new ZStringSymbolParser();
		} else if (request.entityValueTranslation.length()>0) {
			request.sequence = translator.translateToExternalValues(request.entityValueTranslation);
			request.entityValueTranslation = new ZStringSymbolParser();
		}
	}
}
