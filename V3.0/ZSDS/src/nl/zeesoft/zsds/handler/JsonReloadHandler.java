package nl.zeesoft.zsds.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
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
			ZStringBuilder err = checkInitialized(response);
			if (err.length()==0) {
				err = checkTesting(response);
			}
			if (err.length()==0) {
				if (getConfiguration().reload()) {
					out.println(getResponse(200,getConfiguration().getBaseConfig().getName() + " is refreshing its memory."));
				} else {
					out.println(setErrorResponse(response,503,getConfiguration().getBaseConfig().getName() + " is already refreshing its memory. Please wait."));
				}
			} else {
				out.println(err);
			}
		} catch (IOException e) {
			getConfiguration().getMessenger().error(this,"I/O exception",e);
		}
	}
}