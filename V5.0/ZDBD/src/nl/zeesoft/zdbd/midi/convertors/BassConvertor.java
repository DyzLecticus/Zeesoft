package nl.zeesoft.zdbd.midi.convertors;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdbd.pattern.InstrumentPattern;
import nl.zeesoft.zdbd.pattern.instruments.PatternInstrument;

public class BassConvertor extends InstrumentConvertor {
	public int							midiNote		= 3;
	public float						hold			= 0.9F;
	public List<BassLayerConvertor>		layers			= new ArrayList<BassLayerConvertor>();
	
	@Override
	public List<MidiNote> getMidiNotesForPatternValue(int value) {
		List<MidiNote> r = new ArrayList<MidiNote>();
		if (value!=PatternInstrument.OFF) {
			for (BassLayerConvertor layer: layers) {
				MidiNote mn = new MidiNote();
				mn.channel = layer.channel;
				mn.midiNote = (layer.baseOctave * 12) + midiNote;
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
}
