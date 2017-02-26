package nl.zeesoft.zjmo.orchestra.members;

import nl.zeesoft.zjmo.orchestra.MemberObject;
import nl.zeesoft.zjmo.orchestra.MemberState;
import nl.zeesoft.zjmo.orchestra.Orchestra;
import nl.zeesoft.zjmo.orchestra.OrchestraMember;

public class Conductor extends MemberObject {
	private Orchestra orchestra = null;
	
	public Conductor(Orchestra orchestra) {
		this.orchestra = orchestra;
	}
	
	public String setMemberState(String positionName,int positionBackupNumber, String stateCode) {
		String err = "";
		lockMe(this);
		OrchestraMember member = orchestra.getMemberForPosition(positionName, positionBackupNumber);
		MemberState state = MemberState.getState(stateCode);
		if (member==null) {
			err = "Member not defined for position: " + positionName + "/" + positionBackupNumber;
		} else if (state==null) {
			err = "Unknown state code: " + stateCode;
		} else {
			member.setState(state);
		}
		unlockMe(this);
		return err;
	}
}
