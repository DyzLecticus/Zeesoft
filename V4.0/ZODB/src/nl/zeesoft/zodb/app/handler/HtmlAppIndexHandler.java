package nl.zeesoft.zodb.app.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.app.resource.HtmlAppIndex;

public class HtmlAppIndexHandler extends HandlerObject {
	public HtmlAppIndexHandler(Config config) {
		super(config,null,"/index.html");
		setAllowPost(true);
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		HtmlAppIndex html = new HtmlAppIndex(getConfiguration());
		return html.toStringBuilder();
	}
}
