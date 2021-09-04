package nl.zeesoft.zdk.midi.instrument;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.midi.synth.SynthConfig;

public class Bass extends Instrument {
	public Bass() {
		super(BASS);
		sounds.add(new InstrumentChannelSound(SynthConfig.BASS_CHANNEL_1, BASS, 3, 0, 0.75F, 120, 1.75F, 120));
		sounds.add(new InstrumentChannelSound(SynthConfig.BASS_CHANNEL_2, BASS, 3, 0, 0.75F, 70, 1.75F, 70));
	}

	@Override
	public List<Integer> getChannels() {
		List<Integer> r = new ArrayList<Integer>();
		r.add(SynthConfig.BASS_CHANNEL_1);
		r.add(SynthConfig.BASS_CHANNEL_2);
		return r;
	}
}
