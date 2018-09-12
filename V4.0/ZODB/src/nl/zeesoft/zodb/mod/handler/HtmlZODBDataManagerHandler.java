package nl.zeesoft.zodb.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.resource.HtmlZODBDataManager;

public class HtmlZODBDataManagerHandler extends HandlerObject {
	public HtmlZODBDataManagerHandler(Config config,ModObject mod) {
		super(config,mod,"/dataManager.html");
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		HtmlZODBDataManager html = new HtmlZODBDataManager(getConfiguration());
		return html.toStringBuilder();
	}
}
