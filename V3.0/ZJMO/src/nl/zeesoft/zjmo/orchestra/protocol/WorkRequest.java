package nl.zeesoft.zjmo.orchestra.protocol;

import nl.zeesoft.zjmo.json.JsElem;
import nl.zeesoft.zjmo.json.JsFile;

public class WorkRequest {
	private String	positionName	= "";
	private String	error			= "";
	private JsFile	request			= null;
	private JsFile	response		= null;
	
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
			f.rootElement.children.add(new JsElem("error","error",true));
		}
		if (request!=null) {
			JsElem rElem = new JsElem("request");
			f.rootElement.children.add(rElem);
			rElem.children.add(request.rootElement);
		}
		if (response!=null) {
			JsElem rElem = new JsElem("response");
			f.rootElement.children.add(rElem);
			rElem.children.add(response.rootElement);
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
				if (elem.name.equals("positionName")) {
					positionName = elem.value.toString();
				} else if (elem.name.equals("error")) {
					error = elem.value.toString();
				} else if (elem.name.equals("request")) {
					JsFile r = new JsFile();
					r.rootElement = elem;
					request = r;
				} else if (elem.name.equals("response")) {
					JsFile r = new JsFile();
					r.rootElement = elem;
					response = r;
				}
			}
		}
	}
}
