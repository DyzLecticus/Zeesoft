package nl.zeesoft.zenn.mod;

import nl.zeesoft.zenn.animal.CarnivoreEvolver;
import nl.zeesoft.zenn.animal.HerbivoreEvolver;
import nl.zeesoft.zenn.mod.handler.HtmlZENNIndexHandler;
import nl.zeesoft.zenn.simulator.EnvironmentInitializer;
import nl.zeesoft.zenn.simulator.Simulator;
import nl.zeesoft.zenn.simulator.SimulatorAnimalInitializer;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.StateListener;
import nl.zeesoft.zodb.db.Database;
import nl.zeesoft.zodb.db.DatabaseStateListener;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.JsonModTestResultsHandler;

public class ModZENN extends ModObject implements StateListener, DatabaseStateListener {
	public static final String			NAME						= "ZENN";
	public static final String			DESC						= 
		"Zeesoft Evolutionary Neural Networks provides an interface to manage artificial life.";

	private EnvironmentInitializer		environmentInitializer		= null;
	private SimulatorAnimalInitializer	simulatorAnimalInitializer	= null;
	
	private HerbivoreEvolver			herbivoreEvolver			= null;
	private CarnivoreEvolver			carnivoreEvolver			= null;
	
	private Simulator					simulator					= null;
	
	public ModZENN(Config config) {
		super(config);
		name = NAME;
		desc.append(DESC);
		environmentInitializer = new EnvironmentInitializer(config);
		environmentInitializer.addListener(this);
		simulatorAnimalInitializer = new SimulatorAnimalInitializer(config);
		simulatorAnimalInitializer.addListener(this);
		herbivoreEvolver = new HerbivoreEvolver(config);
		carnivoreEvolver = new CarnivoreEvolver(config);
		simulator = new Simulator(config,environmentInitializer,simulatorAnimalInitializer,herbivoreEvolver,carnivoreEvolver);
		config.getZODB().getDatabase().addListener(this);
	}
	
	@Override
	public void install() {
		environmentInitializer.install();
	}
	
	@Override
	public void initialize() {
		handlers.add(new HtmlZENNIndexHandler(configuration,this));
		handlers.add(new JsonModTestResultsHandler(configuration,this));
		super.initialize();
		environmentInitializer.initialize();
		simulatorAnimalInitializer.initialize();
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
			simulator.setEnvironment(environmentInitializer.getEnvironmentConfig(),environmentInitializer.getEnvironmentState());
			if (simulatorAnimalInitializer.isInitialized()) {
				simulator.start();
			}
		} else if (source instanceof SimulatorAnimalInitializer && open) {
			if (environmentInitializer.isInitialized()) {
				simulator.start();
			}
		}
	}

	@Override
	public void keyChanged(StringBuilder newKey) {
		// Ignore
	}
}
