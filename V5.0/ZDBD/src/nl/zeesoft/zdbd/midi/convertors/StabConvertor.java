package nl.zeesoft.zdbd.midi.convertors;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdbd.pattern.SequenceChord;

public class StabConvertor extends BassConvertor {
	public static void applyChordNotes(List<MidiNote> mns, int octave, SequenceChord chord, int shift) {
		List<Integer> chordNotes = new ArrayList<Integer>();
		chordNotes.add(chord.baseNote);
		for (int i = 0; i < chord.interval.length; i++) {
			int note = chord.baseNote + chord.interval[i];
			if (note<chord.baseNote+12 && !chordNotes.contains(note)) {
				chordNotes.add(note);
			}
		}
		List<MidiNote> list = new ArrayList<MidiNote>();
		for (MidiNote mn: mns) {
			int i = 0;
			for (Integer note: chordNotes) {
				int mnv = mn.midiNote + note;
				if (i < shift) {
					mnv += 12;
				}
				MidiNote n = new MidiNote();
				n.channel = mn.channel;
				n.hold = mn.hold;
				n.midiNote = mnv;
				n.velocity = mn.velocity;
				list.add(n);
				i++;
			}
		}
		mns.clear();
		mns.addAll(list);
	}
}
