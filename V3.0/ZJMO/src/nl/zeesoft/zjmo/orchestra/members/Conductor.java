package nl.zeesoft.zjmo.orchestra.members;

import nl.zeesoft.zjmo.json.JsFile;
import nl.zeesoft.zjmo.orchestra.MemberObject;
import nl.zeesoft.zjmo.orchestra.Orchestra;

public class Conductor extends MemberObject {
	private ConductorMemberController	controller	= null;
	
	public Conductor(Orchestra orchestra) {
		super(orchestra,Orchestra.CONDUCTOR,0);
		controller = new ConductorMemberController(orchestra);
	}
	
	public JsFile getMemberState(Object source) {
		//controller.getState(null);
		lockMe(source);
		JsFile f = getOrchestra().toJson(true);
		unlockMe(source);
		return f;
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

	public void updateState(String memberId, Object source) {
		System.out.println("Get state: " + memberId);
		controller.getState(memberId);
		System.out.println("Got state: " + memberId);
	}
}
