package nl.zeesoft.zjmo.orchestra.protocol;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class WorkRequest extends RequestObject {
	private String	positionName	= "";
	
	public WorkRequest() {
		
	}

	public WorkRequest(String positionName,String request) {
		this.positionName = positionName;
		setRequest(request);
	}
	
	public String getPositionName() {
		return positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}
	
	public JsFile toJson() {
		JsFile f = super.toJson();
		f.rootElement.children.add(new JsElem("positionName",positionName,true));
		return f;
	}

	public void fromJson(JsFile json) {
		positionName = "";
		super.fromJson(json);
		if (json.rootElement!=null && json.rootElement.children.size()>0) {
			for (JsElem elem: json.rootElement.children) {
				if (elem.name.equals("positionName") && elem.value!=null) {
					positionName = elem.value.toString();
				}
			}
		}
	}
}
