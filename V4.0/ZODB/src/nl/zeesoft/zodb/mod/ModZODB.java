package nl.zeesoft.zodb.mod;

import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.Database;
import nl.zeesoft.zodb.db.DatabaseRequest;
import nl.zeesoft.zodb.db.DatabaseRequestHandler;
import nl.zeesoft.zodb.db.DatabaseResponse;
import nl.zeesoft.zodb.db.DatabaseStateListener;
import nl.zeesoft.zodb.mod.handler.HtmlZODBDataManagerHandler;
import nl.zeesoft.zodb.mod.handler.HtmlZODBIndexHandler;
import nl.zeesoft.zodb.mod.handler.JavaScriptZODBDataManagerHandler;
import nl.zeesoft.zodb.mod.handler.JavaScriptZODBHandler;
import nl.zeesoft.zodb.mod.handler.JsonModTestResultsHandler;
import nl.zeesoft.zodb.mod.handler.JsonZODBRequestHandler;

public class ModZODB extends ModObject implements DatabaseStateListener {
	public static final String	NAME		= "ZODB";
	public static final String	DESC		= "The Zeesoft Object Database provides a simple JSON API to store JSON objects.";
	
	private Database			database	= null;
	
	public ModZODB(Config config) {
		super(config);
		name = NAME;
		desc.append(DESC);
	}
	
	@Override
	public void install() {
		Database db = getNewDatabase();
		db.install();
	}
	
	@Override
	public void initialize() {
		handlers.add(new HtmlZODBIndexHandler(configuration,this));
		handlers.add(new JavaScriptZODBHandler(configuration,this));
		handlers.add(new JavaScriptZODBDataManagerHandler(configuration,this));
		handlers.add(new HtmlZODBDataManagerHandler(configuration,this));
		handlers.add(new JsonZODBRequestHandler(configuration,this));
		handlers.add(new JsonModTestResultsHandler(configuration,this));
		database = getNewDatabase();
		database.addListener(this);
		tester = getNewTester();
		database.start();
		super.initialize();
	}
	
	@Override
	public void destroy() {
		if (selfTest) {
			tester.stop();
		}
		database.stop();
	}

	@Override
	public void databaseStateChanged(boolean open) {
		if (open && selfTest) {
			tester.start();
		}
	}
	
	public Database getDatabase() {
		return database;
	}
	
	public DatabaseResponse handleRequest(DatabaseRequest request) {
		DatabaseRequestHandler handler = getNewDatabaseRequestHandler(database);
		return handler.handleDatabaseRequest(request);
	}
	
	protected Database getNewDatabase() {
		return new Database(configuration);
	}
	
	protected DatabaseRequestHandler getNewDatabaseRequestHandler(Database db) {
		return new DatabaseRequestHandler(db);
	}
	
	protected ZODBTester getNewTester() {
		return new ZODBTester(configuration,configuration.getModuleUrl(NAME) + JsonZODBRequestHandler.PATH);
	}
}
