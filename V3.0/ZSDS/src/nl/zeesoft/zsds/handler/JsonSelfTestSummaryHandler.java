package nl.zeesoft.zsds.handler;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zsd.interpret.SequenceInterpreterTester;
import nl.zeesoft.zsds.AppConfiguration;

public class JsonSelfTestSummaryHandler extends JsonBaseHandlerObject {
	public static final String	PATH	= "/selfTestSummary.json";
	
	public JsonSelfTestSummaryHandler(AppConfiguration config) {
		super(config,PATH);
		setAllowPost(false);
	}

	public JsonSelfTestSummaryHandler(AppConfiguration config, String path) {
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
