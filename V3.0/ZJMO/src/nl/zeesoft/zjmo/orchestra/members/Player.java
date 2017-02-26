package nl.zeesoft.zjmo.orchestra.members;

import nl.zeesoft.zjmo.orchestra.MemberObject;
import nl.zeesoft.zjmo.orchestra.Orchestra;

public class Player extends MemberObject {
	public Player(Orchestra orchestra,String positionName,int positionBackupNumber) {
		super(orchestra,positionName,positionBackupNumber);
	}
	
	@Override
	public boolean start() {
		System.out.println("Starting " + getPosition().getName() + "/" + getPositionBackupNumber() + " (control: " + getControlPort() + ", work: " + getWorkPort() +  ") ...");
		boolean started = super.start();
		if (started) {
			System.out.println("Started " + getPosition().getName() + "/" + getPositionBackupNumber());
		} else {
			System.err.println("Failed to start " + getPosition().getName() + "/" + getPositionBackupNumber());
		}
		return started;
	}
}
