package nl.zeesoft.zsds.handler;

import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zsds.AppConfiguration;

public class JsonReloadHandler extends JsonBaseHandlerObject {
	public JsonReloadHandler(AppConfiguration config) {
		super(config,"/reload.json");
		setAllowGet(false);
		setCheckTesting(true);
	}
	
	@Override
	protected ZStringBuilder buildPostResponse(HttpServletResponse response,JsFile json) {
		ZStringBuilder r = new ZStringBuilder();
		if (getConfiguration().reload()) {
			r = getResponse(200,getConfiguration().getBase().getName() + " is refreshing its memory.");
		} else {
			r = setErrorResponse(response,503,getConfiguration().getBase().getName() + " is already refreshing its memory. Please wait.");
		}
		return r;
	}
}
