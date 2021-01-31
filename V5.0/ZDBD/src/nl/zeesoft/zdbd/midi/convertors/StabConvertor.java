package nl.zeesoft.zdbd.midi.convertors;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdbd.pattern.SequenceChord;

public class StabConvertor extends BassConvertor {
	public static void applyChordNotes(List<MidiNote> mns, int octave, SequenceChord chord) {
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
			for (Integer note: chordNotes) {
				MidiNote n = new MidiNote();
				n.channel = mn.channel;
				n.hold = mn.hold;
				n.midiNote = mn.midiNote + note;
				n.velocity = mn.velocity;
				list.add(n);
			}
		}
		mns.clear();
		mns.addAll(list);
	}
}
