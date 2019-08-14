package nl.zeesoft.zenn.mod;

import nl.zeesoft.zenn.animal.CarnivoreEvolver;
import nl.zeesoft.zenn.animal.HerbivoreEvolver;
import nl.zeesoft.zenn.mod.handler.HtmlZENNIndexHandler;
import nl.zeesoft.zenn.simulator.EnvironmentInitializer;
import nl.zeesoft.zenn.simulator.Simulator;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.StateListener;
import nl.zeesoft.zodb.db.Database;
import nl.zeesoft.zodb.db.DatabaseStateListener;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.JsonModTestResultsHandler;

public class ModZENN extends ModObject implements StateListener, DatabaseStateListener {
	public static final String		NAME						= "ZENN";
	public static final String		DESC						= 
		"Zeesoft Evolutionary Neural Networks provides an interface to manage artificial life.";

	private EnvironmentInitializer	environmentInitializer		= null;
	
	private HerbivoreEvolver		herbivoreEvolver			= null;
	private CarnivoreEvolver		carnivoreEvolver			= null;
	
	private Simulator				simulator					= null;
	
	public ModZENN(Config config) {
		super(config);
		name = NAME;
		desc.append(DESC);
		environmentInitializer = new EnvironmentInitializer(config);
		environmentInitializer.addListener(this);
		herbivoreEvolver = new HerbivoreEvolver(config);
		carnivoreEvolver = new CarnivoreEvolver(config);
		simulator = new Simulator(config,environmentInitializer,herbivoreEvolver,carnivoreEvolver);
		config.getZODB().getDatabase().addListener(this);
	}
	
	@Override
	public void install() {
		EnvironmentInitializer envInit = new EnvironmentInitializer(configuration);
		envInit.install();
	}
	
	@Override
	public void initialize() {
		handlers.add(new HtmlZENNIndexHandler(configuration,this));
		handlers.add(new JsonModTestResultsHandler(configuration,this));
		super.initialize();
		environmentInitializer.initialize();
		herbivoreEvolver.setDebug(configuration.isDebug());
		carnivoreEvolver.setDebug(configuration.isDebug());
	}
	
	@Override
	public void destroy() {
		environmentInitializer.destroy();
		herbivoreEvolver.stop();
		carnivoreEvolver.stop();
		herbivoreEvolver.whileStopping();
		carnivoreEvolver.whileStopping();
		simulator.stop();
		simulator.destroy();
		super.destroy();
	}

	@Override
	public void stateChanged(Object source, boolean open) {
		if (source instanceof Database) {
			if (open) {
				herbivoreEvolver.load();
				carnivoreEvolver.load();
				environmentInitializer.reinitialize();
			} else {
				boolean waitHerb = false;
				boolean waitCarn = false;
				if (herbivoreEvolver.isWorking()) {
					herbivoreEvolver.stop();
					waitHerb = true;
				}
				if (carnivoreEvolver.isWorking()) {
					carnivoreEvolver.stop();
					waitCarn = true;
				}
				if (waitHerb) {
					herbivoreEvolver.whileStopping();
				}
				if (waitCarn) {
					carnivoreEvolver.whileStopping();
				}
				if (simulator.isWorking()) {
					simulator.stop();
				}
			}
		} else if (source instanceof EnvironmentInitializer && open) {
			simulator.setEnvironment(environmentInitializer.getEnvironment());
			simulator.start();
		}
	}

	@Override
	public void keyChanged(StringBuilder newKey) {
		// Ignore
	}
}
