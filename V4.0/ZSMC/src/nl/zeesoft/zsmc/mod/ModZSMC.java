package nl.zeesoft.zsmc.mod;

import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.StateListener;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.JsonModTestResultsHandler;
import nl.zeesoft.zsmc.mod.handler.HtmlZSMCIndexHandler;
import nl.zeesoft.zsmc.mod.handler.HtmlZSMCStateHandler;

public class ModZSMC extends ModObject implements StateListener {
	public static final String		NAME						= "ZSMC";
	public static final String		DESC						= 
		"Zeesoft Symbolic Multithreaded Confabulators provides a simple JSON API to manage multiple symbolic confabulators.";
	
	public ModZSMC(Config config) {
		super(config);
		name = NAME;
		desc.append(DESC);
	}
	
	@Override
	public void install() {
	}
	
	@Override
	public void initialize() {
		handlers.add(new HtmlZSMCIndexHandler(configuration,this));
		handlers.add(new JsonModTestResultsHandler(configuration,this));
		handlers.add(new HtmlZSMCStateHandler(configuration,this));
		super.initialize();
	}
	
	@Override
	public void destroy() {
		super.destroy();
	}

	@Override
	public void stateChanged(Object source, boolean open) {
		
	}
}
