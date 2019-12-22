package nl.zeesoft.zsda.mod;

import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.StateListener;
import nl.zeesoft.zodb.db.Database;
import nl.zeesoft.zodb.db.DatabaseStateListener;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.JsonModTestResultsHandler;
import nl.zeesoft.zsda.mod.handler.HtmlZSDAIndexHandler;

public class ModZSDA extends ModObject implements StateListener, DatabaseStateListener {
	public static final String			NAME						= "ZSDA";
	public static final String			DESC						= 
		"Zeesoft Streaming Data Analyzer provides a configurable grid of SDR processors to analyze data streams.";

	public ModZSDA(Config config) {
		super(config);
		name = NAME;
		desc.append(DESC);
		config.getZODB().getDatabase().addListener(this);
	}
	
	@Override
	public void install() {
		// ...
	}
	
	@Override
	public void initialize() {
		handlers.add(new HtmlZSDAIndexHandler(configuration,this));
		handlers.add(new JsonModTestResultsHandler(configuration,this));
		super.initialize();
	}
	
	@Override
	public void destroy() {
		// ...
		super.destroy();
	}
	
	@Override
	public void stateChanged(Object source, boolean open) {
		// ...
	}

	@Override
	public void databaseStateChanged(String state) {
		// ...
		if (state.equals(Database.STAT_OPEN)) {
		} else if (state.equals(Database.STAT_STOPPING)) {
		}
	}
}
