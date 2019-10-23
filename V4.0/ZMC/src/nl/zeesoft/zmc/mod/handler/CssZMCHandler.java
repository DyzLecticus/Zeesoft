package nl.zeesoft.zmc.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zmc.mod.resource.CssZMC;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.HandlerObject;

public class CssZMCHandler extends HandlerObject {
	private CssZMC css = new CssZMC();
	
	public CssZMCHandler(Config config,ModObject mod) {
		super(config,mod,"/ZMC.css");
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		return css.css;
	}
}
