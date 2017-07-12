package nl.zeesoft.zmmt.test;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zmmt.composition.Composition;
import nl.zeesoft.zmmt.sequencer.SequencePlayer;

public class MockSequencePlayer extends SequencePlayer {

	public MockSequencePlayer(Messenger msgr, WorkerUnion uni) {
		super(msgr, uni);
	}
	
	public void setComposition(Composition comp) {
		setCompositionPattern(comp,0);
	}
}
