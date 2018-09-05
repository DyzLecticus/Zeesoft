package nl.zeesoft.zodb.app.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.app.AppObject;
import nl.zeesoft.zodb.app.resource.HtmlZODBIndex;

public class HtmlZODBIndexHandler extends HandlerObject {
	public HtmlZODBIndexHandler(Config config,AppObject app) {
		super(config,app,"/index.html");
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		HtmlZODBIndex html = new HtmlZODBIndex(getConfiguration());
		return html.toStringBuilder();
	}
}
