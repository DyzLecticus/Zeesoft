package nl.zeesoft.zjmo.orchestra.members;

import nl.zeesoft.zjmo.orchestra.MemberObject;
import nl.zeesoft.zjmo.orchestra.Orchestra;

public class Player extends MemberObject {
	public Player(Orchestra orchestra,String positionName,int positionBackupNumber) {
		super(orchestra,positionName,positionBackupNumber);
	}
}
