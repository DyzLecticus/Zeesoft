package nl.zeesoft.zso.orchestra;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zjmo.orchestra.Channel;
import nl.zeesoft.zjmo.orchestra.Orchestra;
import nl.zeesoft.zjmo.orchestra.OrchestraGenerator;
import nl.zeesoft.zjmo.orchestra.OrchestraMember;
import nl.zeesoft.zjmo.orchestra.controller.OrchestraController;
import nl.zeesoft.zjmo.orchestra.members.Player;
import nl.zeesoft.zso.orchestra.controller.SampleOrchestraController;
import nl.zeesoft.zso.orchestra.members.MidiPlayer;
import nl.zeesoft.zso.orchestra.members.SamplePlayer;

public class SampleOrchestra extends Orchestra {
	public static final String CONTROL			= "Control";
	public static final String BASEBEAT			= "Basebeat";
	public static final String SNARE			= "Snare";
	public static final String HIHAT			= "Hihat";
	public static final String SYNTHESIZER		= "Synthesizer";

	public static final String SYNTH_BASS		= "Bass";
	public static final String SYNTH_PIANO		= "Piano";
	public static final String SYNTH_HARP		= "Harp";
	public static final String SYNTH_STRINGS	= "Strings";

	@Override
	public void initialize() {
		Channel controlChannel = addChannel(CONTROL,true);
		
		addPosition(BASEBEAT);
		addPosition(SNARE);
		addPosition(HIHAT);
		addPosition(SYNTHESIZER);

		OrchestraMember conductor0 = getConductors().get(0);
		conductor0.setWorkRequestTimeoutDrain(true);
		conductor0.setControlPort(7700);
		conductor0.setWorkPort(7701);
		
		addMember(BASEBEAT,0,LOCALHOST,8800,8801,100,true);
		addMember(SNARE,0,LOCALHOST,8802,8803,100,true);
		addMember(HIHAT,0,LOCALHOST,8804,8805,100,true);
		//addMember(SYNTHESIZER,0,LOCALHOST,8806,8807,100,true);

		// Backups 
		addMember(CONDUCTOR,1,LOCALHOST,7702,7703,500,false);

		addMember(BASEBEAT,1,LOCALHOST,9900,9901,200,true);
		addMember(SNARE,1,LOCALHOST,9902,9903,200,true);
		addMember(HIHAT,1,LOCALHOST,9904,9905,200,true);
		//addMember(SYNTHESIZER,1,LOCALHOST,9906,9907,200,true);
		
		// Instrument control channel
		for (OrchestraMember member: getMembers()) {
			if (!member.getPosition().getName().equals(CONDUCTOR)) {
				controlChannel.getSubscriberIdList().add(member.getId());
			}
		}
	}

	@Override
	public OrchestraGenerator getNewGenerator() {
		return new SampleOrchestraGenerator();
	}

	@Override
	public OrchestraController getNewController(boolean exitOnClose) {
		return new SampleOrchestraController(this,exitOnClose);
	}

	@Override
	public Player getNewPlayer(Messenger msgr, String positionName, int positionBackupNumber) {
		Player r = null;
		if (positionName.equals(SYNTHESIZER)) {
			r = new MidiPlayer(msgr,this,positionName,positionBackupNumber);
		} else {
			r = new SamplePlayer(msgr,this,positionName,positionBackupNumber);
		}
		return r;
	}
}
