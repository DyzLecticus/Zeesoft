package nl.zeesoft.zmmt.sequencer;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class SequencePlayerUpdateWorker extends Worker {
	private SequencePlayer		player		= null;

	public SequencePlayerUpdateWorker(Messenger msgr, WorkerUnion union,SequencePlayer player) {
		super(msgr, union);
		this.player = player;
		setSleep(100);
	}
	
	@Override
	public void whileWorking() {
		player.checkUpdateSequence();
	}
}
