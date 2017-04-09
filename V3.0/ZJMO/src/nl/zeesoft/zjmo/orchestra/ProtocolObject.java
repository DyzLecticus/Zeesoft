package nl.zeesoft.zjmo.orchestra;

import java.util.Map.Entry;
import java.util.SortedMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;

/**
 * Abstract protocol object.
 */
public abstract class ProtocolObject {
	public static final String CLOSE_SESSION 	= "CLOSE_SESSION";
	
	private boolean stop	= false;
	private boolean restart	= false;
	private boolean close	= false;
	
	protected ZStringBuilder handleInput(MemberObject member,ZStringBuilder input) {
		ZStringBuilder output = null;
		if (isCommandJson(input)) {
			String command = getCommandFromJson(input);
			//System.out.println(this + ": Handle command: " + command);
			if (command.equals(CLOSE_SESSION)) {
				setClose(true);
				output = input;
			}
		}
		return output;
	}

	protected boolean isStop() {
		return stop;
	}

	protected void setStop(boolean stop) {
		this.stop = stop;
	}

	protected boolean isRestart() {
		return restart;
	}

	protected void setRestart(boolean restart) {
		this.restart = restart;
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
		return json!=null && json.startsWith("{\"command\":");
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
	
	public static ZStringBuilder getErrorJson(String error) {
		ZStringBuilder r = new ZStringBuilder();
		r.append("{\"error\":\"");
		r.append(error);
		r.append("\"}");
		return r;
	}

	public static boolean isErrorJson(ZStringBuilder json) {
		return json!=null && json.startsWith("{\"error\":");
	}

	public static String getErrorFromJson(ZStringBuilder json) {
		return getFirstElementValueFromJson(json);
	}

	public static ZStringBuilder getResponseJson(String response) {
		ZStringBuilder r = new ZStringBuilder();
		r.append("{\"response\":\"");
		r.append(response);
		r.append("\"}");
		return r;
	}

	public static boolean isResponseJson(ZStringBuilder json) {
		return json!=null && json.startsWith("{\"response\":");
	}

	public static String getResponseFromJson(ZStringBuilder json) {
		return getFirstElementValueFromJson(json);
	}

	public static String getFirstElementValueFromJson(ZStringBuilder json) {
		String r = "";
		JsFile f = new JsFile();
		f.fromStringBuilder(json);
		if (f.rootElement.children.size()>0) {
			r = f.rootElement.children.get(0).value.toString();
		}
		return r;
	}

	public static ZStringBuilder getExecutedCommandResponse() {
		return getResponseJson("Executed command");
	}

	public static ZStringBuilder getFailedToExecuteCommandResponse() {
		return getErrorJson("Failed to execute command");
	}
}
