package nl.zeesoft.zjmo.orchestra;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zjmo.orchestra.members.Conductor;

public class ProtocolControl extends ProtocolObject {
	public static final String STOP_PROGRAM		= "STOP_PROGRAM";
	public static final String GET_STATE 		= "GET_STATE";
	public static final String UPDATE_STATE		= "UPDATE_STATE";
	public static final String TAKE_OFFLINE		= "TAKE_OFFLINE";
	public static final String DRAIN_OFFLINE	= "DRAIN_OFFLINE";
	public static final String BRING_ONLINE		= "BRING_ONLINE";
	
	protected ZStringBuilder handleInput(MemberObject member,ZStringBuilder input) {
		ZStringBuilder output = null;
		if (isCommandJson(input)) {
			String command = getCommandFromJson(input);
			if (command.equals(STOP_PROGRAM)) {
				if (member.goToStateIfState(MemberState.STOPPING,MemberState.ONLINE,MemberState.OFFLINE)) {
					setStop(true);
				} else {
					output = getErrorJson("Failed to execute command");
				}
			} else if (command.equals(CLOSE_SESSION)) {
				setClose(true);
				// TODO: Update state?
			} else if (command.equals(GET_STATE)) {
				output = member.getStateJson();
			} else if (command.equals(UPDATE_STATE)) {
				if (member instanceof Conductor) {
					String memberId = getCommandParameterFromJson(input,"id");
					Conductor con = (Conductor) member;
					con.updateState(memberId);
					output = new ZStringBuilder();
				}
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
