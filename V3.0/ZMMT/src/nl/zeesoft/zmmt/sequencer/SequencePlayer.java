package nl.zeesoft.zmmt.sequencer;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zmmt.composition.Composition;
import nl.zeesoft.zmmt.gui.state.StateChangeEvent;
import nl.zeesoft.zmmt.gui.state.StateChangeSubscriber;

public class SequencePlayer extends Locker implements StateChangeSubscriber {
	private Sequencer	sequencer				= null;
	private	boolean		patternMode				= true;
	
	private int			selectedPattern			= 0;
	private Composition compositionCopy			= null;
	private Sequence 	sequence				= null;
	
	public SequencePlayer(Messenger msgr) {
		super(msgr);
	}

	public void setSequencer(Sequencer sequencer) {
		lockMe(this);
		this.sequencer = sequencer;
		updateSequencerSequence();
		unlockMe(this);
	}

	public void setPatternMode(boolean patternMode) {
		lockMe(this);
		if (!this.patternMode==patternMode) {
			this.patternMode = patternMode;
			updateSequence();
		}
		unlockMe(this);
	}
	
	@Override
	public void handleStateChange(StateChangeEvent evt) {
		if (evt.getType().equals(StateChangeEvent.CHANGED_COMPOSITION)) {
			lockMe(this);
			selectedPattern = evt.getSelectedPattern();
			compositionCopy = evt.getComposition().copy();
			updateSequence();
			unlockMe(this);
		} else if (evt.getSelectedPattern()!=selectedPattern) {
			lockMe(this);
			selectedPattern = evt.getSelectedPattern();
			if (patternMode) {
				updateSequence();
			}
			unlockMe(this);
		}
	}
	
	public void start() {
		lockMe(this);
		System.out.println(this + ": Start");
		if (sequencer!=null) {
			if (sequencer.isRunning()) {
				sequencer.stop();
			}
			sequencer.start();
		}
		unlockMe(this);
	}

	public void stop() {
		lockMe(this);
		System.out.println(this + ": Stop");
		if (sequencer!=null) {
			if (sequencer.isRunning()) {
				sequencer.stop();
				sequencer.setTickPosition(0);
			}
		}
		unlockMe(this);
	}
	
	protected void setTempo(float fBPM) {
		float fCurrent = sequencer.getTempoInBPM();
		float fFactor = fBPM / fCurrent;
		sequencer.setTempoFactor(fFactor);
	}
	
	protected void updateSequence() {
		CompositionToSequenceConvertor convertor = new CompositionToSequenceConvertor(getMessenger(),compositionCopy);
		if (patternMode) {
			sequence = convertor.getPatternSequence(selectedPattern);
		} else {
			// TODO: Implement
		}
		updateSequencerSequence();
	}
	
	protected void updateSequencerSequence() {
		if (sequencer!=null && sequence!=null) {
			boolean restart = false;
			long currentTick = 0;
			if (sequencer.isRunning()) {
				restart = true;
				sequencer.stop();
				currentTick = sequencer.getTickPosition();
			}
			try {
				sequencer.setSequence(sequence);
			} catch (InvalidMidiDataException e) {
				getMessenger().error(this,"Invalid MIDI data",e);
			}
			setTempo((float) compositionCopy.getBeatsPerMinute());
			if (restart) {
				sequencer.setTickPosition(currentTick);
				sequencer.start();
			}
		}
	}
}
 