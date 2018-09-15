package nl.zeesoft.zevt.trans;

import nl.zeesoft.zdk.ZStringSymbolParser;

public class TranslatorRequestHandler {
	private Translator translator = null;
	
	public TranslatorRequestHandler(Translator translator) {
		this.translator = translator;
	}
	
	public void handleRequest(TranslatorRequestResponse request) {
		if (request.sequence.length()>0) {
			request.translation = translator.translateToInternalValues(request.sequence,request.languages,null);
			request.sequence = new ZStringSymbolParser();
		} else if (request.translation.length()>0) {
			request.sequence = translator.translateToExternalValues(request.translation);
			request.translation = new ZStringSymbolParser();
		}
	}
}
