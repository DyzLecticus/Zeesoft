package nl.zeesoft.zjmo.orchestra.protocol;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zjmo.orchestra.MemberObject;
import nl.zeesoft.zjmo.orchestra.MemberState;
import nl.zeesoft.zjmo.orchestra.ProtocolObject;

public class ProtocolControl extends ProtocolObject {
	public static final String STOP_PROGRAM		= "STOP_PROGRAM";
	public static final String RESTART_PROGRAM	= "RESTART_PROGRAM";
	public static final String GET_STATE 		= "GET_STATE";
	public static final String TAKE_OFFLINE		= "TAKE_OFFLINE";
	public static final String DRAIN_OFFLINE	= "DRAIN_OFFLINE";
	public static final String BRING_ONLINE		= "BRING_ONLINE";
	
	@Override
	protected ZStringBuilder handleInput(MemberObject member,ZStringBuilder input) {
		ZStringBuilder output = super.handleInput(member, input);
		if (output==null && isCommandJson(input)) {
			String command = getCommandFromJson(input);
			//System.out.println(this + ": Handle command: " + command);
			if (command.equals(STOP_PROGRAM)) {
				if (member.goToStateIfState(MemberState.STOPPING,MemberState.ONLINE,MemberState.OFFLINE)) {
					setStop(true);
					output = getExecutedCommandResponse();
				} else {
					output = getFailedToExecuteCommandResponse();
				}
			} else if (command.equals(RESTART_PROGRAM)) {
				if (member.goToStateIfState(MemberState.RESTARTING,MemberState.ONLINE,MemberState.OFFLINE)) {
					setRestart(true);
					output = getExecutedCommandResponse();
				} else {
					output = getFailedToExecuteCommandResponse();
				}
			} else if (command.equals(GET_STATE)) {
				output = member.getStateJson();
			} else if (command.equals(TAKE_OFFLINE)) {
				if (!member.takeOffLine()) {
					output = getFailedToExecuteCommandResponse();
				} else {
					output = getExecutedCommandResponse();
				}
			} else if (command.equals(DRAIN_OFFLINE)) {
				if (!member.drainOffLine()) {
					output = getFailedToExecuteCommandResponse();
				} else {
					output = getExecutedCommandResponse();
				}
			} else if (command.equals(BRING_ONLINE)) {
				if (!member.bringOnLine()) {
					output = getFailedToExecuteCommandResponse();
				} else {
					output = getExecutedCommandResponse();
				}
			}
		}
		return output;
	}
}
