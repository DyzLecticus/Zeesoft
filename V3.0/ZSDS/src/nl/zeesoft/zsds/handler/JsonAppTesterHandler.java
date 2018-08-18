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
import nl.zeesoft.zsds.tester.AppTester;
import nl.zeesoft.zsds.tester.TestCaseSetTester;

public class JsonAppTesterHandler extends HandlerObject {
	public static final String	PATH	= "/appTester.json";
	
	public JsonAppTesterHandler(AppConfiguration config) {
		super(config,PATH);
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		setDefaultHeadersAndStatus(response);
		PrintWriter out;
		try {
			out = response.getWriter();
			String environmentName = request.getParameter("environment");
			ZStringBuilder err = checkRequest(environmentName,response);
			if (err.length()>0) {
				out.println(err.toString());
			} else {
				TestCaseSetTester tester = getTester(environmentName);
				JsFile summary = tester.getSummary();
				if (summary==null) {
					if (tester.isTesting()) {
						out.println(getResponse(200,"The summary for this environment is not yet available. Please wait"));
					} else {
						out.println(getResponse(200,"The summary for this environment is not available"));
					}
				} else {
					out.println(summary.toStringBuilderReadFormat());
				}
			}
		} catch (IOException e) {
			getConfiguration().getMessenger().error(this,"I/O exception",e);
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		setDefaultHeadersAndStatus(response);
		PrintWriter out;
		
		ZStringBuilder js = new ZStringBuilder();
		JsFile json = new JsFile();
		String line = null;
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				js.append(line);
			}
		} catch (IOException e) {
			getConfiguration().getMessenger().error(this,"I/O exception while reading JSON POST request",e);
		}
		if (js.length()>0) {
			try {
				json.fromStringBuilder(js);
			} catch(Exception e) {
				getConfiguration().getMessenger().error(this,"Exception while parsing JSON POST request",e);
			}
		}

		ZStringBuilder err = new ZStringBuilder();
		String action = "";
		String environmentName = "";
		if (json.rootElement!=null) {
			action = json.rootElement.getChildString("action");
			environmentName = json.rootElement.getChildString("environmentName");
		}
		if (action.length()==0) {
			err = setErrorResponse(response,400,"Required action element not found");
		} else if (!action.equals("test") && !action.equals("reload")) {
			err = setErrorResponse(response,400,"Action not supported: " + action);
		} else if (action.equals("test")){
			err = checkRequest(environmentName,response);
		}
		
		try {
			out = response.getWriter();
			if (err.length()>0) {
				out.println(err.toString());
			} else {
				if (action.equals("test")) {
					TestCaseSetTester tester = getTester(environmentName);
					if (tester.isTesting()) {
						out.println(setErrorResponse(response,503,"Environment is already being tested"));
					} else if (!tester.start()) {
						out.println(setErrorResponse(response,500,"Failed to start tester"));
					} else {
						out.println(getResponse(200,"Started testing environment"));
					}
				} else if (action.equals("reload")) {
					if (!getConfiguration().getAppTester().reinitialize()) {
						out.println(setErrorResponse(response,503,"Failed to reload environment test cases"));
					} else {
						out.println(getResponse(200,"Reloading environment test cases"));
					}
				}
			}
		} catch (IOException e) {
			getConfiguration().getMessenger().error(this,"I/O exception",e);
		}
	}

	@Override
	public void setDefaultHeadersAndStatus(HttpServletResponse response) {
		response.setStatus(200);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");
		response.setHeader("Pragma", "no-cache");
	}
	
	protected TestCaseSetTester getTester(String environmentName) {
		TestCaseSetTester r = null;
		AppTester appTester = getConfiguration().getAppTester();
		if (appTester.isInitialized()) {
			r = appTester.getTester(environmentName);
		}
		return r;
	}
		
	protected ZStringBuilder checkRequest(String environmentName, HttpServletResponse response) {
		ZStringBuilder err = new ZStringBuilder();
		AppTester appTester = getConfiguration().getAppTester();
		if (environmentName==null || environmentName.length()==0) {
			err = setErrorResponse(response,400,"Missing environment parameter");
		} else if (appTester.getConfiguration().getEnvironment(environmentName)==null) {
			err = setErrorResponse(response,400,"Environment not found: " + environmentName);
		} else if (!appTester.isInitialized()) {
			err = setErrorResponse(response,503,"Application tester is initializing. Please wait.");
		}
		return err;
	}
	
	protected ZStringBuilder setErrorResponse(HttpServletResponse response, int status,String error) {
		response.setStatus(status);
		return getErrorResponse("" + status,error);
	}

	protected ZStringBuilder getErrorResponse(String code,String error) {
		return getTypeResponse(code,"error",error);
	}

	protected ZStringBuilder getResponse(int status,String message) {
		return getTypeResponse("" + status,"response",message);
	}
	
	protected ZStringBuilder getTypeResponse(String code,String type,String message) {
		ZStringBuilder r = null;
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("code",code,true));
		json.rootElement.children.add(new JsElem(type,message,true));
		if (getConfiguration().isDebug()) {
			r = json.toStringBuilderReadFormat();
		} else {
			r = json.toStringBuilder();
		}
		return r;
	}

	@Override
	protected ZStringBuilder buildResponse() {
		return null;
	}
}
