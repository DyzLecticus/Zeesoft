package nl.zeesoft.zsds.handler;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zsds.AppConfiguration;

public class JsonStateHandler extends JsonSelfTestHandler {
	public JsonStateHandler(AppConfiguration config) {
		super(config,"/state.json");
		setCheckGenerating(true);
		setCheckReloading(true);
		setCheckTesting(true);
	}
	
	@Override
	protected ZStringBuilder buildResponse() {
		ZStringBuilder r = super.buildResponse();
		if (r.length()==0) {
			r = null;
		}
		return r;
	}
}
