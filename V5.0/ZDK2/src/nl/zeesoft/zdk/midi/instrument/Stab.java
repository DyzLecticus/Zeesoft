package nl.zeesoft.zdk.midi.instrument;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.midi.synth.SynthConfig;

public class Stab extends Instrument {
	public Stab() {
		super(STAB);
	}

	@Override
	public List<Integer> getChannels() {
		List<Integer> r = new ArrayList<Integer>();
		r.add(SynthConfig.STAB_CHANNEL);
		return r;
	}
}
