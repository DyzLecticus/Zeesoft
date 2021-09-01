package nl.zeesoft.zdk.midi.instrument;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.midi.synth.SynthConfig;

public class Bass extends Instrument {
	public Bass() {
		super(BASS);
	}

	@Override
	public List<Integer> getChannels() {
		List<Integer> r = new ArrayList<Integer>();
		r.add(SynthConfig.BASS_CHANNEL_1);
		r.add(SynthConfig.BASS_CHANNEL_2);
		return r;
	}
}
