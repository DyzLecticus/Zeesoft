package nl.zeesoft.zsds.handler;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zsds.AppConfiguration;

public class JsonTestConfigHandler extends JsonBaseHandlerObject {
	public JsonTestConfigHandler(AppConfiguration config) {
		super(config,"/testConfig.json");
		setAllowPost(false);
		setUseGetCache(true);
		setCheckInitialized(false);
	}

	@Override
	protected ZStringBuilder buildResponse() {
		return getConfiguration().getAppTester().getConfiguration().toJson().toStringBuilderReadFormat();
	}
}
