package nl.zeesoft.zenn.mod;

import nl.zeesoft.zenn.animal.CarnivoreEvolver;
import nl.zeesoft.zenn.animal.CarnivoreMutator;
import nl.zeesoft.zenn.animal.HerbivoreEvolver;
import nl.zeesoft.zenn.animal.HerbivoreMutator;
import nl.zeesoft.zenn.mod.handler.CssZENNHandler;
import nl.zeesoft.zenn.mod.handler.HtmlZENNEnvironmentHandler;
import nl.zeesoft.zenn.mod.handler.HtmlZENNIndexHandler;
import nl.zeesoft.zenn.mod.handler.JavaScriptZENNEnvironmentHandler;
import nl.zeesoft.zenn.mod.handler.JsonZENNEnvironmentHandler;
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
	private HerbivoreMutator			herbivoreMutator			= null;
	private CarnivoreMutator			carnivoreMutator			= null;
	
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
		herbivoreMutator = new HerbivoreMutator(config,herbivoreEvolver);
		carnivoreMutator = new CarnivoreMutator(config,carnivoreEvolver);
		simulator = new Simulator(
			config,environmentInitializer,simulatorAnimalInitializer,herbivoreEvolver,carnivoreEvolver,herbivoreMutator,carnivoreMutator);
		config.getZODB().getDatabase().addListener(this);
	}
	
	@Override
	public void install() {
		environmentInitializer.install();
		simulatorAnimalInitializer.install();
	}
	
	@Override
	public void initialize() {
		handlers.add(new CssZENNHandler(configuration,this));
		handlers.add(new HtmlZENNIndexHandler(configuration,this));
		handlers.add(new HtmlZENNEnvironmentHandler(configuration,this));
		handlers.add(new JsonZENNEnvironmentHandler(configuration,this));
		handlers.add(new JavaScriptZENNEnvironmentHandler(configuration,this));
		handlers.add(new JsonModTestResultsHandler(configuration,this));
		super.initialize();
		environmentInitializer.initialize();
		simulatorAnimalInitializer.initialize();
		herbivoreEvolver.setDebug(configuration.isDebug());
		carnivoreEvolver.setDebug(configuration.isDebug());
		herbivoreMutator.setDebug(configuration.isDebug());
		carnivoreMutator.setDebug(configuration.isDebug());
	}
	
	@Override
	public void destroy() {
		environmentInitializer.destroy();
		herbivoreEvolver.stop();
		carnivoreEvolver.stop();
		herbivoreMutator.stop();
		carnivoreMutator.stop();
		herbivoreEvolver.whileStopping();
		carnivoreEvolver.whileStopping();
		herbivoreMutator.whileStopping();
		carnivoreMutator.whileStopping();
		simulator.stop();
		simulator.destroy();
		super.destroy();
	}

	public Simulator getSimulator() {
		return simulator;
	}
	
	@Override
	public void stateChanged(Object source, boolean open) {
		if (source instanceof EnvironmentInitializer && open) {
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
	public void databaseStateChanged(String state) {
		if (state.equals(Database.STAT_OPEN)) {
			herbivoreEvolver.load();
			carnivoreEvolver.load();
			herbivoreMutator.load();
			carnivoreMutator.load();
			environmentInitializer.reinitialize();
		} else if (state.equals(Database.STAT_STOPPING)) {
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
			waitHerb = false;
			waitCarn = false;
			if (herbivoreMutator.isWorking()) {
				herbivoreMutator.stop();
				waitHerb = true;
			}
			if (carnivoreMutator.isWorking()) {
				carnivoreMutator.stop();
				waitCarn = true;
			}
			if (waitHerb) {
				herbivoreMutator.whileStopping();
			}
			if (waitCarn) {
				carnivoreMutator.whileStopping();
			}
			if (simulator.isWorking()) {
				simulator.stop();
			}
		}
	}
}
