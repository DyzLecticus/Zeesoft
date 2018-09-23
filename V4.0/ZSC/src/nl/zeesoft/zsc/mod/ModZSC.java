package nl.zeesoft.zsc.mod;

import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.JsonModTestResultsHandler;
import nl.zeesoft.zsc.mod.handler.HtmlZSCIndexHandler;

public class ModZSC extends ModObject {
	public static final String		NAME			= "ZSC";
	public static final String		DESC			= 
		"Zeesoft Symbolic Confabulators provides a simple JSON API to manage multiple symbolic confabulators.";
	
	public ModZSC(Config config) {
		super(config);
		name = NAME;
		desc.append(DESC);
	}
	
	@Override
	public void install() {
	}
	
	@Override
	public void initialize() {
		handlers.add(new HtmlZSCIndexHandler(configuration,this));
		handlers.add(new JsonModTestResultsHandler(configuration,this));
		super.initialize();
	}
	
	@Override
	public void destroy() {
		super.destroy();
	}
}
