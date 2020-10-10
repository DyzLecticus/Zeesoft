package nl.zeesoft.zdk.midi;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.thread.Lock;

public class StateManager {
	private Lock						lock					= new Lock();
	
	private State						state					= new State();
	private List<StateChangeListener>	listeners				= new ArrayList<StateChangeListener>();

	public void addListener(StateChangeListener listener) {
		lock.lock(this);
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
		lock.unlock(this);
	}
	
	public void setState(State state) {
		lock.lock(this);
		this.state = state.copy();
		State copy = state.copy();
		lock.unlock(this);
		notifyListeners(copy);
	}
	
	public State getState() {
		lock.lock(this);
		State r = state.copy();
		lock.unlock(this);
		return r;
	}
	
	public int getMsPerStep() {
		lock.lock(this);
		int bpm = state.beatsPerMinute;
		int spb = state.stepsPerBeat;
		lock.unlock(this);
		return (60000 / bpm) / spb;
	}
	
	public long getNsPerStep() {
		lock.lock(this);
		float msPerBeat = (60000F / (float)state.beatsPerMinute);
		long nsPerStep = (long)((1000000F * msPerBeat) / (float)state.stepsPerBeat);
		lock.unlock(this);
		return nsPerStep;
	}
	
	protected void notifyListeners(State state) {
		lock.lock(this);
		List<StateChangeListener> list = new ArrayList<StateChangeListener>(listeners); 
		lock.unlock(this);
		for (StateChangeListener listener: list) {
			try {
				listener.stateChanged(state);
			} catch (Exception ex) {
				Logger.err(this,new Str("Caught exception while notifying listeners of state change"), ex);
			}
		}
	}

}
