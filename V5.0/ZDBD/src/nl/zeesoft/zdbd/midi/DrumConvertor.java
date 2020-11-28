package nl.zeesoft.zdbd.midi;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdbd.pattern.InstrumentPattern;
import nl.zeesoft.zdbd.pattern.inst.PatternInstrument;

public class DrumConvertor extends InstrumentConvertor {
	public int							channel = SynthConfig.DRUM_CHANNEL;
	public List<DrumSampleConvertor>	samples	= new ArrayList<DrumSampleConvertor>();
	
	@Override
	public List<MidiNote> getMidiNotesForPatternValue(int value) {
		List<MidiNote> r = new ArrayList<MidiNote>();
		if (value!=PatternInstrument.OFF) {
			for (DrumSampleConvertor sample: samples) {
				MidiNote mn = new MidiNote();
				mn.channel = channel;
				mn.midiNote = sample.midiNote; 
				if (InstrumentPattern.isAccent(value)) {
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
