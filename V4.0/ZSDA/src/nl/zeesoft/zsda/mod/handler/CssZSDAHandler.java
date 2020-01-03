package nl.zeesoft.zsda.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.HandlerObject;
import nl.zeesoft.zsda.mod.resource.CssZSDA;

public class CssZSDAHandler extends HandlerObject {
	private CssZSDA css = new CssZSDA();
	
	public CssZSDAHandler(Config config,ModObject mod) {
		super(config,mod,"/ZSDA.css");
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		return css.css;
	}
}
