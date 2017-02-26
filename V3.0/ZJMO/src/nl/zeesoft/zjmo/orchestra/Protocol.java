package nl.zeesoft.zjmo.orchestra;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zjmo.json.JsFile;

public class Protocol {
	public static final String STOP_PROGRAM		= "STOP_PROGRAM";
	public static final String CLOSE_SESSION 	= "CLOSE_SESSION";
	public static final String GET_STATE 		= "GET_STATE";
	public static final String UPDATE_SETTINGS	= "UPDATE_SETTINGS";
	
	private boolean stop	= false;
	private boolean close	= false;
	
	protected ZStringBuilder handleInput(MemberObject member,ZStringBuilder input) {
		ZStringBuilder output = null;
		if (isCommandJson(input)) {
			String command = getCommandFromJson(input);
			if (command.equals(STOP_PROGRAM)) {
				stop = true;
			} else if (command.equals(CLOSE_SESSION)) {
				close = true;
			} else if (command.equals(GET_STATE)) {
				output = member.getStateJson();
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

	protected boolean isClose() {
		return close;
	}

	protected void setClose(boolean close) {
		this.close = close;
	}
	
	protected ZStringBuilder getCommandJson(String command) {
		ZStringBuilder r = new ZStringBuilder();
		r.append("{\"command\":\"");
		r.append(command);
		r.append("\"}");
		return r;
	}
	
	protected boolean isCommandJson(ZStringBuilder json) {
		return json.startsWith("{\"command\":\"");
	}

	protected String getCommandFromJson(ZStringBuilder json) {
		String r = "";
		JsFile f = new JsFile();
		f.fromStringBuilder(json);
		if (f.rootElement.children.size()>0) {
			r = f.rootElement.children.get(0).value.toString();
		}
		return r;
	}
	
}
