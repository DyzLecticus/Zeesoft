package nl.zeesoft.zjmo.orchestra;

import java.util.Map.Entry;
import java.util.SortedMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zjmo.json.JsFile;

public class Protocol {
	public static final String STOP_PROGRAM		= "STOP_PROGRAM";
	public static final String CLOSE_SESSION 	= "CLOSE_SESSION";
	public static final String GET_STATE 		= "GET_STATE";
	public static final String FORCE_OFFLINE	= "FORCE_OFFLINE";
	public static final String DRAIN_OFFLINE	= "DRAIN_OFFLINE";
	public static final String FORCE_ONLINE		= "FORCE_ONLINE";
	
	private boolean stop	= false;
	private boolean close	= false;
	
	protected ZStringBuilder handleInput(MemberObject member,ZStringBuilder input) {
		ZStringBuilder output = null;
		if (isCommandJson(input)) {
			String command = getCommandFromJson(input);
			if (command.equals(STOP_PROGRAM)) {
				if (member.goToStateIfState(MemberState.STOPPING,MemberState.ONLINE,MemberState.OFFLINE)) {
					stop = true;
				}
			} else if (command.equals(CLOSE_SESSION)) {
				close = true;
			} else if (command.equals(GET_STATE)) {
				output = member.getStateJson();
			} else if (command.equals(FORCE_OFFLINE)) {
				if (member.goToStateIfState(MemberState.GOING_OFFLINE,MemberState.ONLINE)) {
					// TODO: Implement force offline
				}
			} else if (command.equals(DRAIN_OFFLINE)) {
				if (member.goToStateIfState(MemberState.DRAINING_OFFLINE,MemberState.ONLINE)) {
					// TODO: Implement drain offline
				}
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
		String r = "";
		JsFile f = new JsFile();
		f.fromStringBuilder(json);
		if (f.rootElement.children.size()>0) {
			r = f.rootElement.children.get(0).value.toString();
		}
		return r;
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
}
