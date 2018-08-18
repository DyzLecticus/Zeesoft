package nl.zeesoft.zsds.handler;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zsds.AppConfiguration;

public class JsonConfigHandler extends JsonBaseHandlerObject {
	public JsonConfigHandler(AppConfiguration config) {
		super(config,"/config.json");
		setAllowPost(false);
		setUseGetCache(true);
		setCheckInitialized(false);
	}

	@Override
	protected ZStringBuilder buildResponse() {
		return getConfiguration().getBase().toJson().toStringBuilderReadFormat();
	}
}
