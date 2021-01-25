package nl.zeesoft.zdbd.midi.convertors;

import java.util.ArrayList;
import java.util.List;

public class ArpConvertor extends InstrumentConvertor {
	public float						hold			= 0.5F;
	public List<SoundLayerConvertor>	layers			= new ArrayList<SoundLayerConvertor>();
	
	public List<MidiNote> getMidiNotesForArpeggiatorNote(int note, int duration, boolean accent) {
		List<MidiNote> r = new ArrayList<MidiNote>();
		for (SoundLayerConvertor layer: layers) {
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
	
	public static void applyOctaveNote(List<MidiNote> mns, int octave, int note) {
		for (MidiNote mn: mns) {
			mn.midiNote += (octave * 12) + note;
		}
	}

	@Override
	public List<MidiNote> getMidiNotesForPatternValue(int value) {
		// Not implemented
		return null;
	}
}
