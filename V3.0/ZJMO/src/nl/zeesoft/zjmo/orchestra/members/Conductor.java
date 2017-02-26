package nl.zeesoft.zjmo.orchestra.members;

import nl.zeesoft.zjmo.json.JsFile;
import nl.zeesoft.zjmo.orchestra.MemberObject;
import nl.zeesoft.zjmo.orchestra.Orchestra;

public class Conductor extends MemberObject {
	private MemberController	controller	= null;
	
	public Conductor(Orchestra orchestra) {
		super(orchestra,Orchestra.CONDUCTOR,0);
		this.controller = new MemberController(orchestra);
	}
	
	public JsFile getMemberState() {
		controller.getState();
		return getOrchestra().toJson(true);
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
		controller.close();
		super.stop();
	}
}
