package nl.zeesoft.zsd.interpret;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringSymbolParser;

public class InterpreterRequest {
	public ZStringSymbolParser	prompt					= new ZStringSymbolParser();
	public ZStringSymbolParser	input					= new ZStringSymbolParser();
	public String				language				= "";
	public String				masterContext			= "";
	public String				context					= "";
	
	public boolean				checkLanguage			= false;
	public boolean				correctInput			= false;
	public boolean				checkMasterContext		= false;
	public boolean				checkContext			= false;
	
	public boolean				translateEntiyValues	= false;
	public List<String>			translateEntityTypes	= new ArrayList<String>();

	public void setAllActions(boolean value) {
		checkLanguage = value;
		correctInput = value;
		checkMasterContext = value;
		checkContext = value;
		translateEntiyValues = value;
	}
}
