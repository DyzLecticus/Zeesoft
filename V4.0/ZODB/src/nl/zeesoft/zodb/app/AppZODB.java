package nl.zeesoft.zodb.app;

import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.app.handler.HtmlNotFoundHandler;
import nl.zeesoft.zodb.app.handler.HtmlZODBIndexHandler;
import nl.zeesoft.zodb.app.handler.JsonNotFoundHandler;
import nl.zeesoft.zodb.db.Database;
import nl.zeesoft.zodb.db.DatabaseRequest;
import nl.zeesoft.zodb.db.DatabaseRequestHandler;
import nl.zeesoft.zodb.db.DatabaseResponse;

public class AppZODB extends AppObject {
	public static final String	NAME		= "ZODB";
	
	private Database			database	= null;
	
	public AppZODB(Config config) {
		super(config);
		name = NAME;
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
