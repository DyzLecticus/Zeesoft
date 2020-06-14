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
	private long				actionTime	= -1;
	private long				nsPerValue	= 100000;
	
	protected LfoGenerator(Logger logger, State state, int lfoIndex) {
		this.state = state;
		this.lfoIndex = lfoIndex;
		lock.setLogger(this, logger);
	}

	protected void setLfo(Lfo lfo) {
		lock.lock(this);
		this.lfo = lfo;
		calculateNoLock();
		lock.unlock(this);
	}
	
	protected void stateChanged() {
		lock.lock(this);
		calculateNoLock();
		lock.unlock(this);
	}
	
	protected void start() {
		lock.lock(this);
		if (runner==null) {
			runner = new CodeRunner(getCodeForRunner());
			runner.setSleepNs(10000);
		}
		actionTime = System.nanoTime();
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
			long now = System.nanoTime();
			if (now>=actionTime) {
				while(now>=actionTime) {
					actionTime += nsPerValue;
				}
				valueIndex++;
				if (values!=null) { 
					if (valueIndex>=values.length) {
						valueIndex = 0;
					}
					value = values[valueIndex];
				}
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
	
	private void calculateNoLock() {
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
		if (lfo!=null && values!=null) {
			nsPerValue = (state.getNsPerStep() / (long)values.length) * (long)lfo.steps;
		} else {
			nsPerValue = 100000;
		}
	}
}
