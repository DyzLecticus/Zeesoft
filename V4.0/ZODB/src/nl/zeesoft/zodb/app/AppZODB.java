package nl.zeesoft.zodb.app;

import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.app.handler.HtmlZODBDataManagerHandler;
import nl.zeesoft.zodb.app.handler.HtmlZODBIndexHandler;
import nl.zeesoft.zodb.app.handler.JavaScriptZODBDataManagerHandler;
import nl.zeesoft.zodb.app.handler.JavaScriptZODBHandler;
import nl.zeesoft.zodb.app.handler.JsonZODBRequestHandler;
import nl.zeesoft.zodb.app.handler.JsonZODBTestResultsHandler;
import nl.zeesoft.zodb.db.Database;
import nl.zeesoft.zodb.db.DatabaseRequest;
import nl.zeesoft.zodb.db.DatabaseRequestHandler;
import nl.zeesoft.zodb.db.DatabaseResponse;
import nl.zeesoft.zodb.db.DatabaseStateListener;

public class AppZODB extends AppObject implements DatabaseStateListener {
	public static final String	NAME		= "ZODB";
	public static final String	DESC		= "The Zeesoft Object Database provides a simple JSON API to store JSON objects.";
	
	private Database			database	= null;
	private ZODBTester			tester		= null; 
	
	public AppZODB(Config config) {
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
		handlers.add(new JsonZODBTestResultsHandler(configuration,this));
		database = getNewDatabase();
		database.addListener(this);
		tester = getNewTester();
		database.start();
		super.initialize();
	}
	
	@Override
	public void destroy() {
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
	
	public ZODBTester getTester() {
		return tester;
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
		return new ZODBTester(configuration,configuration.getApplicationUrl(NAME) + JsonZODBRequestHandler.PATH);
	}
}
