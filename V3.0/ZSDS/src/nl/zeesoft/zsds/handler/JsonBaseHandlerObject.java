package nl.zeesoft.zsds.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zsds.AppConfiguration;

public abstract class JsonBaseHandlerObject extends HandlerObject {
	public JsonBaseHandlerObject(AppConfiguration config,String path) {
		super(config,path);
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json");
		PrintWriter out;
		try {
			out = response.getWriter();
			if (getConfiguration().isInitialized()) {
				out.println(buildResponse());
			} else {
				out.println(setErrorResponse(response,503,"Application is not yet initialized. Please try again in a few minutes."));
			}
		} catch (IOException e) {
			getConfiguration().getMessenger().error(this,"I/O exception",e);
		}
	}

	@Override
	protected ZStringBuilder buildResponse() {
		return getConfiguration().getBaseConfig().toJson().toStringBuilderReadFormat();
	}
	
	protected ZStringBuilder setErrorResponse(HttpServletResponse response, int status,String error) {
		response.setStatus(status);
		return getErrorResponse("" + status,error);
	}
	
	protected ZStringBuilder getErrorResponse(String code,String error) {
		ZStringBuilder r = null;
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("code",code,true));
		json.rootElement.children.add(new JsElem("error",error,true));
		if (getConfiguration().isDebug()) {
			r = json.toStringBuilderReadFormat();
		} else {
			r = json.toStringBuilder();
		}
		return r;
	}
}
