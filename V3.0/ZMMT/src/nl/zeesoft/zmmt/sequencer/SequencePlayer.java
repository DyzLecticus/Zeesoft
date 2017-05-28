package nl.zeesoft.zmmt.sequencer;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zmmt.composition.Composition;
import nl.zeesoft.zmmt.gui.state.StateChangeEvent;
import nl.zeesoft.zmmt.gui.state.StateChangeSubscriber;

public class SequencePlayer extends Locker implements StateChangeSubscriber, MetaEventListener {
	private static final int				END_OF_SEQUENCE		= 47;
	private	static final boolean			USE_LOOP_FIX		= true;

	private Sequencer						sequencer			= null;

	private	boolean							patternMode			= true;
	private int								selectedPattern		= 0;
	private Composition						compositionCopy		= null;

	private Sequence 						sequence			= null;
	private int								sequenceEndTick		= 0;
	private boolean							updateSequence		= false;
	
	private List<SequencePlayerSubscriber>	subscribers			= new ArrayList<SequencePlayerSubscriber>();
	
	public SequencePlayer(Messenger msgr,WorkerUnion uni) {
		super(msgr);
	}

	public void addSequencerSubscriber(SequencePlayerSubscriber sub) {
		lockMe(this);
		subscribers.add(sub);
		unlockMe(this);
	}
	
	public void setSequencer(Sequencer sequencer) {
		lockMe(this);
		this.sequencer = sequencer;
		if (USE_LOOP_FIX) {
			sequencer.addMetaEventListener(this);
		} else {
			sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
		}
		updateSequencerSequence();
		unlockMe(this);
	}

	public void setPatternMode(boolean patternMode) {
		lockMe(this);
		if (!this.patternMode==patternMode) {
			this.patternMode = patternMode;
			updateSequence = true;
		}
		unlockMe(this);
	}
	
	@Override
	public void handleStateChange(StateChangeEvent evt) {
		if (evt.getType().equals(StateChangeEvent.CHANGED_COMPOSITION)) {
			lockMe(this);
			selectedPattern = evt.getSelectedPattern();
			compositionCopy = evt.getComposition().copy();
			updateSequence = true;
			unlockMe(this);
		} else if (evt.getSelectedPattern()!=selectedPattern) {
			lockMe(this);
			selectedPattern = evt.getSelectedPattern();
			if (patternMode) {
				updateSequence = true;
			}
			unlockMe(this);
		}
	}
	
	@Override
	public void meta(MetaMessage message) {
		if (message.getType()==END_OF_SEQUENCE) {
			lockMe(this);
			if (sequencer!=null && sequencer.isOpen()) {
				sequencer.setTickPosition(0);
				sequencer.start();
			}
			unlockMe(this);
		}
	}
	
	public void start() {
		lockMe(this);
		if (sequencer!=null && sequencer.getSequence()!=null) {
			if (sequencer.isRunning()) {
				sequencer.stop();
			}
			sequencer.start();
		}
		List<SequencePlayerSubscriber> subs = new ArrayList<SequencePlayerSubscriber>(subscribers);	
		unlockMe(this);
		for (SequencePlayerSubscriber sub: subs) {
			sub.started();
		}
	}

	public void stop() {
		lockMe(this);
		if (sequencer!=null) {
			if (sequencer.isRunning()) {
				sequencer.stop();
				sequencer.setTickPosition(0);
			}
		}
		List<SequencePlayerSubscriber> subs = new ArrayList<SequencePlayerSubscriber>(subscribers);	
		unlockMe(this);
		for (SequencePlayerSubscriber sub: subs) {
			sub.stopped();
		}
	}
	
	protected void checkUpdateSequence() {
		boolean update = false;
		boolean pattern = true;
		int	number = 0;
		Composition comp = null;
		lockMe(this);
		if (updateSequence) {
			updateSequence = false;
			update = true;
			pattern = patternMode;
			number = selectedPattern;
			comp = compositionCopy;
		}
		unlockMe(this);
		if (update) {
			Sequence seq = null;
			int endTick = 0;
			CompositionToSequenceConvertor convertor = new CompositionToSequenceConvertor(getMessenger(),comp);
			if (pattern) {
				seq = convertor.getPatternSequence(number);
				endTick = convertor.getPatternSequenceEndTick(number); 
			} else {
				// TODO: Implement
			}
			if (seq!=null) {
				lockMe(this);
				sequence = seq;
				sequenceEndTick = endTick;
				updateSequencerSequence();
				unlockMe(this);
			}
		}
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
				sequencer.setLoopStartPoint(0);
				sequencer.setLoopEndPoint(sequenceEndTick);
				if (currentTick>sequenceEndTick) {
					currentTick = 0;
				}
			} catch (InvalidMidiDataException e) {
				getMessenger().error(this,"Invalid MIDI data",e);
			}
			if (restart) {
				sequencer.setTickPosition(currentTick);
				sequencer.start();
			}
		}
	}
}
 