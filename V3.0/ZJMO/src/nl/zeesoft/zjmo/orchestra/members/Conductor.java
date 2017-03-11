package nl.zeesoft.zjmo.orchestra.members;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zjmo.json.JsFile;
import nl.zeesoft.zjmo.orchestra.MemberObject;
import nl.zeesoft.zjmo.orchestra.Orchestra;
import nl.zeesoft.zjmo.orchestra.protocol.ProtocolControl;
import nl.zeesoft.zjmo.orchestra.protocol.ProtocolControlConductor;
import nl.zeesoft.zjmo.orchestra.protocol.ProtocolWork;
import nl.zeesoft.zjmo.orchestra.protocol.ProtocolWorkConductor;

public class Conductor extends MemberObject {
	private ConductorMemberController	controller	= null;
	
	public Conductor(Messenger msgr,Orchestra orchestra) {
		super(msgr,orchestra,Orchestra.CONDUCTOR,0);
		controller = new ConductorMemberController(getMessenger(),getUnion(),orchestra);
	}
	
	@Override
	public boolean start() {
		boolean started = super.start();
		if (started) {
			controller.open();
			controller.getState(Orchestra.CONDUCTORID);
		}
		return started;
	}

	@Override
	public void stop() {
		controller.close();
		super.stop();
	}

	@Override
	protected ProtocolControl getNewControlProtocol() {
		return new ProtocolControlConductor();
	}

	@Override
	protected ProtocolWork getNewWorkProtocol() {
		return new ProtocolWorkConductor();
	}

	public JsFile getOrchestraState() {
		return controller.getOrchestraState();
	}
	
	public JsFile getMemberState(String memberId) {
		return controller.getMemberState(memberId);
	}
	
	public void updateState(String memberId) {
		controller.getState(memberId);
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

	public WorkClient getClient(Object source,String positionName) {
		return controller.getClient(source, positionName);
	}

	public void returnClient(WorkClient client) {
		controller.returnClient(client);
	}
}
