package nl.zeesoft.zodb.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.resource.HtmlZODBIndexManager;

public class HtmlZODBIndexManagerHandler extends HandlerObject {
	public HtmlZODBIndexManagerHandler(Config config,ModObject mod) {
		super(config,mod,"/indexManager.html");
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		HtmlZODBIndexManager html = new HtmlZODBIndexManager(getConfiguration());
		return html.toStringBuilder();
	}
}
