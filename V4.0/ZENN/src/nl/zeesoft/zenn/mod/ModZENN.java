package nl.zeesoft.zenn.mod;

import nl.zeesoft.zenn.mod.handler.HtmlZENNIndexHandler;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.StateListener;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.JsonModTestResultsHandler;

public class ModZENN extends ModObject implements StateListener {
	public static final String		NAME						= "ZENN";
	public static final String		DESC						= 
		"Zeesoft Evolutionary Neural Networks provides an interface to manage artificial life.";

	public ModZENN(Config config) {
		super(config);
		name = NAME;
		desc.append(DESC);
	}
	
	@Override
	public void install() {
	}
	
	@Override
	public void initialize() {
		handlers.add(new HtmlZENNIndexHandler(configuration,this));
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
