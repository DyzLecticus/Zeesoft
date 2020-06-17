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
				protected void selectedNextValue(int lfoIndex, float percentage) {
					lfoSelectedNextValue(lfoIndex,percentage);
				}
			};
			state.addListener(lfoGenerators[i]);
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
	
	protected void lfoSelectedNextValue(int lfoIndex, float percentage) {
		lock.lock(this);
		Lfo lfo = lfos[lfoIndex];
		if (lfo!=null) {
			for (int channel = 0; channel < instruments.length; channel++) {
				Inst instrument = instruments[channel];
				if (instrument!=null) {
					for (InstLfoSource lfoSource: instrument.lfoSources) {
						if (lfoSource.lfoIndex==lfoIndex) {
							float max = lfoSource.percentage * 127F;
							int val = (int) (percentage * max);
							int pVal = instrument.getPropertyValue(lfoSource.property);
							if (lfoSource.invert) {
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
							synth.setInstrumentProperty(channel,lfoSource.property,val);
						}
					}
				}
			}
		}
		lock.unlock(this);
	}
}
