package nl.zeesoft.zdbd.midi;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdbd.pattern.DrumAndBassPattern;

public class DrumConvertor extends InstrumentConvertor {
	public int							channel = SynthConfig.DRUM_CHANNEL;
	public List<DrumSampleConvertor>	samples	= new ArrayList<DrumSampleConvertor>();
	
	@Override
	public List<MidiNote> getMidiNotesForPatternNote(int note) {
		List<MidiNote> r = new ArrayList<MidiNote>();
		if (note!=DrumAndBassPattern.OFF) {
			for (DrumSampleConvertor sample: samples) {
				MidiNote mn = new MidiNote();
				mn.channel = channel;
				mn.midiNote = sample.midiNote; 
				if (note==DrumAndBassPattern.ACCENT) {
					mn.velocity = sample.accentVelocity;
					mn.hold = sample.accentHold;
				} else if (note==DrumAndBassPattern.ON) {
					mn.velocity = sample.velocity;
					mn.hold = sample.hold;
				}
				r.add(mn);
			}
		}
		return r;
	}
}
