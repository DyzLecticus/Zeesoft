package nl.zeesoft.zodb.mod.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;

public abstract class HandlerObject {
	protected static final String	METH_GET		= "GET";
	protected static final String	METH_POST		= "POST";
	
	private	Config					configuration	= null;
	private	ModObject				module			= null;
	private String					path			= "";
	private String					contentType		= "text/html";
	
	private boolean					allowGet		= true;
	private boolean					allowPost		= false;
	
	public HandlerObject(Config config,ModObject mod,String path) {
		this.configuration = config;
		this.module = mod;
		this.path = path;
		if (path.endsWith(".js")) {
			contentType = "application/javascript";
		} else if (path.endsWith(".css")) {
			contentType = "text/css";
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		setDefaultHeaders(response);
		try {
			PrintWriter out = response.getWriter();
			if (allowGet) {
				out.println(handleAllowedRequest(METH_GET,request,response));
			} else {
				out.println(setResponse(response,405,"Method Not Allowed"));
			}
		} catch (IOException e) {
			getConfiguration().error(this,"I/O exception",e);
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		setDefaultHeaders(response);
		try {
			PrintWriter out = response.getWriter();
			if (allowPost) {
				out.println(handleAllowedRequest(METH_POST,request,response));
			} else {
				out.println(setResponse(response,405,"Method Not Allowed"));
			}
		} catch (IOException e) {
			getConfiguration().error(this,"I/O exception",e);
		}
	}

	public String getPath() {
		return path;
	}

	protected ZStringBuilder handleAllowedRequest(String method, HttpServletRequest request, HttpServletResponse response) {
		return setResponse(response,200,"OK");
	}
	
	protected void setDefaultHeaders(HttpServletResponse response) {
		response.setContentType(contentType);
	}

	protected ZStringBuilder setResponse(HttpServletResponse response,int code,String message) {
		ZStringBuilder body = null;
		response.setStatus(code);
		if (path.endsWith(".json")) {
			String elemName = "response";
			if (code!=200) {
				elemName = "error";
			}
			JsFile json = new JsFile();
			json.rootElement = new JsElem();
			json.rootElement.children.add(new JsElem("code","" + code));
			json.rootElement.children.add(new JsElem(elemName,message,true));
			body = json.toStringBuilderReadFormat();
		} else {
			body = new ZStringBuilder();
			body.append(message);
		}
		return body;
	}

	protected Config getConfiguration() {
		return configuration;
	}

	protected ModObject getModule() {
		return module;
	}
	
	protected void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	protected void setAllowGet(boolean allowGet) {
		this.allowGet = allowGet;
	}
	
	protected void setAllowPost(boolean allowPost) {
		this.allowPost = allowPost;
	}
}
