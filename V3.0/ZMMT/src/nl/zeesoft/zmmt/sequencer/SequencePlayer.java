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
import nl.zeesoft.zmmt.composition.Pattern;

public class SequencePlayer extends Locker implements MetaEventListener {
	private static final int				END_OF_SEQUENCE			= 47;
	private	static final boolean			USE_LOOP_FIX			= true;

	private Sequencer						sequencer				= null;

	private	boolean							playing					= false;
	private	boolean							patternMode				= false;
	private int								selectedPattern			= 0;
	private Composition						compositionCopy			= null;

	private Sequence 						sequence				= null;
	private long							sequenceEndTick			= 0;
	private boolean							updateSequence			= false;

	private Sequence 						fullSequence			= null;
	private long							fullSequenceEndTick		= 0;

	private long							startPatternTick		= 0;
	private long							startSequenceTick		= 0;
	private long							stopTick				= 0;

	private List<SequencePlayerSubscriber>	subscribers				= new ArrayList<SequencePlayerSubscriber>();
	
	private SequencePlayerUpdateWorker		updateWorker 			= null;
	private SequencePlayerSideChainWorker	sideChainWorker			= null;
	
	public SequencePlayer(Messenger msgr,WorkerUnion uni) {
		super(msgr);
		updateWorker = new SequencePlayerUpdateWorker(msgr,uni,this);
		sideChainWorker = new SequencePlayerSideChainWorker(msgr,uni,this);
	}

	public void startWorkers() {
		updateWorker.start();
		sideChainWorker.start();
	}
	
	public void stopWorkers() {
		updateWorker.stop();
		sideChainWorker.stop();
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
		boolean update = false;
		lockMe(this);
		if (!this.patternMode==patternMode) {
			this.patternMode = patternMode;
			if (patternMode || fullSequence==null) {
				update = true;
			} else {
				sequence = fullSequence;
				sequenceEndTick = fullSequenceEndTick;
				updateSequencerSequence();
			}
		}
		unlockMe(this);
		if (update) {
			updateSequence();
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
		long startTick = 0;
		lockMe(this);
		if (patternMode) {
			startTick = startPatternTick;
		} else {
			startTick = startSequenceTick;
		}
		unlockMe(this);
		start(startTick);
	}
		
	public void start(long startTick) {
		boolean started = false;
		lockMe(this);
		if (sequencer!=null && sequencer.getSequence()!=null) {
			if (sequencer.isRunning()) {
				sequencer.stop();
			}
			sequencer.setTickPosition(startTick);
			sequencer.start();
			playing = true;
			started = true;
		}
		if (stopTick<startTick) {
			stopTick=startTick;
		}
		List<SequencePlayerSubscriber> subs = new ArrayList<SequencePlayerSubscriber>(subscribers);	
		unlockMe(this);
		if (started) {
			for (SequencePlayerSubscriber sub: subs) {
				sub.started();
			}
		}
	}

	public void stop() {
		boolean stopped = false;
		lockMe(this);
		if (sequencer!=null) {
			if (sequencer.isRunning()) {
				sequencer.stop();
				stopTick = sequencer.getTickPosition();
				playing = false;
				stopped = true;
			}
		}
		List<SequencePlayerSubscriber> subs = new ArrayList<SequencePlayerSubscriber>(subscribers);	
		unlockMe(this);
		if (stopped) {
			for (SequencePlayerSubscriber sub: subs) {
				sub.stopped();
			}
		}
	}

	public void startContinue() {
		start(stopTick);
	}

	public boolean isPlaying() {
		boolean r = false;
		lockMe(this);
		r = playing;
		unlockMe(this);
		return r;
	}

	protected void setCompositionPattern(Composition comp,int pattern) {
		lockMe(this);
		selectedPattern = pattern;
		compositionCopy = comp.copy();
		fullSequence = null;
		fullSequenceEndTick = 0;
		updateSequence = true;
		unlockMe(this);
	}
	
	protected void setStartTickPattern(int selectedRow) {
		lockMe(this);
		if (compositionCopy!=null) {
			startPatternTick = selectedRow * compositionCopy.getTicksPerStep();
			if (startPatternTick>0) {
				startPatternTick--;
			}
		} else {
			startPatternTick = 0;
		}
		unlockMe(this);
	}
	
	protected void setStartTickSequence(int selectedRow) {
		lockMe(this);
		if (compositionCopy!=null) {
			startSequenceTick = 0;
			for (int i = 0; i < selectedRow; i++) {
				Pattern ptn = compositionCopy.getPattern(compositionCopy.getSequence().get(i));
				if (ptn!=null) {
					int steps = compositionCopy.getStepsForPattern(ptn);
					startSequenceTick = startSequenceTick + (steps * compositionCopy.getTicksPerStep());
				}
			}
			if (startSequenceTick>0) {
				startSequenceTick--;
			}
		} else {
			startSequenceTick = 0;
		}
		unlockMe(this);
	}

	protected void setPattern(int pattern) {
		lockMe(this);
		if (selectedPattern!=pattern) {
			selectedPattern = pattern;
			if (patternMode) {
				updateSequence = true;
			}
		}
		unlockMe(this);
	}

	protected void checkUpdateSequence() {
		boolean update = false;
		lockMe(this);
		if (updateSequence && compositionCopy!=null) {
			updateSequence = false;
			update = true;
		}
		unlockMe(this);
		if (update) {
			updateSequence();
		}
	}

	protected void updateSequence() {
		boolean pattern = true;
		int	number = 0;
		Composition comp = null;
		lockMe(this);
		if (compositionCopy!=null) {
			pattern = patternMode;
			number = selectedPattern;
			comp = compositionCopy;
		}
		unlockMe(this);
		if (comp!=null) {
			Sequence seq = null;
			long endTick = 0;
			CompositionToSequenceConvertor convertor = new CompositionToSequenceConvertor(getMessenger(),comp);
			if (pattern) {
				convertor.convertPatternInternal(number,true);
			} else {
				if (comp.getSynthesizerConfiguration().getSideChainSource().length()>0) {
					sideChainWorker.setComposition(comp);
				}
				convertor.convertSequenceInternal(false);
			}
			seq = convertor.getSequence();
			endTick = convertor.getSequenceEndTick();
			if (seq!=null) {
				lockMe(this);
				sequence = seq;
				sequenceEndTick = endTick;
				updateSequencerSequence();
				if (!patternMode) {
					fullSequence = seq;
					fullSequenceEndTick = endTick;
				}
				unlockMe(this);
			}
		}
	}
	
	protected void setFullSequenceAndEndTick(Sequence seq, long endTick) {
		lockMe(this);
		fullSequence = seq;
		fullSequenceEndTick = endTick;
		if (!patternMode) {
			sequence = seq;
			sequenceEndTick = endTick;
			updateSequencerSequence();
		}
		unlockMe(this);
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
 