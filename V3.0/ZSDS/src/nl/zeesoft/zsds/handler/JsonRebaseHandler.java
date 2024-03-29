package nl.zeesoft.zsds.handler;

import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zsds.AppConfiguration;

public class JsonRebaseHandler extends JsonBaseHandlerObject {
	public JsonRebaseHandler(AppConfiguration config) {
		super(config,"/rebase.json");
		setAllowGet(false);
		setCheckTesting(true);
	}
	
	@Override
	protected ZStringBuilder buildPostResponse(HttpServletResponse response,JsFile json) {
		ZStringBuilder r = new ZStringBuilder();
		if (!getConfiguration().getBase().isSelfTest()) {
			r = getResponse(400,getConfiguration().getBase().getName() + " does not support self testing.");
		} else if (getConfiguration().rebase()) {
			r = getResponse(200,getConfiguration().getBase().getName() + " has been rebased.");
		} else {
			r = setErrorResponse(response,503,"Failed to rebase. Please try again later.");
		}
		return r;
	}
}
