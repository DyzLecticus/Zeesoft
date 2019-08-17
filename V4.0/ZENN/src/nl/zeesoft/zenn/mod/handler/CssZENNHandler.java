package nl.zeesoft.zenn.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zenn.mod.resource.CssZENN;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.HandlerObject;

public class CssZENNHandler extends HandlerObject {
	private CssZENN css = new CssZENN();
	
	public CssZENNHandler(Config config,ModObject mod) {
		super(config,mod,"/ZENN.css");
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		return css.css;
	}
}
