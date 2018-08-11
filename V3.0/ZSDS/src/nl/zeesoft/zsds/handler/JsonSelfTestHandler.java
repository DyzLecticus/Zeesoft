package nl.zeesoft.zsds.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zsd.interpret.SequenceInterpreterTester;
import nl.zeesoft.zsds.AppConfiguration;

public class JsonSelfTestHandler extends JsonBaseHandlerObject {
	public JsonSelfTestHandler(AppConfiguration config) {
		super(config,"/selfTestSummary.json");
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		setDefaultHeadersAndStatus(response);
		PrintWriter out;
		try {
			out = response.getWriter();
			ZStringBuilder err = checkInitialized(response);
			if (err.length()==0) {
				SequenceInterpreterTester tester = getConfiguration().getTester();
				out.println(tester.getSummary().toStringBuilderReadFormat());
			} else {
				out.println(err);
			}
		} catch (IOException e) {
			getConfiguration().getMessenger().error(this,"I/O exception",e);
		}
	}
}
