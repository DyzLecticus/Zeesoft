package nl.zeesoft.zsds.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zsd.interpret.SequenceInterpreterTester;
import nl.zeesoft.zsds.AppConfiguration;

public abstract class JsonBaseHandlerObject extends HandlerObject {
	private boolean	allowGet			= true;
	private boolean	allowPost			= true;
	private boolean useGetCache			= false;
	private boolean checkInitialized	= true;
	private boolean checkGenerating		= false;
	private boolean checkReloading		= false;
	private boolean checkTesting		= false;
	private boolean checkJson			= false;
	
	public JsonBaseHandlerObject(AppConfiguration config,String path) {
		super(config,path);
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		setDefaultHeadersAndStatus(response);
		PrintWriter out;
		try {
			out = response.getWriter();
			if (!allowGet) {
				out.println(setErrorResponse(response,405,"GET is not supported for this resource"));
			} else {
				ZStringBuilder err = new ZStringBuilder(); 
				if (err.length()==0 && checkInitialized) {
					err = checkInitialized(response);
				}
				if (err.length()==0 && checkGenerating) {
					err = checkGenerating(response);
				}
				if (err.length()==0 && checkReloading) {
					err = checkReloading(response);
				}
				if (err.length()==0 && checkTesting) {
					err = checkTesting(response);
				}
				if (err.length()==0) {
					ZStringBuilder res = null;
					if (useGetCache) {
						res = getCachedResponse();
					} else {
						res = buildResponse();
					}
					if (res==null) {
						out.println(setErrorResponse(response,503,getConfiguration().getBaseConfig().getName() + " is busy. Please wait."));
					} else {
						out.println(res);
					}
				} else {
					out.println(err);
				}
			}
		} catch (IOException e) {
			getConfiguration().getMessenger().error(this,"I/O exception",e);
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			getConfiguration().getMessenger().error(this,"Unsupported encoding",e);
		}
		setDefaultHeadersAndStatus(response);

		PrintWriter out;
		ZStringBuilder js = new ZStringBuilder();
		JsFile json = new JsFile();
		
		if (allowPost) {
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
		}
		
		try {
			out = response.getWriter();
			if (!allowPost) {
				out.println(setErrorResponse(response,405,"POST is not supported for this resource"));
			} else {
				ZStringBuilder err = new ZStringBuilder(); 
				if (err.length()==0 && checkInitialized) {
					err = checkInitialized(response);
				}
				if (err.length()==0 && checkGenerating) {
					err = checkGenerating(response);
				}
				if (err.length()==0 && checkReloading) {
					err = checkReloading(response);
				}
				if (err.length()==0 && checkTesting) {
					err = checkTesting(response);
				}
				if (err.length()==0 && checkJson && json.rootElement==null) {
					err = setErrorResponse(response,400,"Invalid request");
				}
				if (err.length()>0) {
					out.println(err);
				} else {
					out.println(buildPostResponse(response,json));
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
	
	@Override
	protected ZStringBuilder buildResponse() {
		return new ZStringBuilder();
	}
	
	protected ZStringBuilder buildPostResponse(HttpServletResponse response, JsFile json) {
		return new ZStringBuilder();
	}

	protected ZStringBuilder checkInitialized(HttpServletResponse response) {
		ZStringBuilder err = new ZStringBuilder();
		if (!getConfiguration().isInitialized()) {
			err = setErrorResponse(response,503,getConfiguration().getBaseConfig().getName() + " is waking up. Please wait.");
		}
		return err;
	}

	protected ZStringBuilder checkGenerating(HttpServletResponse response) {
		ZStringBuilder err = new ZStringBuilder();
		if (getConfiguration().isGenerating()) {
			err = setErrorResponse(response,503,getConfiguration().getBaseConfig().getName() + " regenerating its memory. Please wait.");
		}
		return err;
	}

	protected ZStringBuilder checkReloading(HttpServletResponse response) {
		ZStringBuilder err = new ZStringBuilder();
		if (getConfiguration().isReloading()) {
			err = setErrorResponse(response,503,getConfiguration().getBaseConfig().getName() + " refreshing its memory. Please wait.");
		}
		return err;
	}
	
	protected ZStringBuilder checkTesting(HttpServletResponse response) {
		ZStringBuilder err = new ZStringBuilder();
		SequenceInterpreterTester tester = getConfiguration().getTester();
		if (tester==null || getConfiguration().isTesting()) {
			String percentage = "";
			if (tester!=null) {
				percentage = " (" + tester.getDonePercentage() + "%)";
			}
			err = setErrorResponse(response,503,getConfiguration().getBaseConfig().getName() + " is testing itself" + percentage + ". Please wait.");
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

	protected void setAllowGet(boolean allowGet) {
		this.allowGet = allowGet;
	}

	protected void setAllowPost(boolean allowPost) {
		this.allowPost = allowPost;
	}

	public void setUseGetCache(boolean useGetCache) {
		this.useGetCache = useGetCache;
	}

	protected void setCheckInitialized(boolean checkInitialized) {
		this.checkInitialized = checkInitialized;
	}

	protected void setCheckGenerating(boolean checkGenerating) {
		this.checkGenerating = checkGenerating;
	}

	protected void setCheckReloading(boolean checkReloading) {
		this.checkReloading = checkReloading;
	}

	protected void setCheckTesting(boolean checkTesting) {
		this.checkTesting = checkTesting;
	}

	protected void setCheckJson(boolean checkJson) {
		this.checkJson = checkJson;
	}
}
