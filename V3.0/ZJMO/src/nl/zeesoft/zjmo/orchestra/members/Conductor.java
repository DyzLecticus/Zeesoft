package nl.zeesoft.zjmo.orchestra.members;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zjmo.orchestra.MemberObject;
import nl.zeesoft.zjmo.orchestra.Orchestra;
import nl.zeesoft.zjmo.orchestra.client.OrchestraConnector;
import nl.zeesoft.zjmo.orchestra.client.WorkClient;
import nl.zeesoft.zjmo.orchestra.protocol.ProtocolControl;
import nl.zeesoft.zjmo.orchestra.protocol.ProtocolControlConductor;
import nl.zeesoft.zjmo.orchestra.protocol.ProtocolWork;
import nl.zeesoft.zjmo.orchestra.protocol.ProtocolWorkConductor;

/**
 * Orchestra conductor.
 */
public class Conductor extends MemberObject {
	private ConductorMemberController	controller		= null;
	
	public Conductor(Messenger msgr,Orchestra orchestra,int positionBackupNumber) {
		super(msgr,orchestra,Orchestra.CONDUCTOR,positionBackupNumber);
		controller = new ConductorMemberController(getMessenger(),getUnion(),orchestra);
	}
	
	@Override
	public boolean start() {
		boolean started = super.start();
		if (started) {
			controller.open();
		}
		return started;
	}

	@Override
	public void stop(Worker ignoreWorker) {
		controller.close();
		super.stop(ignoreWorker);
	}

	@Override
	protected ProtocolControl getNewControlProtocol() {
		return new ProtocolControlConductor();
	}

	@Override
	protected ProtocolWork getNewWorkProtocol() {
		return new ProtocolWorkConductor();
	}

	@Override
	protected boolean checkRestartRequired(Orchestra newOrchestra) {
		return true;
	}

	public JsFile getOrchestraState() {
		return controller.getOrchestraState();
	}
	
	public JsFile getMemberState(String memberId) {
		return controller.getMemberState(memberId);
	}
	
	public void updateState(String memberId,boolean connect) {
		controller.getState(memberId,connect);
	}

	public ZStringBuilder takeOffline(String memberId) {
		return controller.takeOffline(memberId);
	}

	public ZStringBuilder drainOffline(String memberId) {
		return controller.drainOffline(memberId);
	}

	public void drainOfflineWorker(String memberId) {
		ConductorMemberDrainOfflineWorker worker = new ConductorMemberDrainOfflineWorker(getMessenger(),getUnion(),controller,memberId);
		worker.start();
	}

	public ZStringBuilder bringOnline(String memberId) {
		return controller.bringOnline(memberId);
	}

	public ZStringBuilder restart(String memberId) {
		return controller.restart(memberId);
	}

	public void workRequestTimedOut(WorkClient client) {
		if (controller.workRequestTimedOut(client)) {
			ConductorMemberDrainOnlineWorker worker = new ConductorMemberDrainOnlineWorker(getMessenger(),getUnion(),controller,client.getMemberId());
			worker.start();
		}
	}
	
	public WorkClient getClient(Object source,String positionName) {
		return controller.getClient(source, positionName);
	}

	public WorkClient getClientForMember(Object source,String memberId) {
		return controller.getClientForMember(source, memberId);
	}

	public void returnClient(WorkClient client) {
		controller.returnClient(client);
	}

	public OrchestraConnector getControlChannel() {
		return controller.getControlChannel();
	}

	public void returnControlChannel(boolean getState) {
		controller.returnControlChannel(getState);
	}
}
