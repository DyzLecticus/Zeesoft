package nl.zeesoft.zdk.midi.instrument;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.midi.pattern.DrumChordPattern;
import nl.zeesoft.zdk.midi.synth.SynthConfig;

public class Drum extends Instrument {
	public static final String	KICK			= "Kick";
	public static final String	SNARE			= "Snare";
	public static final String	CLOSED_HIHAT	= "ClosedHihat";
	public static final String	OPEN_HIHAT		= "OpenHihat";
	
	public Drum() {
		super(DRUM);
		chordPattern = new DrumChordPattern();
		for (Integer channel: getChannels()) {
			sounds.add(new InstrumentChannelSound(channel, KICK, 2, 0, 0.75F, 110, 1.5F, 127));
			sounds.add(new InstrumentChannelSound(channel, SNARE, 2, 1, 0.75F, 100, 1.5F, 110));
			sounds.add(new InstrumentChannelSound(channel, CLOSED_HIHAT, 2, 2, 0.25F, 70, 0.5F, 70));
			sounds.add(new InstrumentChannelSound(channel, OPEN_HIHAT, 2, 3, 0.5F, 80, 0.75F, 80));
		}
	}

	@Override
	public List<Integer> getChannels() {
		List<Integer> r = new ArrayList<Integer>();
		r.add(SynthConfig.DRUM_CHANNEL);
		return r;
	}
}
