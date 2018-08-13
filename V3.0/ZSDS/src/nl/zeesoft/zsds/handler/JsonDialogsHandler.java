package nl.zeesoft.zsds.handler;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zsd.util.DialogSetToJson;
import nl.zeesoft.zsds.AppConfiguration;

public class JsonDialogsHandler extends JsonBaseHandlerObject {
	public JsonDialogsHandler(AppConfiguration config) {
		super(config,"/dialogs.json");
		setAllowPost(false);
	}
	
	@Override
	protected ZStringBuilder buildResponse() {
		DialogSetToJson convertor = new DialogSetToJson();
		return convertor.toJson(getConfiguration().getDialogHandlerConfig().getDialogSet(),"").toStringBuilderReadFormat();
	}
}
