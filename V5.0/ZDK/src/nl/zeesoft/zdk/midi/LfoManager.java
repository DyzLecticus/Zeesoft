package nl.zeesoft.zdk.midi;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.thread.CodeRunner;
import nl.zeesoft.zdk.thread.Lock;

public class LfoManager {
	private Lock						lock			= new Lock();
	private Synth						synth			= null;

	private Lfo[]						lfos			= new Lfo[16];
	private LfoGenerator[]				lfoGenerators	= new LfoGenerator[16];
	private Inst[]						instruments		= new Inst[16];
	
	protected LfoManager(Synth synth, StateManager state) {
		this.synth = synth;
		for (int i = 0; i < lfoGenerators.length; i++) {
			lfoGenerators[i] = new LfoGenerator(state,i) {
				@Override
				protected void selectedNextValue(int lfoIndex, float percentage) {
					lfoSelectedNextValue(lfoIndex,percentage);
				}
			};
			state.addListener(lfoGenerators[i]);
		}
	}
	
	public void initializeDefaultLfos() {
		setLfo(0,new Lfo());
		setLfo(1,new Lfo(Lfo.SINE, 3));
		setLfo(2,new Lfo(Lfo.SINE, 7));
		setLfo(3,new Lfo(Lfo.BINARY, 2));
		setLfo(3,new Lfo(Lfo.BINARY, 4));
		setLfo(4,new Lfo(Lfo.SAW, 8));
		setLfo(5,new Lfo(Lfo.LINEAR, 16));
		setLfo(6,new Lfo(Lfo.SINE, 24));
		setLfo(7,new Lfo(Lfo.LINEAR, 32));
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
	
	public void reset() {
		for (int i = 0; i < lfoGenerators.length; i++) {
			lfoGenerators[i].reset();
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
	
	protected void setInstrumentPropertyValue(int channel, String property, int value) {
		lock.lock(this);
		if (instruments[channel]!=null) {
			instruments[channel].setPropertyValue(property, value);
		}
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
