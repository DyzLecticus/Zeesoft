package nl.zeesoft.zsds.handler;

import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zsds.AppConfiguration;

public class JsonGenerateHandler extends JsonBaseHandlerObject {
	public JsonGenerateHandler(AppConfiguration config) {
		super(config,"/generate.json");
		setAllowGet(false);
		setCheckGenerating(true);
		setCheckReloading(true);
		setCheckTesting(true);
	}
	
	@Override
	protected ZStringBuilder buildPostResponse(HttpServletResponse response,JsFile json) {
		ZStringBuilder r = new ZStringBuilder();
		if (getConfiguration().generate()) {
			r = getResponse(200,getConfiguration().getBaseConfig().getName() + " is regenerating its memory.");
		} else {
			r = setErrorResponse(response,503,getConfiguration().getBaseConfig().getName() + " is already regenerating its memory. Please wait.");
		}
		return r;
	}
}
