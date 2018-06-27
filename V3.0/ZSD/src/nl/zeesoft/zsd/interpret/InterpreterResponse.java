package nl.zeesoft.zsd.interpret;

import nl.zeesoft.zdk.ZStringSymbolParser;

public class InterpreterResponse {
	public InterpreterRequest	request					= null;
	
	public String				responseLanguage		= "";
	public String				responseMasterContext	= "";
	public String				responseContext			= "";
	public ZStringSymbolParser	correctedInput			= new ZStringSymbolParser();
	public ZStringSymbolParser	entityValueTranslation	= new ZStringSymbolParser();
}
