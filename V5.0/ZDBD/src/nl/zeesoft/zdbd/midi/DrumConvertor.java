package nl.zeesoft.zdbd.midi;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdbd.pattern.InstrumentPattern;

public class DrumConvertor extends InstrumentConvertor {
	public static final int				KICK				= 0;
	public static final int				SNARE				= 1;
	public static final int				CLOSED_HIHAT		= 2;
	public static final int				OPEN_HIHAT			= 3;
	public static final int				RIDE				= 4;
	public static final int				CYMBAL				= 5;
	public static final int				BASS				= 6;
	
	public static final String[]		INSTRUMENT_NAMES	= {
		"Kick", "Snare", "ClosedHihat", "OpenHihat", "Ride", "Cymbal", "Bass"
	};
	
	public int							channel = SynthConfig.DRUM_CHANNEL;
	public List<DrumSampleConvertor>	samples	= new ArrayList<DrumSampleConvertor>();
	
	@Override
	public List<MidiNote> getMidiNotesForPatternNote(int note) {
		List<MidiNote> r = new ArrayList<MidiNote>();
		if (note!=InstrumentPattern.OFF) {
			for (DrumSampleConvertor sample: samples) {
				MidiNote mn = new MidiNote();
				mn.channel = channel;
				mn.midiNote = sample.midiNote; 
				if (InstrumentPattern.getAccentForNote(note)) {
					mn.velocity = sample.accentVelocity;
					mn.hold = sample.accentHold;
				} else {
					mn.velocity = sample.velocity;
					mn.hold = sample.hold;
				}
				r.add(mn);
			}
		}
		return r;
	}
}
