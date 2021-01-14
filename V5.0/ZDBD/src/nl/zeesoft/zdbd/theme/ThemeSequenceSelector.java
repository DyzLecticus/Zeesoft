package nl.zeesoft.zdbd.theme;

import java.util.List;

import javax.sound.midi.Sequence;

import nl.zeesoft.zdbd.api.html.form.SequencerControl;
import nl.zeesoft.zdbd.midi.MidiSequencer;
import nl.zeesoft.zdbd.midi.MidiSequencerEventListener;
import nl.zeesoft.zdbd.midi.MidiSys;
import nl.zeesoft.zdbd.midi.MixState;
import nl.zeesoft.zdbd.neural.NetworkTrainer;
import nl.zeesoft.zdbd.pattern.PatternSequence;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Rand;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.thread.Lock;

public class ThemeSequenceSelector implements MidiSequencerEventListener, EventListener {
	private Lock				lock					= new Lock();
	
	private boolean				hold					= false;
	private boolean				selectRandom			= false;
	private boolean				selectTrainingSequence	= false;
	private boolean				regenerateOnPlay		= true;
	
	private String				currSequence			= "";
	private String				nextSequence			= "";
	private MixState			currMix					= new MixState();
	private MixState			nextMix					= new MixState();

	private boolean				recording				= false;

	private ThemeController		controller				= null;
	
	public void setController(ThemeController controller) {
		lock.lock(this);
		if (this.controller!=null) {
			this.controller.eventPublisher.removeListener(this);
		}
		this.controller = controller;
		if (this.controller!=null) {
			this.controller.eventPublisher.addListener(this);
		}
		lock.unlock(this);
	}

	public void setHold(boolean hold) {
		lock.lock(this);
		if (this.hold!=hold) {
			this.hold = hold;
			if (!hold) {
				nextSequence = selectNextSequenceNoLock();
				changedNextSequenceNoLock();
			} else if (!nextSequence.equals(currSequence)) {
				nextSequence = currSequence;
				changedNextSequenceNoLock();
			}
		}
		lock.unlock(this);
	}

	public void setSelectRandom(boolean selectRandom) {
		lock.lock(this);
		this.selectRandom = selectRandom;
		lock.unlock(this);
	}

	public void setSelectTrainingSequence(boolean selectTrainingSequence) {
		lock.lock(this);
		this.selectTrainingSequence = selectTrainingSequence;
		lock.unlock(this);
	}

	public void setRegenerateOnPlay(boolean regenerateOnPlay) {
		lock.lock(this);
		this.regenerateOnPlay = regenerateOnPlay;
		lock.unlock(this);
	}
	
	public void start() {
		lock.lock(this);
		if (!MidiSys.isInitialized()) {
			Logger.err(this, new Str("Midi system is not initialized"));
		} else if (controller==null) {
			Logger.err(this, new Str("Theme controller has not been set"));
		} else if (!controller.isBusy() && currSequence.length()>0 && !MidiSys.sequencer.isRunning()) {
			MidiSys.sequencer.start();
			if (regenerateOnPlay && !currSequence.equals(NetworkTrainer.TRAINING_SEQUENCE)) {
				controller.generateSequence(currSequence).start();
			}
		}
		lock.unlock(this);
	}

	public void startSequence(String name) {
		startTheme(name,false);
	}
	
	public void startTheme(String startSequence) {
		startTheme(startSequence,true);
	}
	
	public void setCurrentSequence(String currentSequence) {
		if (currentSequence.length()>0 && !this.currSequence.equals(currentSequence)) {
			lock.lock(this);
			this.currSequence = currentSequence;
			changedCurrentSequenceNoLock();
			this.nextSequence = selectNextSequenceNoLock();
			changedNextSequenceNoLock();
			lock.unlock(this);
		}
	}
	
	public void setNextSequence(String nextSequence) {
		if (nextSequence.length()>0 && !this.nextSequence.equals(nextSequence)) {
			lock.lock(this);
			this.nextSequence = nextSequence;
			changedNextSequenceNoLock();
			lock.unlock(this);
		}
	}
	
	public void setCurrentMix(MixState state) {
		if (state!=null) {
			state = state.copy();
			lock.lock(this);
			currMix = state;
			lock.unlock(this);
			if (MidiSys.sequencer!=null) {
				MidiSys.sequencer.setMixState(state);
			}
		}
	}
	
	public void setNextMix(MixState state) {
		if (state!=null) {
			state = state.copy();
			lock.lock(this);
			nextMix = state;
			lock.unlock(this);
			if (MidiSys.sequencer!=null) {
				MidiSys.sequencer.setNextMixState(state);
			}
		}
	}
	
	public MixState getCurrentMix() {
		MixState r = null;
		lock.lock(this);
		if (currMix!=null) {
			r = currMix.copy();
		}
		lock.unlock(this);
		return r;
	}
	
	public MixState getNextMix() {
		MixState r = null;
		lock.lock(this);
		if (nextMix!=null) {
			r = nextMix.copy();
		}
		lock.unlock(this);
		return r;
	}

	public void startRecording() {
		lock.lock(this);
		if (!recording) {
			recording = true;
			if (!MidiSys.sequencer.isRecording()) {
				MidiSys.sequencer.startRecording();
			}
		}
		lock.unlock(this);
	}

	public void stopRecording() {
		lock.lock(this);
		if (recording) {
			recording = false;
			if (MidiSys.sequencer.isRecording()) {
				MidiSys.sequencer.stopRecording();
			}
		}
		lock.unlock(this);
	}

	public boolean isRecording() {
		lock.lock(this);
		boolean r = recording;
		lock.unlock(this);
		return r;
	}
	
	@Override
	public void handleEvent(Event event) {
		if (event.name.equals(ThemeController.INITIALIZING_THEME) ||
			event.name.equals(ThemeController.LOADING_THEME) ||
			event.name.equals(ThemeController.DESTROYING)
			) {
			lock.lock(this);
			currSequence = "";
			nextSequence = "";
			if (event.name.equals(ThemeController.DESTROYING) && controller!=null) {
				controller.eventPublisher.removeListener(this);
				controller = null;
			}
			lock.unlock(this);
		} else if (event.name.equals(ThemeController.GENERATED_SEQUENCES)) {
			lock.lock(this);
			if ((currSequence.length()==0 || currSequence.equals(NetworkTrainer.TRAINING_SEQUENCE)) && 
				!MidiSys.sequencer.isRunning()
				) {
				List<String> sequences = controller.getSequenceNames();
				sequences.remove(NetworkTrainer.TRAINING_SEQUENCE);
				if (sequences.size()>=1) {
					currSequence = sequences.get(0);
					changedCurrentSequenceNoLock();
					hold = false;
					nextSequence = selectNextSequenceNoLock();
					changedNextSequenceNoLock();
				}
			}
			lock.unlock(this);
		} else if (
			event.name.equals(ThemeController.CHANGED_TRAINING_SEQUENCE) ||
			event.name.equals(ThemeController.GENERATED_SEQUENCE)
			) {
			lock.lock(this);
			if (event.param!=null) {
				if (event.param.toString().equals(currSequence) && !MidiSys.sequencer.isRunning()) {
					changedCurrentSequenceNoLock();
					Logger.dbg(this, new Str("Updated current sequence"));
				}
				if (event.param.toString().equals(nextSequence)) {
					changedNextSequenceNoLock();
					Logger.dbg(this, new Str("Updated next sequence"));
				}
			}
			lock.unlock(this);
		} else if (event.name.equals(ThemeController.CHANGED_SHUFFLE)) {
			lock.lock(this);
			if (currSequence.length()>0) {
				changedCurrentSequenceNoLock();
			}
			if (nextSequence.length()>0) {
				changedNextSequenceNoLock();
			}
			lock.unlock(this);
		}
	}

	@Override
	public void switchedSequence() {
		lock.lock(this);
		switchedSequenceNoLock();
		lock.unlock(this);
	}

	public boolean isHold() {
		lock.lock(this);
		boolean r = hold;
		lock.unlock(this);
		return r;
	}

	public boolean isSelectRandom() {
		lock.lock(this);
		boolean r = selectRandom;
		lock.unlock(this);
		return r;
	}

	public boolean isSelectSelectTrainingSequence() {
		lock.lock(this);
		boolean r = selectTrainingSequence;
		lock.unlock(this);
		return r;
	}

	public boolean isRegenerateOnPlay() {
		lock.lock(this);
		boolean r = regenerateOnPlay;
		lock.unlock(this);
		return r;
	}

	public String getCurrentSequence() {
		lock.lock(this);
		String r = currSequence;
		lock.unlock(this);
		return r;
	}

	public String getNextSequence() {
		lock.lock(this);
		String r = nextSequence;
		lock.unlock(this);
		return r;
	}
	
	public SequencerControl getSequencerControl(int bpm, float shufflePercentage) {
		long recordedTicks = 0;
		MidiSequencer sequencer = MidiSys.sequencer;
		if (sequencer!=null) {
			recordedTicks = MidiSys.sequencer.getRecordedTicks();
		}
		lock.lock(this);
		SequencerControl r = new SequencerControl(
			bpm,
			shufflePercentage,
			recording,
			recordedTicks,
			controller.getSequenceNames(),
			currSequence,
			nextSequence,
			hold,
			selectRandom,
			selectTrainingSequence,
			regenerateOnPlay,
			currMix,
			nextMix
		);
		lock.unlock(this);
		return r;
	}

	protected void startTheme(String startSequence, boolean selectNextSequence) {
		lock.lock(this);
		if (!MidiSys.isInitialized()) {
			Logger.err(this, new Str("Midi system is not initialized"));
		} else if (controller==null) {
			Logger.err(this, new Str("Theme controller has not been set"));
		} else if (!controller.isBusy() && !MidiSys.sequencer.isRunning()) {
			if (startSequence.length()==0) {
				startSequence = currSequence;
			}
			PatternSequence sequence = controller.getSequences().get(startSequence);
			if (sequence!=null) {
				currSequence = startSequence;
				changedCurrentSequenceNoLock();
				MidiSys.sequencer.start();
				if (selectNextSequence) {
					nextSequence = selectNextSequenceNoLock();
				} else {
					hold = true;
					nextSequence = startSequence;
				}
				changedNextSequenceNoLock();
				if (regenerateOnPlay && !startSequence.equals(NetworkTrainer.TRAINING_SEQUENCE)) {
					controller.generateSequence(startSequence).start();
				}
			} else {
				Str err = new Str("Sequence not found: '");
				err.sb().append(startSequence);
				err.sb().append("'");
				Logger.err(this, err);
			}
		}
		lock.unlock(this);
	}
	
	protected void switchedSequenceNoLock() {
		currSequence = nextSequence;
		nextSequence = selectNextSequenceNoLock();
		changedNextSequenceNoLock();
		currMix = nextMix.copy();
		nextMix = currMix.copy();
		if (regenerateOnPlay && !currSequence.equals(NetworkTrainer.TRAINING_SEQUENCE)) {
			controller.generateSequence(currSequence).start();
		}
	}
	
	protected void changedCurrentSequenceNoLock() {
		if (controller!=null && !MidiSys.sequencer.isRunning()) {
			PatternSequence sequence = controller.getSequences().get(currSequence);
			if (sequence!=null) {
				Sequence midiSequence = controller.generateMidiSequence(sequence);
				MidiSys.sequencer.setSequence(midiSequence);
			}
		}
	}
	
	protected void changedNextSequenceNoLock() {
		if (controller!=null) {
			PatternSequence sequence = controller.getSequences().get(nextSequence);
			if (sequence!=null) {
				Sequence midiSequence = controller.generateMidiSequence(sequence);
				MidiSys.sequencer.setNextSequence(midiSequence);
			}
		}
	}
	
	protected String selectNextSequenceNoLock() {
		String r = currSequence;
		if (!hold) {
			List<String> sequenceNames = controller.getSequenceNames();
			if (!selectTrainingSequence) {
				sequenceNames.remove(NetworkTrainer.TRAINING_SEQUENCE);
			}
			if (sequenceNames.size()>1) {
				if (selectRandom) {
					sequenceNames.remove(currSequence);
					r = sequenceNames.get(Rand.getRandomInt(0, sequenceNames.size() - 1));
				} else {
					boolean next = false;
					int nextIdx = 0;
					int i = 0;
					for (String name: sequenceNames) {
						if (next) {
							nextIdx = i;
							break;
						}
						if (name.equals(currSequence)) {
							next = true;
						}
						i++;
					}
					r = sequenceNames.get(nextIdx);
				}
			} else if (sequenceNames.size()==1) {
				r = sequenceNames.get(0);
			}
		}
		return r;
	}
}
