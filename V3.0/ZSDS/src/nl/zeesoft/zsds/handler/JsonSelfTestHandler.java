package nl.zeesoft.zsds.handler;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zsd.interpret.SequenceInterpreterTester;
import nl.zeesoft.zsds.AppConfiguration;

public class JsonSelfTestHandler extends JsonBaseHandlerObject {
	public JsonSelfTestHandler(AppConfiguration config) {
		super(config,"/selfTestSummary.json");
		setAllowPost(false);
	}

	public JsonSelfTestHandler(AppConfiguration config, String path) {
		super(config,path);
		setAllowPost(false);
	}

	@Override
	protected ZStringBuilder buildResponse() {
		ZStringBuilder r = new ZStringBuilder();
		SequenceInterpreterTester tester = getConfiguration().getTester();
		if (tester!=null && tester.getSummary()!=null) {
			JsFile summary = tester.getSummary();
			if (summary!=null) {
				r = summary.toStringBuilderReadFormat();
			}
		}
		return r;
	}
}
