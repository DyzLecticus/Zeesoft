package nl.zeesoft.zdk.midi;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.thread.Lock;

public class State {
	private Lock			lock			= new Lock();
	
	private	int				beatsPerMinute	= 120;
	private int				stepsPerBeat	= 4;
	
	protected State(Logger logger) {
		lock.setLogger(this, logger);
	}

	public int getBeatsPerMinute() {
		lock.lock(this);
		int r = beatsPerMinute;
		lock.unlock(this);
		return r;
	}

	public void setBeatsPerMinute(int beatsPerMinute) {
		lock.lock(this);
		this.beatsPerMinute = beatsPerMinute;
		lock.unlock(this);
	}

	public int getStepsPerBeat() {
		lock.lock(this);
		int r = stepsPerBeat;
		lock.unlock(this);
		return r;
	}

	public void setStepsPerBeat(int stepsPerBeat) {
		lock.lock(this);
		this.stepsPerBeat = stepsPerBeat;
		lock.unlock(this);
	}
	
	public int getMsPerStep() {
		lock.lock(this);
		int bpm = beatsPerMinute;
		int spb = stepsPerBeat;
		lock.unlock(this);
		return (60000 / bpm) / spb;
	}
	
	public int getNsPerStep() {
		lock.lock(this);
		float msPerBeat = (60000F / (float)beatsPerMinute);
		int nsPerStep = (int)((1000000F * msPerBeat) / (float)stepsPerBeat);
		lock.unlock(this);
		return nsPerStep;
	}

}
