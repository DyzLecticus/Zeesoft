package nl.zeesoft.zeetracker.gui;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zeetracker.gui.state.StateChangeEvent;
import nl.zeesoft.zeetracker.gui.state.StateChangeSubscriber;
import nl.zeesoft.zmmt.sequencer.SequencePlayer;

public class SequencePlayerImpl extends SequencePlayer implements StateChangeSubscriber {
	public SequencePlayerImpl(Messenger msgr, WorkerUnion uni) {
		super(msgr, uni);
	}

	@Override
	public void handleStateChange(StateChangeEvent evt) {
		if (evt.getType().equals(StateChangeEvent.CHANGED_COMPOSITION)) {
			setCompositionPattern(evt.getComposition(),evt.getSelectedPattern());
		} else if (evt.getType().equals(StateChangeEvent.CHANGED_PATTERN_SELECTION)) {
			setStartTickPattern(evt.getSelectedPatternRowFrom());
		} else if (evt.getType().equals(StateChangeEvent.CHANGED_SEQUENCE_SELECTION)) {
			setStartTickSequence(evt.getSelectedSequenceRowFrom());
		} else {
			setPattern(evt.getSelectedPattern());
		}
	}
}
 