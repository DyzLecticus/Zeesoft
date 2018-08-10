package nl.zeesoft.zsds.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zsd.interpret.SequenceInterpreterTester;
import nl.zeesoft.zsds.AppConfiguration;

public class JsonReloadHandler extends JsonBaseHandlerObject {
	public JsonReloadHandler(AppConfiguration config) {
		super(config,"/reload.json");
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		setDefaultHeadersAndStatus(response);
		PrintWriter out;
		try {
			out = response.getWriter();
			out.println(setErrorResponse(response,405,"GET is not supported for this resource"));
		} catch (IOException e) {
			getConfiguration().getMessenger().error(this,"I/O exception",e);
		}
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
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
					if (getConfiguration().reload()) {
						out.println("{\"response\": \"" + getConfiguration().getBaseConfig().getName() + " is reloading.\"}");
					} else {
						out.println(setErrorResponse(response,503,getConfiguration().getBaseConfig().getName() + " is already reloading."));
					}
				}
			} else {
				out.println(setErrorResponse(response,503,getConfiguration().getBaseConfig().getName() + " is waking up. Please wait."));
			}
		} catch (IOException e) {
			getConfiguration().getMessenger().error(this,"I/O exception",e);
		}
	}
}
