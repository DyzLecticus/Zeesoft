package nl.zeesoft.zjmo.orchestra.protocol;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class PublishRequest extends RequestObject {
	private String	channelName		= "";

	public PublishRequest() {
		
	}

	public PublishRequest(String channelName,String request) {
		this.channelName = channelName;
		setRequest(request);
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	
	public JsFile toJson() {
		JsFile f = super.toJson();
		f.rootElement.children.add(new JsElem("channelName",channelName,true));
		return f;
	}

	public void fromJson(JsFile json) {
		channelName = "";
		super.fromJson(json);
		if (json.rootElement!=null && json.rootElement.children.size()>0) {
			for (JsElem elem: json.rootElement.children) {
				if (elem.name.equals("channelName") && elem.value!=null) {
					channelName = elem.value.toString();
				}
			}
		}
	}
}
