package nl.zeesoft.zdbd.midi.convertors;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdbd.pattern.InstrumentPattern;
import nl.zeesoft.zdbd.pattern.instruments.PatternInstrument;

public class BassConvertor extends InstrumentConvertor {
	public float						hold			= 0.9F;
	public List<SynthLayerConvertor>	layers			= new ArrayList<SynthLayerConvertor>();
	
	@Override
	public List<MidiNote> getMidiNotesForPatternValue(int value) {
		List<MidiNote> r = new ArrayList<MidiNote>();
		if (value!=PatternInstrument.OFF) {
			for (SynthLayerConvertor layer: layers) {
				MidiNote mn = new MidiNote();
				mn.channel = layer.channel;
				mn.midiNote = (layer.baseOctave * 12);
				mn.hold = ((float)(InstrumentPattern.getDurationForValue(value) - 1)) + hold;
				if (InstrumentPattern.isAccent(value)) {
					mn.velocity = layer.accentVelocity;
				} else {
					mn.velocity = layer.velocity;
				}
				r.add(mn);
			}
		}
		return r;
	}
	
	public static void applyOctaveNote(List<MidiNote> mns, int octave, int note) {
		for (MidiNote mn: mns) {
			mn.midiNote += (octave * 12) + note;
		}
	}
}
