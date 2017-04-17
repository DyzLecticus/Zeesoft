package nl.zeesoft.zjmo.orchestra.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zjmo.Orchestrator;
import nl.zeesoft.zjmo.orchestra.MemberClient;
import nl.zeesoft.zjmo.orchestra.Orchestra;
import nl.zeesoft.zjmo.orchestra.OrchestraMember;
import nl.zeesoft.zjmo.orchestra.ProtocolObject;
import nl.zeesoft.zjmo.orchestra.protocol.ProtocolControl;
import nl.zeesoft.zjmo.orchestra.protocol.ProtocolControlConductor;

public class ControllerActionWorker extends Worker {
	private OrchestraController 	controller 		= null;
	private MemberClient		 	client 			= null;

	private String					action			= "";
	private List<OrchestraMember>	members			= null;
	private Orchestra				orchestraUpdate	= null;

	public ControllerActionWorker(Messenger msgr, WorkerUnion union,OrchestraController controller,MemberClient client) {
		super(msgr, union);
		setSleep(1000);
		this.controller = controller;
		this.client = client;
	}

	public void publishOrchestraUpdate(Orchestra orchestraUpdate) {
		this.action = ProtocolControl.UPDATE_ORCHESTRA;
		this.orchestraUpdate = orchestraUpdate;
		members = null;
		start();
	}

	public void handleAction(String action,List<OrchestraMember> members) {
		this.action = action;
		List<OrchestraMember> orderedMembers = new ArrayList<OrchestraMember>();
		for (OrchestraMember member: members) {
			if (!member.getPosition().getName().equals(Orchestra.CONDUCTOR)) {
				orderedMembers.add(member);
			}
		}
		for (OrchestraMember member: members) {
			if (member.getPosition().getName().equals(Orchestra.CONDUCTOR) && member.getPositionBackupNumber()>0) {
				orderedMembers.add(member);
			}
		}
		for (OrchestraMember member: members) {
			if (member.getPosition().getName().equals(Orchestra.CONDUCTOR) && member.getPositionBackupNumber()==0) {
				orderedMembers.add(member);
			}
		}
		this.members = orderedMembers;
		orchestraUpdate = null;
		start();
	}
	
	@Override
	public void whileWorking() {
		String err = "";
		if (action.equals(ProtocolControl.UPDATE_ORCHESTRA)) {
			JsFile json = orchestraUpdate.toJson(false);
			json.rootElement.children.add(0,new JsElem("command",ProtocolControlConductor.PUBLISH_ORCHESTRA,true));
			ZStringBuilder response = client.writeOutputReadInput(json.toStringBuilder(),1000);
			if (response==null || !response.equals(ProtocolObject.getExecutedCommandResponse())) {
				if (ProtocolObject.isErrorJson(response)) {
					err = "Failed to publish changes: " + ProtocolObject.getErrorFromJson(response);
				} else {
					err = "Failed to publish changes";
				}
			} else {
				File f = new File(Orchestrator.ORCHESTRA_JSON);
				if (f.exists()) {
					err = orchestraUpdate.toJson(false).toFile(Orchestrator.ORCHESTRA_JSON,true);
				}
				List<OrchestraMember> restartMembers = controller.publishedOrchestraUpdate();
				if (restartMembers.size()>0) {
					action = ProtocolControl.RESTART_PROGRAM;
					members = restartMembers;
				}
			}
		}
		if (!action.equals(ProtocolControl.UPDATE_ORCHESTRA)) {
			List<OrchestraMember> doneMembers = new ArrayList<OrchestraMember>();
			for (OrchestraMember member: members) {
				ZStringBuilder response = sendMemberCommandForAction(client,member,action);
				if (response!=null && ProtocolObject.isErrorJson(response)) {
					err = member.getId() + ": " + ProtocolObject.getFirstElementValueFromJson(response);
					break;
				} else {
					doneMembers.add(member);
				}
			}
			if (action.equals(ProtocolControl.RESTART_PROGRAM)) {
				controller.restartedMembers(doneMembers);
			}
		}
		if (err.length() > 0) {
			controller.showErrorMessage(err);
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
