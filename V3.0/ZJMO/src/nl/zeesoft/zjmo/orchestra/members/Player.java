package nl.zeesoft.zjmo.orchestra.members;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zjmo.orchestra.MemberClient;
import nl.zeesoft.zjmo.orchestra.MemberObject;
import nl.zeesoft.zjmo.orchestra.Orchestra;
import nl.zeesoft.zjmo.orchestra.OrchestraMember;

public class Player extends MemberObject {
	private MemberClient		client			= null;
	private PlayerClientWorker	clientWorker	= null;
	
	public Player(Messenger msgr,Orchestra orchestra,String positionName,int positionBackupNumber) {
		super(msgr,orchestra,positionName,positionBackupNumber);
	}

	@Override
	public boolean start() {
		boolean started = super.start();
		if (started) {
			if (client==null) {
				OrchestraMember conductor = getOrchestra().getConductor();
				client = conductor.getNewControlClient(getMessenger());
				clientWorker = new PlayerClientWorker(getMessenger(),getUnion(),client,getId());
				clientWorker.start();
			}
		}
		return started;
	}

	@Override
	public void stop() {
		clientWorker.stop();
		client.close();
		client = null;
		clientWorker = null;
		super.stop();
	}
}
