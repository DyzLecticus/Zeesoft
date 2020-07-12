package nl.zeesoft.zdk.midi;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.thread.CodeRunner;
import nl.zeesoft.zdk.thread.Lock;
import nl.zeesoft.zdk.thread.RunCode;

public class LfoGenerator implements StateChangeListener {
	private static final int	MIN_VALUE		= 0;
	private static final int	MAX_VALUE		= 64;
	
	private Lock				lock			= new Lock();
	private StateManager		stateManager	= null;
	private int					lfoIndex		= 0;
	
	private Lfo					lfo				= null;
	
	private CodeRunner			runner			= null;
	
	private int[]				values			= null;
	private int					valueIndex		= -1;
	private long				actionTime		= -1;
	private long				nsPerValue		= 100000;
	
	protected LfoGenerator(Logger logger, StateManager stateManager, int lfoIndex) {
		this.stateManager = stateManager;
		this.lfoIndex = lfoIndex;
		lock.setLogger(this, logger);
		calculateNsPerValueNoLock();
	}

	protected void setLfo(Lfo lfo) {
		lock.lock(this);
		this.lfo = lfo;
		calculateValuesNoLock();
		calculateNsPerValueNoLock();
		lock.unlock(this);
	}
	
	@Override
	public void stateChanged(State state) {
		lock.lock(this);
		calculateNsPerValueNoLock();
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
	
	protected void reset() {
		lock.lock(this);
		valueIndex = -1;
		lock.unlock(this);
	}
	
	protected void selectNextValue() {
		int value = -1;
		float percentage = 0;
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
					if (value>0) {
						percentage = (float)value / (float)MAX_VALUE;
					}
				}
			}
		} else {
			valueIndex = -1;
		}
		lock.unlock(this);
		if (value>=0) {
			selectedNextValue(index,percentage);
		}
	}
	
	protected void selectedNextValue(int lfoIndex,float percentage) {
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
			if (lfo.type.equals(Lfo.LINEAR) ||
				lfo.type.equals(Lfo.SAW)) {
				int increments = MAX_VALUE;
				if (lfo.type.equals(Lfo.LINEAR)) {
					increments = increments * 2;
				}
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
			} else if (lfo.type.equals(Lfo.SINE)) {
				int increments = MAX_VALUE * 2;
				values = new int[increments];
				double value = MIN_VALUE;
				for (int i = 0; i < increments; i++) {
					double r = (value / (double)MAX_VALUE) * 360D;
					double sin = Math.sin(Math.toRadians(r));
					int base = (MAX_VALUE / 2);
					int add = (int)(sin * (double)base);
					if (add>=0) {
						values[i] = base + add;
					} else {
						values[i] = base - (add * -1);
					}
					value += 0.5D;
				}
			} else if (lfo.type.equals(Lfo.BINARY)) {
				values = new int[2];
				values[0] = MIN_VALUE;
				values[1] = MAX_VALUE;
			}
		}
	}
	
	private void calculateNsPerValueNoLock() {
		if (lfo!=null && values!=null) {
			nsPerValue = (stateManager.getNsPerStep() / (long)values.length) * (long)lfo.steps;
		} else {
			nsPerValue = 100000;
		}
	}
}
