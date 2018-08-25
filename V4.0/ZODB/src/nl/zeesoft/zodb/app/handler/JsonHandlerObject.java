package nl.zeesoft.zodb.app.handler;

import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.app.AppObject;

public abstract class JsonHandlerObject extends HandlerObject {
	public JsonHandlerObject(Config config, AppObject app, String path) {
		super(config, app, path);
		setContentType("application/json");
	}
}
