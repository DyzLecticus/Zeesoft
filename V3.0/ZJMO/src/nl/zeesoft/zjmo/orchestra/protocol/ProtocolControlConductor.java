package nl.zeesoft.zjmo.orchestra.protocol;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zjmo.orchestra.MemberObject;
import nl.zeesoft.zjmo.orchestra.OrchestraMember;
import nl.zeesoft.zjmo.orchestra.client.ActiveClient;
import nl.zeesoft.zjmo.orchestra.client.OrchestraConnector;
import nl.zeesoft.zjmo.orchestra.members.Conductor;

public class ProtocolControlConductor extends ProtocolControl {
	public static final String GET_ORCHESTRA_STATE	= "GET_ORCHESTRA_STATE";
	public static final String GET_MEMBER_STATE		= "GET_MEMBER_STATE";
	public static final String CONNECT_MEMBER		= "CONNECT_MEMBER";
	public static final String RESTART_MEMBER		= "RESTART_MEMBER";
	public static final String UPDATE_MEMBER_STATE	= "UPDATE_MEMBER_STATE";
	public static final String TAKE_MEMBER_OFFLINE	= "TAKE_MEMBER_OFFLINE";
	public static final String DRAIN_MEMBER_OFFLINE	= "DRAIN_MEMBER_OFFLINE";
	public static final String BRING_MEMBER_ONLINE	= "BRING_MEMBER_ONLINE";
	public static final String PUBLISH_ORCHESTRA	= "PUBLISH_ORCHESTRA";
	
	@Override
	protected ZStringBuilder handleInput(MemberObject member,ZStringBuilder input) {
		ZStringBuilder output = null;
		if (isCommandJson(input)) {
			String command = getCommandFromJson(input);
			boolean handled = false;
			if (command.equals(GET_ORCHESTRA_STATE) ||
				command.equals(GET_MEMBER_STATE) ||
				command.equals(CONNECT_MEMBER) ||
				command.equals(RESTART_MEMBER) ||
				command.equals(UPDATE_MEMBER_STATE) ||
				command.equals(TAKE_MEMBER_OFFLINE) ||
				command.equals(DRAIN_MEMBER_OFFLINE) ||
				command.equals(BRING_MEMBER_ONLINE)
				) {
				//System.out.println(this + ": Handle command: " + command);
				handled = true;
				output = handleMemberCommand(member,input,command);
			} else if (command.equals(PUBLISH_ORCHESTRA)) {
				JsFile json = getJsonForInput(input);
				json.rootElement.getChildByName("command").value = new ZStringBuilder(ProtocolControl.UPDATE_ORCHESTRA);
				Conductor con = (Conductor) member;
				output = handleControlChannelRequest(con,json.toStringBuilder());
				handled = true;
			}
			if (!handled) {
				output = super.handleInput(member,input);
			}
		}
		return output;
	}
	
	protected ZStringBuilder handleMemberCommand(MemberObject member,ZStringBuilder input,String command) {
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
				} else if (command.equals(CONNECT_MEMBER)) {
					con.updateState(memberId,true);
					output = getExecutedCommandResponse();
				} else if (command.equals(UPDATE_MEMBER_STATE)) {
					con.updateState(memberId,false);
					output = getExecutedCommandResponse();
				} else if (command.equals(TAKE_MEMBER_OFFLINE)) {
					output = con.takeOffline(memberId);
				} else if (command.equals(DRAIN_MEMBER_OFFLINE)) {
					output = con.drainOffline(memberId);
					if (!isErrorJson(output)) {
						con.drainOfflineWorker(memberId);
					}
				} else if (command.equals(BRING_MEMBER_ONLINE)) {
					output = con.bringOnline(memberId);
				} else if (command.equals(RESTART_MEMBER)) {
					output = con.restart(memberId);
				}
			}
		}
		return output;
	}
	
	protected ZStringBuilder handleControlChannelRequest(Conductor con, ZStringBuilder json) {
		ZStringBuilder output = null;
		OrchestraConnector controlChannel = con.getControlChannel();
		if (controlChannel==null) {
			output = getErrorJson("Control channel is busy");
		} else {
			List<ControlChannelWorker> workers = new ArrayList<ControlChannelWorker>();
			for (OrchestraMember member: con.getOrchestra().getMembers()) {
				ActiveClient client = controlChannel.getClient(member.getId());
				if (!client.isOpen()) {
					output = getErrorJson("Channel subscriber is not online: " + member.getId());
					break;
				} else {
					ControlChannelWorker worker = new ControlChannelWorker(con.getMessenger(),con.getUnion(),json,client);
					workers.add(worker);
				}
			}
			if (output==null) {
				for (ControlChannelWorker worker: workers) {
					worker.start();
				}
				for (ControlChannelWorker worker: workers) {
					int i = 0;
					while (!worker.isDone()) {
						i++;
						try {
							if (i>10) {
								Thread.sleep(10);
							} else {
								Thread.sleep(1);
							}
						} catch (InterruptedException e) {
							con.getMessenger().error(this,"Request publishing was interrupted");
						}
					}
				}
				for (ControlChannelWorker worker: workers) {
					if (worker.getResponse()==null || !worker.getResponse().equals(getExecutedCommandResponse())) {
						output = getErrorJson(worker.getClient().getMember().getId() + " failed to execute command");
						break;
					}
				}
			}
			if (output==null) {
				output = getExecutedCommandResponse();
			}
			con.returnControlChannel();
		}
		return output;
	}
}
