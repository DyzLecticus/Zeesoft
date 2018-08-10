package nl.zeesoft.zsds.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zsd.interpret.SequenceInterpreterTester;
import nl.zeesoft.zsds.AppConfiguration;

public class JsonSelfTestHandler extends JsonBaseHandlerObject {
	public JsonSelfTestHandler(AppConfiguration config) {
		super(config,"/selfTest.json");
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		setDefaultHeadersAndStatus(response);
		PrintWriter out;
		try {
			out = response.getWriter();
			if (getConfiguration().isInitialized()) {
				SequenceInterpreterTester tester = getConfiguration().getTester();
				if (tester==null || tester.isTesting()) {
					String percentage = "";
					if (tester!=null) {
						percentage = " (" + tester.getDonePercentage() + "%)";
					}
					out.println(setErrorResponse(response,503,getConfiguration().getBaseConfig().getName() + " is testing itself" + percentage + ". Please wait."));
				} else {
					out.println(tester.getSummary().toStringBuilderReadFormat());
				}
			} else {
				out.println(setErrorResponse(response,503,getConfiguration().getBaseConfig().getName() + " is waking up. Please wait."));
			}
		} catch (IOException e) {
			getConfiguration().getMessenger().error(this,"I/O exception",e);
		}
	}
}
