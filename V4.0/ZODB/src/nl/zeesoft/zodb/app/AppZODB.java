package nl.zeesoft.zodb.app;

import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.app.handler.HtmlNotFoundHandler;
import nl.zeesoft.zodb.app.handler.JsonNotFoundHandler;

public class AppZODB extends AppObject {
	public static final String	NAME	= "ZODB";
	
	public AppZODB(Config config) {
		super(config);
		name = NAME;
	}
	
	@Override
	public void install() {
		// TODO: Implement
	}
	
	@Override
	public void initialize(boolean write) {
		handlers.add(new HtmlNotFoundHandler(configuration,this));
		handlers.add(new JsonNotFoundHandler(configuration,this));
		super.initialize(write);
	}
}
