package nl.zeesoft.zjmo.orchestra;

import nl.zeesoft.zdk.ZStringBuilder;

public class ProtocolControl extends ProtocolObject {
	public static final String STOP_PROGRAM		= "STOP_PROGRAM";
	public static final String GET_STATE 		= "GET_STATE";
	public static final String TAKE_OFFLINE		= "TAKE_OFFLINE";
	public static final String DRAIN_OFFLINE	= "DRAIN_OFFLINE";
	public static final String BRING_ONLINE		= "BRING_ONLINE";
	
	protected ZStringBuilder handleInput(MemberObject member,ZStringBuilder input) {
		ZStringBuilder output = null;
		if (isCommandJson(input)) {
			String command = getCommandFromJson(input);
			System.out.println(this + ": Handle command: " + command);
			if (command.equals(STOP_PROGRAM)) {
				if (member.goToStateIfState(MemberState.STOPPING,MemberState.ONLINE,MemberState.OFFLINE)) {
					setStop(true);
				} else {
					output = getErrorJson("Failed to execute command");
				}
			} else if (command.equals(CLOSE_SESSION)) {
				setClose(true);
			} else if (command.equals(GET_STATE)) {
				output = member.getStateJson();
			} else if (command.equals(TAKE_OFFLINE)) {
				if (!member.takeOffLine()) {
					output = getErrorJson("Failed to execute command");
				} else {
					output = new ZStringBuilder();
				}
			} else if (command.equals(DRAIN_OFFLINE)) {
				if (!member.drainOffLine()) {
					output = getErrorJson("Failed to execute command");
				} else {
					output = new ZStringBuilder();
				}
			} else if (command.equals(BRING_ONLINE)) {
				if (!member.bringOnLine()) {
					output = getErrorJson("Failed to execute command");
				} else {
					output = new ZStringBuilder();
				}
			}
		}
		return output;
	}
}
