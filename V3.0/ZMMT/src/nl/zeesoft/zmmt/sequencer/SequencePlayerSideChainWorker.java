package nl.zeesoft.zmmt.sequencer;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zmmt.composition.Composition;

public class SequencePlayerSideChainWorker extends Worker {
	private SequencePlayer		player		= null;
	private Composition			comp		= null;
	
	private boolean				changed 	= false;

	public SequencePlayerSideChainWorker(Messenger msgr, WorkerUnion union,SequencePlayer player) {
		super(msgr, union);
		this.player = player;
		setSleep(100);
	}
	
	public void setComposition(Composition comp) {
		lockMe(this);
		this.comp = comp;
		changed = true;
		unlockMe(this);
	}
	
	@Override
	public void whileWorking() {
		Composition copy = null;
		lockMe(this);
		if (changed) {
			changed = false;
			copy = comp.copy();
		}
		unlockMe(this);
		if (copy!=null) {
			CompositionToSequenceConvertor convertor = new CompositionToSequenceConvertor(getMessenger(),copy);
			convertor.convertSequenceInternal(true);
			lockMe(this);
			boolean update = !changed;
			unlockMe(this);
			if (update) {
				player.setFullSequenceAndEndTick(convertor.getSequence(),convertor.getSequenceEndTick());
			}
		}
	}
}
