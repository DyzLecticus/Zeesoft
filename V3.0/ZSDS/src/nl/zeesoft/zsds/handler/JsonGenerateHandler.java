package nl.zeesoft.zsds.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zsds.AppConfiguration;

public class JsonGenerateHandler extends JsonBaseHandlerObject {
	public JsonGenerateHandler(AppConfiguration config) {
		super(config,"/generate.json");
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
				err = checkGenerating(response);
			}
			if (err.length()==0) {
				err = checkReloading(response);
			}
			if (err.length()==0) {
				err = checkTesting(response);
			}
			if (err.length()==0) {
				if (getConfiguration().generate()) {
					out.println(getResponse(200,getConfiguration().getBaseConfig().getName() + " is regenerating its memory."));
				} else {
					out.println(setErrorResponse(response,503,getConfiguration().getBaseConfig().getName() + " is already regenerating its memory. Please wait."));
				}
			} else {
				out.println(err);
			}
		} catch (IOException e) {
			getConfiguration().getMessenger().error(this,"I/O exception",e);
		}
	}
}
