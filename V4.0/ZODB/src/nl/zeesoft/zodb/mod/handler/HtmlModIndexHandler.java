package nl.zeesoft.zodb.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.resource.HtmlModIndex;

public class HtmlModIndexHandler extends HandlerObject {
	public HtmlModIndexHandler(Config config) {
		super(config,null,"/index.html");
		setAllowPost(true);
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		HtmlModIndex html = new HtmlModIndex(getConfiguration());
		return html.toStringBuilder();
	}
}
