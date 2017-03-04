package nl.zeesoft.zjmo.orchestra;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zjmo.orchestra.members.Conductor;

public class ProtocolControlConductor extends ProtocolControl {
	public static final String GET_ORCHESTRA_STATE	= "GET_ORCHESTRA_STATE";
	public static final String GET_MEMBER_STATE		= "GET_MEMBER_STATE";
	public static final String UPDATE_MEMBER_STATE	= "UPDATE_MEMBER_STATE";
	public static final String TAKE_MEMBER_OFFLINE	= "TAKE_MEMBER_OFFLINE";
	public static final String DRAIN_MEMBER_OFFLINE	= "DRAIN_MEMBER_OFFLINE";
	public static final String BRING_MEMBER_ONLINE	= "BRING_MEMBER_ONLINE";
	
	protected ZStringBuilder handleInput(MemberObject member,ZStringBuilder input) {
		ZStringBuilder output = null;
		if (isCommandJson(input)) {
			String command = getCommandFromJson(input);
			boolean handled = false;
			if (command.equals(GET_ORCHESTRA_STATE) ||
				command.equals(GET_MEMBER_STATE) ||
				command.equals(UPDATE_MEMBER_STATE) ||
				command.equals(TAKE_MEMBER_OFFLINE) ||
				command.equals(DRAIN_MEMBER_OFFLINE) ||
				command.equals(BRING_MEMBER_ONLINE)
				) {
				System.out.println(this + ": Handle command: " + command);
				handled = true;
				output = handleMemberCommand(member,input,command);
			}
			if (!handled) {
				output = super.handleInput(member,input);
			}
		}
		return output;
	}
	
	private ZStringBuilder handleMemberCommand(MemberObject member,ZStringBuilder input,String command) {
		ZStringBuilder output = null;
		if (member instanceof Conductor) {
			String memberId = getCommandParameterFromJson(input,"id");
			if (!command.equals(GET_ORCHESTRA_STATE)) {
				if (memberId.length()==0) {
					output = getErrorJson("Command requires member 'id' parameter");
				} else if (member.getOrchestra().getMemberById(memberId)==null) {
					output = getErrorJson("Orchestra does not contain member with id: " + memberId);
				}
			}
			if (output==null) {
				Conductor con = (Conductor) member;
				if (command.equals(GET_ORCHESTRA_STATE)) {
					output = con.getOrchestraState().toStringBuilder();
				} else if (command.equals(GET_MEMBER_STATE)) {
					output = con.getMemberState(memberId).toStringBuilder();
				} else if (command.equals(UPDATE_MEMBER_STATE)) {
					con.updateState(memberId);
					output = new ZStringBuilder();
				} else if (command.equals(TAKE_MEMBER_OFFLINE)) {
					output = con.takeOffline(memberId);
				} else if (command.equals(DRAIN_MEMBER_OFFLINE)) {
					output = con.drainOffline(memberId);
				} else if (command.equals(BRING_MEMBER_ONLINE)) {
					output = con.bringOnline(memberId);
				}
			}
		}
		return output;
	}
}
