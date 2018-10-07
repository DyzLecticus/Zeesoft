package nl.zeesoft.zals.mod;

import nl.zeesoft.zals.confab.ConfabConfigurator;
import nl.zeesoft.zals.env.EnvironmentInitializer;
import nl.zeesoft.zals.mod.handler.HtmlZALSIndexHandler;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.StateListener;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.JsonModTestResultsHandler;

public class ModZALS extends ModObject implements StateListener {
	public static final String			NAME			= "ZALS";
	public static final String			DESC			= 
		"The Zeesoft Artificial Life Simulator provides a simulation of artificial life using Zeesoft Symbolic Confabulators.";
	
	private EnvironmentInitializer		initializer		= null;
	private ConfabConfigurator			configurator	= null;
	
	public ModZALS(Config config) {
		super(config);
		name = NAME;
		desc.append(DESC);
		initializer = new EnvironmentInitializer(configuration);
		initializer.addListener(this);
		configurator = new ConfabConfigurator(configuration);
		configurator.addListener(this);
	}
	
	@Override
	public void install() {
		initializer.install();
	}
	
	@Override
	public void initialize() {
		handlers.add(new HtmlZALSIndexHandler(configuration,this));
		handlers.add(new JsonModTestResultsHandler(configuration,this));
		initializer.initialize();
		super.initialize();
	}
	
	@Override
	public void destroy() {
		super.destroy();
		initializer.destroy();
	}

	@Override
	public void stateChanged(Object source, boolean open) {
		if (open) {
			if (source instanceof EnvironmentInitializer) {
				configurator.configureConfabulators(initializer.getEnvironment());
			} else if (source instanceof ConfabConfigurator) {
				// TODO Start simulation
				configuration.debug(this,"Ready to start simulator");
			}
		}
		// TODO Create and implement tester
	}
}
