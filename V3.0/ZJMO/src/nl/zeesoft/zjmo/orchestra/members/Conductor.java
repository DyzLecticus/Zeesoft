package nl.zeesoft.zjmo.orchestra.members;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zjmo.json.JsFile;
import nl.zeesoft.zjmo.orchestra.MemberObject;
import nl.zeesoft.zjmo.orchestra.Orchestra;
import nl.zeesoft.zjmo.orchestra.ProtocolControl;
import nl.zeesoft.zjmo.orchestra.ProtocolControlConductor;

public class Conductor extends MemberObject {
	private ConductorMemberController	controller	= null;
	
	public Conductor(Orchestra orchestra) {
		super(orchestra,Orchestra.CONDUCTOR,0);
		controller = new ConductorMemberController(orchestra);
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
		//System.out.println("Closing controller ...");
		controller.close();
		//System.out.println("Stopping conductor ...");
		super.stop();
	}

	@Override
	protected ProtocolControl getNewControlProtocol() {
		return new ProtocolControlConductor();
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

	public ZStringBuilder bringOnline(String memberId) {
		return controller.bringOnline(memberId);
	}
}
