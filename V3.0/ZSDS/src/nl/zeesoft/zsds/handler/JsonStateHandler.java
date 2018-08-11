package nl.zeesoft.zsds.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zsd.interpret.SequenceInterpreterTester;
import nl.zeesoft.zsds.AppConfiguration;

public class JsonStateHandler extends JsonBaseHandlerObject {
	public JsonStateHandler(AppConfiguration config) {
		super(config,"/state.json");
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		setDefaultHeadersAndStatus(response);
		PrintWriter out;
		try {
			out = response.getWriter();
			ZStringBuilder err = checkInitialized(response);
			if (err.length()==0) {
				err = checkGenerating(response);
			}
			if (err.length()==0) {
				err = checkReloading(response);
			}
			if (err.length()==0) {
				err = checkTesting(response);
			}
			if (err.length()==0) {
				SequenceInterpreterTester tester = getConfiguration().getTester();
				if (tester!=null && tester.getSummary()!=null) {
					out.println(tester.getSummary().toStringBuilderReadFormat());
				}
			} else {
				out.println(err);
			}
		} catch (IOException e) {
			getConfiguration().getMessenger().error(this,"I/O exception",e);
		}
	}
}
