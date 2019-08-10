package nl.zeesoft.zenn.mod;

import nl.zeesoft.zenn.animal.AnimalEvolver;
import nl.zeesoft.zenn.mod.handler.HtmlZENNIndexHandler;
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

	private AnimalEvolver			herbivoreEvolver			= null;
	private AnimalEvolver			carnivoreEvolver			= null;

	public ModZENN(Config config) {
		super(config);
		name = NAME;
		desc.append(DESC);
		herbivoreEvolver = new AnimalEvolver(config,true);
		carnivoreEvolver = new AnimalEvolver(config,false);
		config.getZODB().getDatabase().addListener(this);
	}
	
	@Override
	public void install() {
	}
	
	@Override
	public void initialize() {
		handlers.add(new HtmlZENNIndexHandler(configuration,this));
		handlers.add(new JsonModTestResultsHandler(configuration,this));
		super.initialize();
		herbivoreEvolver.setDebug(configuration.isDebug());
		carnivoreEvolver.setDebug(configuration.isDebug());
	}
	
	@Override
	public void destroy() {
		herbivoreEvolver.stop();
		carnivoreEvolver.stop();
		herbivoreEvolver.whileStopping();
		carnivoreEvolver.whileStopping();
		super.destroy();
	}

	@Override
	public void stateChanged(Object source, boolean open) {
		if (source instanceof Database) {
			if (open) {
				herbivoreEvolver.load();
				carnivoreEvolver.load();
			} else {
				herbivoreEvolver.stop();
				carnivoreEvolver.stop();
				herbivoreEvolver.whileStopping();
				carnivoreEvolver.whileStopping();
			}
		}
	}

	@Override
	public void keyChanged(StringBuilder newKey) {
		// Ignore
	}
}
