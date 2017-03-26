package nl.zeesoft.zso.orchestra;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zjmo.orchestra.Orchestra;
import nl.zeesoft.zjmo.orchestra.OrchestraGenerator;
import nl.zeesoft.zjmo.orchestra.members.Player;
import nl.zeesoft.zso.orchestra.members.SamplePlayer;

public class SampleOrchestra extends Orchestra {
	public static final String BASEBEAT		= "Basebeat";
	public static final String SNARE		= "Snare";
	public static final String HIHAT		= "Hihat";
	
	@Override
	public void initialize() {
		addPosition(BASEBEAT);
		addPosition(SNARE);
		addPosition(HIHAT);
		
		addMember(BASEBEAT,0,LOCALHOST,6543,6542,100,true);
		addMember(SNARE,0,LOCALHOST,7654,7653,100,true);
		addMember(HIHAT,0,LOCALHOST,8765,8764,100,true);

		// Backups 
		addMember(BASEBEAT,1,LOCALHOST,6541,6540,200,false);
		addMember(SNARE,1,LOCALHOST,7652,7651,200,false);
		addMember(HIHAT,1,LOCALHOST,8763,8762,200,false);
	}

	@Override
	public OrchestraGenerator getNewGenerator() {
		return new SampleOrchestraGenerator();
	}

	@Override
	public Player getNewPlayer(Messenger msgr, String positionName, int positionBackupNumber) {
		return new SamplePlayer(msgr,this,positionName,positionBackupNumber);
	}
}
