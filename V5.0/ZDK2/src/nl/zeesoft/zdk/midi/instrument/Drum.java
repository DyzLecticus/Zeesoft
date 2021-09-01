package nl.zeesoft.zdk.midi.instrument;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.midi.synth.SynthConfig;

public class Drum extends Instrument {
	public Drum() {
		super(DRUM);
	}

	@Override
	public List<Integer> getChannels() {
		List<Integer> r = new ArrayList<Integer>();
		r.add(SynthConfig.DRUM_CHANNEL);
		return r;
	}
}
