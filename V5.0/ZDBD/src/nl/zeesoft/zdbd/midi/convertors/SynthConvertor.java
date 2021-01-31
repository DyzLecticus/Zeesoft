package nl.zeesoft.zdbd.midi.convertors;

import java.util.ArrayList;
import java.util.List;

public class SynthConvertor extends InstrumentConvertor {
	public float						hold			= 0.75F;
	public List<SynthLayerConvertor>	layers			= new ArrayList<SynthLayerConvertor>();
	
	public List<MidiNote> getMidiNotesForArpeggiatorNote(int note, int duration, boolean accent) {
		List<MidiNote> r = new ArrayList<MidiNote>();
		for (SynthLayerConvertor layer: layers) {
			MidiNote mn = new MidiNote();
			mn.channel = layer.channel;
			mn.midiNote = (layer.baseOctave * 12) + note;
			mn.hold = ((float)(duration - 1)) + hold;
			if (accent) {
				mn.velocity = layer.accentVelocity;
			} else {
				mn.velocity = layer.velocity;
			}
			r.add(mn);
		}
		return r;
	}

	@Override
	public List<MidiNote> getMidiNotesForPatternValue(int value) {
		// Not implemented
		return null;
	}
}
