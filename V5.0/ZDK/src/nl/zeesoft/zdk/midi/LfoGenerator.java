package nl.zeesoft.zdk.midi;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.thread.CodeRunner;
import nl.zeesoft.zdk.thread.Lock;
import nl.zeesoft.zdk.thread.RunCode;

public class LfoGenerator {
	protected static final int	MIN_VALUE	= 0;
	protected static final int	MAX_VALUE	= 64;
	
	private Lock				lock		= new Lock();
	private State				state		= null;
	private int					lfoIndex	= 0;
	
	private Lfo					lfo			= null;
	
	private CodeRunner			runner		= null;
	
	private int[]				values		= null;
	private int					valueIndex	= -1;
	private int					msPerValue	= 10;
	private int					nsPerValue	= 0;
	
	// TODO: Refactor to improve accuracy and flexibility
	
	protected LfoGenerator(Logger logger, State state, int lfoIndex) {
		this.state = state;
		this.lfoIndex = lfoIndex;
		lock.setLogger(this, logger);
	}

	protected void setLfo(Lfo lfo) {
		lock.lock(this);
		this.lfo = lfo;
		calculateValuesNoLock();
		lock.unlock(this);
	}
	
	protected void stateChanged() {
		lock.lock(this);
		calculateValuesNoLock();
		lock.unlock(this);
	}
	
	protected void start() {
		lock.lock(this);
		if (runner==null) {
			runner = new CodeRunner(getCodeForRunner());
			if (msPerValue>0) {
				runner.setSleepMs(msPerValue);
			} else {
				runner.setSleepNs(nsPerValue);
			}
		}
		runner.start();
		lock.unlock(this);
	}
	
	protected void stop() {
		lock.lock(this);
		if (runner!=null) {
			runner.stop();
		}
		lock.unlock(this);
	}
	
	protected CodeRunner getRunner() {
		lock.lock(this);
		CodeRunner r = runner;
		lock.unlock(this);
		return r;
	}
	
	protected void selectNextValue() {
		int value = -1;
		lock.lock(this);
		int index = lfoIndex;
		if (lfo!=null) {
			valueIndex++;
			if (values!=null) { 
				if (valueIndex>=values.length) {
					valueIndex = 0;
				}
				value = values[valueIndex];
			}
		} else {
			valueIndex = -1;
		}
		lock.unlock(this);
		if (value>=0) {
			selectedNextValue(index,value);
		}
	}
	
	protected void selectedNextValue(int lfoIndex,int value) {
		// Override to implement
	}
	
	private RunCode getCodeForRunner() {
		return new RunCode() {
			@Override
			protected boolean run() {
				selectNextValue();
				return false;
			}
		};
	}
	
	private void calculateValuesNoLock() {
		if (lfo!=null) {
			if (lfo.type.equals(Lfo.LINEAR)) {
				int increments = MAX_VALUE * 2;
				values = new int[increments];
				int value = MIN_VALUE;
				boolean increment = true;
				for (int i = 0; i < increments; i++) {
					values[i] = value;
					if (value>=MAX_VALUE) {
						increment = false;
					}
					if (increment) {
						value += 1;
					} else {
						value -= 1;
					}
				}
			} else if (lfo.type.equals(Lfo.BINARY)) {
				values = new int[2];
				values[0] = MIN_VALUE;
				values[1] = MAX_VALUE;
			}
		}
		resetTimePerValue();
	}
	
	private void resetTimePerValue() {
		if (lfo!=null) {
			nsPerValue = (state.getNsPerStep() / values.length) * lfo.steps;
			msPerValue = 0;
			if (nsPerValue>=1000000) {
				msPerValue = (state.getMsPerStep() / values.length) * lfo.steps;
				if (msPerValue == 0) {
					msPerValue = 1;
				}
				nsPerValue = 0;
			}
		} else {
			msPerValue = 10;
			nsPerValue = 0;
		}
		if (runner!=null) {
			if (msPerValue>0) {
				runner.setSleepMs(msPerValue);
			} else {
				runner.setSleepNs(nsPerValue);
			}
		}
	}
}
