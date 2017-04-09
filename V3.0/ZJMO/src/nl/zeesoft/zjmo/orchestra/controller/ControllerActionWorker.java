package nl.zeesoft.zjmo.orchestra.controller;

import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zjmo.orchestra.MemberClient;
import nl.zeesoft.zjmo.orchestra.OrchestraMember;
import nl.zeesoft.zjmo.orchestra.ProtocolObject;
import nl.zeesoft.zjmo.orchestra.protocol.ProtocolControl;
import nl.zeesoft.zjmo.orchestra.protocol.ProtocolControlConductor;

public class ControllerActionWorker extends Worker {
	private OrchestraController 	controller 	= null;
	private MemberClient		 	client 		= null;

	private String					action		= "";
	private List<OrchestraMember>	members		= null;

	public ControllerActionWorker(Messenger msgr, WorkerUnion union,OrchestraController controller,MemberClient client) {
		super(msgr, union);
		setSleep(1000);
		this.controller = controller;
		this.client = client;
	}

	public void handleAction(String action,List<OrchestraMember> members) {
		this.action = action;
		this.members = members;
		start();
	}
	
	@Override
	public void whileWorking() {
		String err = "";
		for (OrchestraMember member: members) {
			ZStringBuilder response = sendMemberCommandForAction(client,member,action);
			if (response!=null && ProtocolObject.isErrorJson(response)) {
				err = member.getId() + ": " + ProtocolObject.getFirstElementValueFromJson(response);
				break;
			}
		}
		if (err.length() > 0) {
			controller.showErrorMessage(err,"Error");
		}
		stop();
	}
	
	protected ZStringBuilder sendMemberCommandForAction(MemberClient client,OrchestraMember member,String action) {
		ZStringBuilder response = null;
		if (action.equals(ProtocolControl.GET_STATE)) {
			response = client.sendCommand(ProtocolControlConductor.UPDATE_MEMBER_STATE,"id",member.getId());
		} else if (action.equals(ProtocolControl.DRAIN_OFFLINE)) {
			response = client.sendCommand(ProtocolControlConductor.DRAIN_MEMBER_OFFLINE,"id",member.getId());
		} else if (action.equals(ProtocolControl.TAKE_OFFLINE)) {
			response = client.sendCommand(ProtocolControlConductor.TAKE_MEMBER_OFFLINE,"id",member.getId());
		} else if (action.equals(ProtocolControl.BRING_ONLINE)) {
			response = client.sendCommand(ProtocolControlConductor.BRING_MEMBER_ONLINE,"id",member.getId());
		} else if (action.equals(ProtocolControl.RESTART_PROGRAM)) {
			response = client.sendCommand(ProtocolControlConductor.RESTART_MEMBER,"id",member.getId());
		}
		return response;
	}
}
