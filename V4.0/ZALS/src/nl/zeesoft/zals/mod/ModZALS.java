package nl.zeesoft.zals.mod;

import nl.zeesoft.zals.confab.ConfabConfigurator;
import nl.zeesoft.zals.mod.handler.HtmlZALSIndexHandler;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.StateListener;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.JsonModTestResultsHandler;

public class ModZALS extends ModObject implements StateListener {
	public static final String			NAME			= "ZALS";
	public static final String			DESC			= 
		"The Zeesoft Artificial Life Simulator provides a simulation of artificial life using Zeesoft Symbolic Confabulators.";
	
	private ConfabConfigurator			configurator	= null;
	
	public ModZALS(Config config) {
		super(config);
		name = NAME;
		desc.append(DESC);
		configurator = new ConfabConfigurator(configuration);
		configurator.addListener(this);
	}
	
	@Override
	public void install() {
		// TODO: Install
	}
	
	@Override
	public void initialize() {
		handlers.add(new HtmlZALSIndexHandler(configuration,this));
		handlers.add(new JsonModTestResultsHandler(configuration,this));
		configurator.configureConfabulators();
		super.initialize();
	}
	
	@Override
	public void destroy() {
		super.destroy();
		// TODO: Destroy
	}

	@Override
	public void stateChanged(Object source, boolean open) {
		if (open && source instanceof ConfabConfigurator) {
			// TODO Create and implement tester
		}
	}
}
