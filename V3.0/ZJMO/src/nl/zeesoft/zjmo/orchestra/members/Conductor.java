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
		return getOrchestra().toJson(true);
	}
	
	@Override
	public boolean start() {
		System.out.println("Starting " + getPosition().getName() + "/" + getPositionBackupNumber() + " (control: " + getControlPort() + ", work: " + getWorkPort() +  ") ...");
		boolean started = super.start();
		if (started) {
			controller.initialize();
			System.out.println("Started " + getPosition().getName() + "/" + getPositionBackupNumber());
		} else {
			System.err.println("Failed to start " + getPosition().getName() + "/" + getPositionBackupNumber());
		}
		return started;
	}

	@Override
	public void stop() {
		controller.close();
		super.stop();
	}
}
