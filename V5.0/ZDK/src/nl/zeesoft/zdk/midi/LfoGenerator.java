package nl.zeesoft.zdk.midi;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.thread.CodeRunner;
import nl.zeesoft.zdk.thread.Lock;
import nl.zeesoft.zdk.thread.RunCode;

public class LfoGenerator {
	private static final int	MIN_VAL		= 0;
	private static final int	MAX_VAL		= 32;
	
	private String				patchName	= "";
	private int					lfoIndex	= 0;
	private Lock				lock		= new Lock();
	private State				state		= null;
	
	private Lfo					lfo			= null;
	
	private CodeRunner			runner		= null;
	
	private int[]				values		= null;
	private int					valueIndex	= 0;
	private int					msPerValue	= 0;
	private int					nsPerValue	= 0;
	
	protected LfoGenerator(Logger logger, State state, String patchName, int lfoIndex) {
		this.state = state;
		this.patchName = patchName;
		this.lfoIndex = lfoIndex;
		lock.setLogger(this, logger);
	}

	protected void setLfo(Lfo lfo) {
		lock.lock(this);
		this.lfo = lfo;
		calculateNoLock();
		lock.unlock(this);
	}
	
	public void stateChanged() {
		lock.lock(this);
		calculateNoLock();
		lock.unlock(this);
	}
	
	public void start() {
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
	
	public void stop() {
		lock.lock(this);
		if (runner!=null) {
			runner.stop();
		}
		lock.unlock(this);
	}
	
	protected void selectNextValue() {
		lock.lock(this);
		if (values!=null && valueIndex>=values.length) {
			valueIndex = 0;
		}
		lock.unlock(this);
	}
	
	protected int getValue() {
		lock.lock(this);
		if (values!=null && valueIndex>=values.length) {
			valueIndex = 0;
		}
		int r = values[valueIndex];
		lock.unlock(this);
		return r;
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
		if (lfo.type.equals(Lfo.LINEAR)) {
			int increments = 64;
			values = new int[increments];
			int value = MIN_VAL;
			boolean increment = true;
			for (int i = 0; i < increments; i++) {
				values[i] = value;
				if (value>=MAX_VAL) {
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
			values[0] = MIN_VAL;
			values[1] = MAX_VAL;
		}
		resetTimePerValue();
	}
	
	private void resetTimePerValue() {
		msPerValue = (state.getMsPerStep() / values.length) * lfo.steps;
		nsPerValue = 0;
		if (msPerValue<100) {
			msPerValue = 0;
			nsPerValue = (state.getNsPerStep() / values.length) * lfo.steps;
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
