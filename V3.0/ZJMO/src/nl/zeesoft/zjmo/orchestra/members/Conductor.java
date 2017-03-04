package nl.zeesoft.zjmo.orchestra.members;

import nl.zeesoft.zjmo.json.JsFile;
import nl.zeesoft.zjmo.orchestra.MemberObject;
import nl.zeesoft.zjmo.orchestra.Orchestra;

public class Conductor extends MemberObject {
	private ConductorMemberController	controller	= null;
	
	public Conductor(Orchestra orchestra) {
		super(orchestra,Orchestra.CONDUCTOR,0);
		this.controller = new ConductorMemberController(orchestra);
	}
	
	public JsFile getMemberState() {
		lockMe(this);
		JsFile f = getOrchestra().toJson(true);
		unlockMe(this);
		return f;
	}
	
	@Override
	public boolean start() {
		boolean started = super.start();
		if (started) {
			controller.initialize();
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

	public void updateState(String memberId) {
		lockMe(this);
		System.out.println("Get state: " + memberId);
		controller.getState(memberId);
		unlockMe(this);
	}
}
