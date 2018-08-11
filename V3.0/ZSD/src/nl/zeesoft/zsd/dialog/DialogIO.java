package nl.zeesoft.zsd.dialog;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringSymbolParser;

public class DialogIO {
	public ZStringSymbolParser	input			= new ZStringSymbolParser();
	public ZStringSymbolParser	output			= new ZStringSymbolParser();
	public List<String>			filterContexts	= new ArrayList<String>();
}
