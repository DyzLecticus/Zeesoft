package nl.zeesoft.zsds.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zsds.AppConfiguration;
import nl.zeesoft.zsds.util.AppTester;
import nl.zeesoft.zsds.util.TestCaseSetTester;

public class JsonAppTesterHandler extends HandlerObject {
	public static final String	PATH	= "/appTester.json";
	
	private TestCaseSetTester	tester	= null;
	
	public JsonAppTesterHandler(AppConfiguration config) {
		super(config,PATH);
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		setDefaultHeadersAndStatus(response);
		PrintWriter out;
		try {
			out = response.getWriter();
			ZStringBuilder err = checkRequest(request,response);
			if (err.length()>0) {
				out.println(err.toString());
			} else {
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
		// TODO: Finish API
	}

	@Override
	public void setDefaultHeadersAndStatus(HttpServletResponse response) {
		super.setDefaultHeadersAndStatus(response);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
	}

	protected ZStringBuilder checkRequest(HttpServletRequest request, HttpServletResponse response) {
		ZStringBuilder err = new ZStringBuilder();
		AppTester appTester = getConfiguration().getAppTester();
		String environmentName = request.getParameter("environment");
		if (environmentName==null || environmentName.length()==0) {
			err = setErrorResponse(response,400,"Missing environment parameter");
		} else if (appTester.getConfiguration().getEnvironment(environmentName)==null) {
			err = setErrorResponse(response,400,"Environment not found: " + environmentName);
		} else if (!appTester.isInitialized()) {
			err = setErrorResponse(response,503,"Application tester is initializing. Please wait.");
		} else {
			tester = appTester.getTester(environmentName);
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
