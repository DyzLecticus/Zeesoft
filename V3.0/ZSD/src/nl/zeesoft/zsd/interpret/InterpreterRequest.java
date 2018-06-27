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
	
	public boolean				checkLanguage			= true;
	public boolean				checkMasterContext		= true;
	public boolean				checkContext			= true;
	public boolean				correctInput			= true;
	
	public boolean				translateEntiyValues	= true;
	public List<String>			translateEntityTypes	= new ArrayList<String>();				
}
