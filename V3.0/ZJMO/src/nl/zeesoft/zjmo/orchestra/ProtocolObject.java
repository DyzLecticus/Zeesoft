package nl.zeesoft.zjmo.orchestra;

import java.util.Map.Entry;
import java.util.SortedMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zjmo.json.JsFile;

public abstract class ProtocolObject {
	public static final String CLOSE_SESSION 	= "CLOSE_SESSION";
	
	private boolean stop	= false;
	private boolean close	= false;
	
	protected abstract ZStringBuilder handleInput(MemberObject member,ZStringBuilder input);

	protected boolean isStop() {
		return stop;
	}

	protected void setStop(boolean stop) {
		this.stop = stop;
	}

	protected boolean isClose() {
		return close;
	}

	protected void setClose(boolean close) {
		this.close = close;
	}

	protected ZStringBuilder getCommandJson(String command,SortedMap<String,String> parameters) {
		ZStringBuilder r = new ZStringBuilder();
		r.append("{\"command\":\"");
		r.append(command);
		r.append("\"");
		if (parameters!=null) {
			for (Entry<String,String> entry: parameters.entrySet()) {
				r.append(",\"");
				r.append(entry.getKey());
				r.append("\":\"");
				r.append(entry.getValue());
				r.append("\"");
			}
		}
		r.append("}");
		return r;
	}

	protected boolean isCommandJson(ZStringBuilder json) {
		return json.startsWith("{\"command\":\"");
	}

	protected String getCommandFromJson(ZStringBuilder json) {
		return getFirstElementValueFromJson(json);
	}
	
	protected String getCommandParameterFromJson(ZStringBuilder json,String name) {
		String r = "";
		JsFile f = new JsFile();
		f.fromStringBuilder(json);
		if (f.rootElement.children.size()>1 && f.rootElement.getChildByName(name)!=null) {
			r = f.rootElement.getChildByName(name).value.toString();
		}
		return r;
	}
	
	protected ZStringBuilder getErrorJson(String error) {
		ZStringBuilder r = new ZStringBuilder();
		r.append("{\"error\":\"");
		r.append(error);
		r.append("\"}");
		return r;
	}

	protected boolean isErrorJson(ZStringBuilder json) {
		return json.startsWith("{\"error\":\"");
	}

	protected String getErrorFromJson(ZStringBuilder json) {
		return getFirstElementValueFromJson(json);
	}
	
	private String getFirstElementValueFromJson(ZStringBuilder json) {
		String r = "";
		JsFile f = new JsFile();
		f.fromStringBuilder(json);
		if (f.rootElement.children.size()>0) {
			r = f.rootElement.children.get(0).value.toString();
		}
		return r;
	}
	
}
