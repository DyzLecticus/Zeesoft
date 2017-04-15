package nl.zeesoft.zjmo.orchestra.members;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zjmo.orchestra.MemberObject;
import nl.zeesoft.zjmo.orchestra.Orchestra;
import nl.zeesoft.zjmo.orchestra.OrchestraMember;

/**
 * Orchestra position player.
 */
public class Player extends MemberObject {
	public Player(Messenger msgr,Orchestra orchestra,String positionName,int positionBackupNumber) {
		super(msgr,orchestra,positionName,positionBackupNumber);
	}

	@Override
	protected boolean checkRestartRequired(Orchestra newOrchestra) {
		boolean r = false;
		OrchestraMember newMember = newOrchestra.getMemberById(getId());
		if (newMember==null || 
			newMember.getControlPort()!=getControlPort() || 
			newMember.getWorkPort()!=getWorkPort()
			) {
			r = true;
		}
		if (!r) {
			for (OrchestraMember conductor: getOrchestra().getConductors()) {
				OrchestraMember newConductor = newOrchestra.getMemberById(conductor.getId());
				if (newConductor==null || 
					!newConductor.getIpAddressOrHostName().equals(conductor.getIpAddressOrHostName()) || 
					newConductor.getControlPort()!=conductor.getControlPort() || 
					newConductor.getWorkPort()!=conductor.getWorkPort()
					) {
					r = true;
					break;
				}
			}
		}
		if (!r) {
			for (OrchestraMember newConductor: newOrchestra.getConductors()) {
				OrchestraMember conductor = getOrchestra().getMemberById(newConductor.getId());
				if (conductor==null) {
					r = true;
					break;
				}
			}
		}
		return r;
	}
}
