package nl.zeesoft.zodb.app.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.app.AppObject;
import nl.zeesoft.zodb.app.resource.HtmlZODBDataManager;

public class HtmlZODBDataManagerHandler extends HandlerObject {
	public HtmlZODBDataManagerHandler(Config config,AppObject app) {
		super(config,app,"/dataManager.html");
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		HtmlZODBDataManager html = new HtmlZODBDataManager(getConfiguration());
		return html.toStringBuilder();
	}
}
