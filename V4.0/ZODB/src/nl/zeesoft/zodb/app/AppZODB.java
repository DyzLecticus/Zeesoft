package nl.zeesoft.zodb.app;

import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.app.handler.HtmlNotFoundHandler;
import nl.zeesoft.zodb.app.handler.HtmlZODBDataManagerHandler;
import nl.zeesoft.zodb.app.handler.HtmlZODBIndexHandler;
import nl.zeesoft.zodb.app.handler.JavaScriptZODBDataManagerHandler;
import nl.zeesoft.zodb.app.handler.JavaScriptZODBHandler;
import nl.zeesoft.zodb.app.handler.JsonNotFoundHandler;
import nl.zeesoft.zodb.app.handler.JsonZODBRequestHandler;
import nl.zeesoft.zodb.db.Database;
import nl.zeesoft.zodb.db.DatabaseRequest;
import nl.zeesoft.zodb.db.DatabaseRequestHandler;
import nl.zeesoft.zodb.db.DatabaseResponse;

public class AppZODB extends AppObject {
	public static final String	NAME		= "ZODB";
	public static final String	DESC		= "The Zeesoft Object Database provides a simple JSON API to store JSON objects.";
	
	private Database			database	= null;
	
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
	public void initialize(boolean write) {
		handlers.add(new HtmlNotFoundHandler(configuration,this));
		handlers.add(new JsonNotFoundHandler(configuration,this));
		handlers.add(new HtmlZODBIndexHandler(configuration,this));
		handlers.add(new JavaScriptZODBHandler(configuration,this));
		handlers.add(new JavaScriptZODBDataManagerHandler(configuration,this));
		handlers.add(new HtmlZODBDataManagerHandler(configuration,this));
		handlers.add(new JsonZODBRequestHandler(configuration,this));
		database = getNewDatabase();
		database.start();
		super.initialize(write);
	}
	
	@Override
	public void destroy() {
		database.stop();
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
}
