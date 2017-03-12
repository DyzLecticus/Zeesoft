package nl.zeesoft.zjmo.orchestra.protocol;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class WorkRequest {
	private String	positionName	= "";
	private String	error			= "";
	private JsFile	request			= null;
	private JsFile	response		= null;
	
	public WorkRequest() {
		
	}

	public WorkRequest(String positionName,String request) {
		this.positionName = positionName;
		setRequest(request);
	}
	
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public JsFile getRequest() {
		return request;
	}

	public void setRequest(JsFile request) {
		this.request = request;
	}

	public void setRequest(String request) {
		request = request.trim();
		if (request.startsWith("{") && request.endsWith("}")) {
			this.request = new JsFile();
			this.request.fromStringBuilder(new ZStringBuilder(request));
		}
	}

	public JsFile getResponse() {
		return response;
	}

	public void setResponse(JsFile response) {
		this.response = response;
	}

	public String getPositionName() {
		return positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}
	
	public JsFile toJson() {
		JsFile f = new JsFile();
		f.rootElement = new JsElem();
		f.rootElement.children.add(new JsElem("positionName",positionName,true));
		if (error.length()>0) {
			f.rootElement.children.add(new JsElem("error",error,true));
		}
		if (request!=null && request.rootElement.children.size()>0) {
			JsElem rElem = new JsElem("request");
			f.rootElement.children.add(rElem);
			for (JsElem cElem: request.rootElement.children) {
				rElem.children.add(cElem);
			}
		}
		if (response!=null && response.rootElement.children.size()>0) {
			JsElem rElem = new JsElem("response");
			f.rootElement.children.add(rElem);
			for (JsElem cElem: request.rootElement.children) {
				rElem.children.add(cElem);
			}
		}
		return f;
	}

	public void fromJson(JsFile json) {
		positionName = "";
		error = "";
		request = null;
		response = null;
		if (json.rootElement!=null && json.rootElement.children.size()>0) {
			for (JsElem elem: json.rootElement.children) {
				if (elem.name.equals("positionName") && elem.value!=null) {
					positionName = elem.value.toString();
				} else if (elem.name.equals("error") && elem.value!=null) {
					error = elem.value.toString();
				} else if (elem.name.equals("request") && elem.children.size()>0) {
					JsFile r = new JsFile();
					r.rootElement = elem;
					request = r;
				} else if (elem.name.equals("response") && elem.children.size()>0) {
					JsFile r = new JsFile();
					r.rootElement = elem;
					response = r;
				}
			}
		}
	}
	
	public void fromStringBuilder(ZStringBuilder str) {
		if (str!=null) {
			JsFile resp = new JsFile();
			resp.fromStringBuilder(str);
			fromJson(resp);
		}
	}
}
