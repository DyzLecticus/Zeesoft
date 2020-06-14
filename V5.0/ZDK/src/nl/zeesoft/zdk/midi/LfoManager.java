package nl.zeesoft.zdk.midi;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.thread.CodeRunner;
import nl.zeesoft.zdk.thread.Lock;

public class LfoManager {
	private Lock						lock			= new Lock();
	private Synth						synth			= null;

	private Lfo[]						lfos			= new Lfo[16];
	private LfoGenerator[]				lfoGenerators	= new LfoGenerator[16];
	private Inst[]						instruments		= new Inst[16];
	
	protected LfoManager(Logger logger, Synth synth, State state) {
		this.synth = synth;
		lock.setLogger(this, logger);
		for (int i = 0; i < lfoGenerators.length; i++) {
			lfoGenerators[i] = new LfoGenerator(logger,state,i) {
				@Override
				protected void selectedNextValue(int lfoIndex, int value) {
					lfoSelectedNextValue(lfoIndex,value);
				}
			};
		}
	}
	
	public void setLfo(int index, Lfo lfo) {
		lock.lock(this);
		if (index>=0 && index<16) {
			lfo = lfo.copy();
			lfos[index] = lfo;
			lfoGenerators[index].setLfo(lfo);
		}
		lock.unlock(this);
	}
	
	public void unsetLfo(int index) {
		lock.lock(this);
		if (index>=0 && index<16 && lfos[index]!=null) {
			lfos[index] = null;
			lfoGenerators[index].setLfo(null);
		}
		lock.unlock(this);
	}
	
	public void start() {
		for (int i = 0; i < lfoGenerators.length; i++) {
			lfoGenerators[i].start();
		}
	}
	
	public void stop() {
		for (int i = 0; i < lfoGenerators.length; i++) {
			lfoGenerators[i].stop();
		}
	}
	
	public List<CodeRunner> getRunners() {
		List<CodeRunner> r = new ArrayList<CodeRunner>();
		for (int i = 0; i < lfoGenerators.length; i++) {
			CodeRunner runner = lfoGenerators[i].getRunner();
			if (runner!=null) {
				r.add(runner);
			}
		}
		return r;
	}
	
	protected void setInstrument(int channel, Inst instrument) {
		lock.lock(this);
		instruments[channel] = instrument;
		lock.unlock(this);
	}
	
	protected void lfoSelectedNextValue(int lfoIndex, int value) {
		lock.lock(this);
		Lfo lfo = lfos[lfoIndex];
		if (lfo!=null) {
			float percentage = 0;
			if (value>0) {
				percentage = (float)value / (float)LfoGenerator.MAX_VALUE;
			}
			for (LfoTarget target: lfo.targets) {
				Inst instrument = instruments[target.channel];
				if (instrument!=null) {
					float max = ((float)target.percentage / 100F) * 127F;
					int val = (int) (percentage * max);
					int pVal = instrument.getPropertyValue(target.property);
					if (target.invert) {
						val = pVal - val;
						if (val < 0) {
							val = 0;
						}
					} else {
						val = pVal + val;
						if (val > 127) {
							val = 127;
						}
					}
					synth.setInstrumentProperty(target.channel,target.property,val);
				}
			}
		}
		lock.unlock(this);
	}
}
