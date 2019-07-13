package nl.zeesoft.zbe.mod;

import nl.zeesoft.zbe.mod.handler.HtmlZBEIndexHandler;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.StateListener;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.JsonModTestResultsHandler;

public class ModZBE extends ModObject implements StateListener {
	public static final String		NAME						= "ZBE";
	public static final String		DESC						= 
		"Zeesoft Brain Evolver provides a simple JSON API to manage the evolution of artificial brains.";

	public ModZBE(Config config) {
		super(config);
		name = NAME;
		desc.append(DESC);
	}
	
	@Override
	public void install() {
	}
	
	@Override
	public void initialize() {
		handlers.add(new HtmlZBEIndexHandler(configuration,this));
		handlers.add(new JsonModTestResultsHandler(configuration,this));
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
