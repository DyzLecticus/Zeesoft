package nl.zeesoft.zjmo.orchestra.members;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zjmo.orchestra.MemberObject;
import nl.zeesoft.zjmo.orchestra.Orchestra;

/**
 * Orchestra position player.
 */
public class Player extends MemberObject {
	public Player(Messenger msgr,Orchestra orchestra,String positionName,int positionBackupNumber) {
		super(msgr,orchestra,positionName,positionBackupNumber);
	}
}
