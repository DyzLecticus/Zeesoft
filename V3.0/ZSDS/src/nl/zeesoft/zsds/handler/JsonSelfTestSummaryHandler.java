package nl.zeesoft.zsds.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zsd.interpret.SequenceInterpreterTester;
import nl.zeesoft.zsds.AppConfiguration;

public class JsonSelfTestSummaryHandler extends JsonBaseHandlerObject {
	public static final String	PATH	= "/selfTestSummary.json";
	
	private boolean overrideGet = true;
	
	public JsonSelfTestSummaryHandler(AppConfiguration config) {
		super(config,PATH);
		setAllowPost(false);
	}

	public JsonSelfTestSummaryHandler(AppConfiguration config, String path) {
		super(config,path);
		setAllowPost(false);
		overrideGet = false;
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		if (overrideGet && !getConfiguration().getBase().isSelfTest()) {
			setDefaultHeadersAndStatus(response);
			PrintWriter out;
			try {
				out = response.getWriter();
				out.println(getResponse(200,getConfiguration().getBase().getName() + " does not support self testing"));
			} catch (IOException e) {
				getConfiguration().getMessenger().error(this,"I/O exception",e);
			}
		} else {
			super.doGet(request,response);
		}
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
		} else if (tester==null && !getConfiguration().getBase().isSelfTest()) {
			r = getResponse(200,getOkResponse());
		}
		return r;
	}
	
	protected String getOkResponse() {
		return getConfiguration().getBase().getName() + " does not support self testing";
	}
}
