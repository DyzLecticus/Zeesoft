package nl.zeesoft.zdbd;

import java.util.List;

import javax.sound.midi.Sequence;

import nl.zeesoft.zdbd.midi.MidiSequencerEventListener;
import nl.zeesoft.zdbd.midi.MidiSys;
import nl.zeesoft.zdbd.neural.NetworkTrainer;
import nl.zeesoft.zdbd.pattern.PatternSequence;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Rand;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.thread.Lock;
import nl.zeesoft.zdk.thread.Waiter;

public class ThemeSequenceSelector implements MidiSequencerEventListener, EventListener {
	private Lock				lock					= new Lock();
	
	private boolean				hold					= true;
	private boolean				selectRandom			= true;
	private boolean				selectTrainingSequence	= true;
	private boolean				regenerateOnPlay		= true;
	
	private String				currSequence			= "";
	private String				nextSequence			= "";
	
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

	public void startSequence(String name) {
		startTheme(name,false);
	}
	
	public void startTheme(String startSequence) {
		startTheme(startSequence,true);
	}
	
	public void setNextSequence(String nextSequence) {
		if (nextSequence.length()>0 && !this.nextSequence.equals(nextSequence)) {
			lock.lock(this);
			this.nextSequence = nextSequence;
			changedNextSequenceNoLock();
			lock.unlock(this);
		}
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
		} else if (
			event.name.equals(ThemeController.CHANGED_TRAINING_SEQUENCE) ||
			event.name.equals(ThemeController.GENERATED_SEQUENCE)
			) {
			lock.lock(this);
			if (event.param!=null && event.param.toString().equals(nextSequence)) {
				changedNextSequenceNoLock();
				Logger.dbg(this, new Str("Updated next sequence"));
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

	protected void startTheme(String startSequence, boolean selectNextSequence) {
		lock.lock(this);
		if (!MidiSys.isInitialized()) {
			Logger.err(this, new Str("Midi system is not initialized"));
		} else if (controller==null) {
			Logger.err(this, new Str("Theme controller has not been set"));
		} else if (!controller.isBusy()) {
			PatternSequence sequence = controller.getSequences().get(startSequence);
			if (sequence!=null) {
				this.currSequence = startSequence;
				Sequence midiSequence = controller.generateMidiSequence(sequence);
				if (MidiSys.sequencer.isRunning()) {
					MidiSys.sequencer.stop();
					Waiter.waitFor(MidiSys.sequencer,100);
				}
				MidiSys.sequencer.setSequence(midiSequence);
				MidiSys.sequencer.start();
				if (selectNextSequence) {
					this.nextSequence = selectNextSequenceNoLock();
				} else {
					this.nextSequence = startSequence;
				}
				this.changedNextSequenceNoLock();
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
		if (regenerateOnPlay && !currSequence.equals(NetworkTrainer.TRAINING_SEQUENCE)) {
			controller.generateSequence(currSequence).start();
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
			sequenceNames.remove(currSequence);
			if (!selectTrainingSequence) {
				sequenceNames.remove(NetworkTrainer.TRAINING_SEQUENCE);
			}
			if (sequenceNames.size()>1) {
				if (selectRandom) {
					r = sequenceNames.get(Rand.getRandomInt(0, sequenceNames.size() - 1));
				} else {
					int currIdx = -1;
					int nextIdx = 0;
					int i = 0;
					for (String name: sequenceNames) {
						if (currIdx>=0 && i>currIdx) {
							nextIdx = i;
							break;
						}
						if (name.equals(currSequence)) {
							currIdx = i;
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
