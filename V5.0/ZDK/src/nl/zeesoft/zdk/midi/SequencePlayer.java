package nl.zeesoft.zdk.midi;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.thread.Lock;

public class SequencePlayer implements MetaEventListener {
	private static final int	END_OF_SEQUENCE		= 47;
	private Lock				lock				= new Lock();
	private Sequencer			sequencer			= null;
	
	private Sequence			sequence			= null;
	
	protected SequencePlayer(Logger logger,Sequencer sequencer) {
		this.sequencer = sequencer;
		lock.setLogger(this, logger);
		sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
	}
	
	protected Sequencer getSequencer() {
		return sequencer;
	}
	
	public void setSequence(Sequence sequence) {
		lock.lock(this);
		this.sequence = sequence;
		updateSequenceNoLock();
		lock.unlock(this);
	}
	
	public void start() {
		lock.lock(this);
		if (!sequencer.isRunning()) {
			sequencer.start();
		}
		lock.unlock(this);
	}
	
	public void stop() {
		lock.lock(this);
		if (sequencer.isRunning()) {
			sequencer.stop();
		}
		lock.unlock(this);
	}
	
	public boolean isRunning() {
		lock.lock(this);
		boolean r = sequencer.isRunning();
		lock.unlock(this);
		return r;
	}

	@Override
	public void meta(MetaMessage message) {
		if (message.getType()==END_OF_SEQUENCE) {
			lock.lock(this);
			if (sequencer!=null && sequencer.isOpen()) {
				sequencer.setTickPosition(0);
				sequencer.start();
			}
			lock.unlock(this);
		}
	}
	
	private void updateSequenceNoLock() {
		boolean restart = false;
		if (sequencer.isRunning()) {
			sequencer.stop();
			sequencer.setTickPosition(0);
			restart = true;
		}
		try {
			sequencer.setSequence(sequence);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
		if (restart) {
			sequencer.start();
		}
	}
}
