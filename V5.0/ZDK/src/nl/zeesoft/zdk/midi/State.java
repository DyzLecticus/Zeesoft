package nl.zeesoft.zdk.midi;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.thread.Lock;

public class State {
	public static final String	BEATS_PER_MINUTE	= "BPM";
	public static final String	STEPS_PER_BEAT		= "SPB";
	
	private Lock				lock				= new Lock();
	private Logger				logger				= null;
	
	private	int					beatsPerMinute		= 120;
	private int					stepsPerBeat		= 4;
	private List<StateListener>	listeners			= new ArrayList<StateListener>();
	
	protected State(Logger logger) {
		this.logger = logger;
		lock.setLogger(this, logger);
	}

	public void addListener(StateListener listener) {
		lock.lock(this);
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
		lock.unlock(this);
	}

	public int getBeatsPerMinute() {
		lock.lock(this);
		int r = beatsPerMinute;
		lock.unlock(this);
		return r;
	}

	public void setBeatsPerMinute(int beatsPerMinute) {
		boolean changed = false;
		lock.lock(this);
		if (this.beatsPerMinute != beatsPerMinute) {
			this.beatsPerMinute = beatsPerMinute;
			changed = true;
		}
		lock.unlock(this);
		if (changed) {
			notifyListeners(BEATS_PER_MINUTE);
		}
	}

	public int getStepsPerBeat() {
		lock.lock(this);
		int r = stepsPerBeat;
		lock.unlock(this);
		return r;
	}

	public void setStepsPerBeat(int stepsPerBeat) {
		boolean changed = false;
		lock.lock(this);
		if (this.stepsPerBeat != stepsPerBeat) {
			this.stepsPerBeat = stepsPerBeat;
			changed = true;
		}
		lock.unlock(this);
		if (changed) {
			notifyListeners(STEPS_PER_BEAT);
		}
	}
	
	public int getMsPerStep() {
		lock.lock(this);
		int bpm = beatsPerMinute;
		int spb = stepsPerBeat;
		lock.unlock(this);
		return (60000 / bpm) / spb;
	}
	
	public long getNsPerStep() {
		lock.lock(this);
		float msPerBeat = (60000F / (float)beatsPerMinute);
		long nsPerStep = (long)((1000000F * msPerBeat) / (float)stepsPerBeat);
		lock.unlock(this);
		return nsPerStep;
	}
	
	protected void notifyListeners(String property) {
		lock.lock(this);
		List<StateListener> list = new ArrayList<StateListener>(listeners); 
		lock.unlock(this);
		for (StateListener listener: list) {
			try {
				listener.stateChanged(property);
			} catch (Exception ex) {
				logger.error(this,new Str("Caught exception while notifying listeners of state change"), ex);
			}
		}
	}

}
