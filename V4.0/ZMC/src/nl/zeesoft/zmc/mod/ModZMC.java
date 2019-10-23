package nl.zeesoft.zmc.mod;

import nl.zeesoft.zmc.mod.handler.CssZMCHandler;
import nl.zeesoft.zmc.mod.handler.HtmlZMCIndexHandler;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.StateListener;
import nl.zeesoft.zodb.db.Database;
import nl.zeesoft.zodb.db.DatabaseStateListener;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.JsonModTestResultsHandler;

public class ModZMC extends ModObject implements StateListener, DatabaseStateListener {
	public static final String			NAME						= "ZMC";
	public static final String			DESC						= 
		"Zeesoft MIDI Composer provides an interface to create MIDI compositions supported by artificial intelligence.";

	public ModZMC(Config config) {
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
		handlers.add(new CssZMCHandler(configuration,this));
		handlers.add(new HtmlZMCIndexHandler(configuration,this));
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
