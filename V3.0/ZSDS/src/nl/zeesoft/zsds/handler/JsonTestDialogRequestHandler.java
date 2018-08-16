package nl.zeesoft.zsds.handler;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zsd.dialog.DialogRequest;
import nl.zeesoft.zsds.AppConfiguration;

public class JsonTestDialogRequestHandler extends JsonBaseHandlerObject {
	public JsonTestDialogRequestHandler(AppConfiguration config) {
		super(config,"/testDialogRequest.json");
		setAllowPost(false);
		setUseGetCache(true);
		setCheckInitialized(false);
	}

	@Override
	protected ZStringBuilder buildResponse() {
		DialogRequest request = new DialogRequest();
		request.setAllActions(true);
		request.appendDebugLog = getConfiguration().isDebug();
		request.randomizeOutput = false;
		return request.toJson().toStringBuilderReadFormat();
	}
}
