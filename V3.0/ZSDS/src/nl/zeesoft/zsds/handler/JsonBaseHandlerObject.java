package nl.zeesoft.zsds.handler;

import java.io.BufferedReader;
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
		setDefaultHeadersAndStatus(response);
		PrintWriter out;
		try {
			out = response.getWriter();
			if (getConfiguration().isInitialized()) {
				out.println(buildResponse());
			} else {
				out.println(setErrorResponse(response,503,getConfiguration().getBaseConfig().getName() + " is waking up. Please wait."));
			}
		} catch (IOException e) {
			getConfiguration().getMessenger().error(this,"I/O exception",e);
		}
	}

	@Override
	public void setDefaultHeadersAndStatus(HttpServletResponse response) {
		super.setDefaultHeadersAndStatus(response);
		response.setContentType("application/json");
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		super.doPost(request, response);
		response.setContentType("application/json");
		PrintWriter out;
		ZStringBuilder js = new ZStringBuilder();
		
		String line = null;
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				js.append(line);
			}
		} catch (IOException e) {
			getConfiguration().getMessenger().error(this,"I/O exception while reading JSON POST request",e);
		}
		
		JsFile json = new JsFile();
		try {
			json.fromStringBuilder(js);
		} catch(Exception e) {
			getConfiguration().getMessenger().error(this,"Exception while parsing JSON POST request",e);
		}
		
		try {
			out = response.getWriter();
			if (getConfiguration().isInitialized()) {
				if (json.rootElement!=null) {
					out.println(buildPostResponse(json));
				} else {
					out.println(setErrorResponse(response,400,"Invalid request"));
				}
			} else {
				out.println(setErrorResponse(response,503,getConfiguration().getBaseConfig().getName() + " is waking up. Please wait."));
			}
		} catch (IOException e) {
			getConfiguration().getMessenger().error(this,"I/O exception",e);
		}
	}

	@Override
	protected ZStringBuilder buildResponse() {
		return new ZStringBuilder();
	}
	
	protected ZStringBuilder buildPostResponse(JsFile json) {
		ZStringBuilder r = new ZStringBuilder();
		return r;
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
