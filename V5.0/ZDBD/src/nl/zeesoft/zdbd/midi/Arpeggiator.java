package nl.zeesoft.zdbd.midi;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.Track;

import nl.zeesoft.zdbd.midi.convertors.ArpConvertor;
import nl.zeesoft.zdbd.midi.convertors.MidiNote;
import nl.zeesoft.zdbd.pattern.PatternSequence;
import nl.zeesoft.zdbd.pattern.SequenceChord;
import nl.zeesoft.zdk.Rand;

public class Arpeggiator {
	public int						minDuration		= 1;
	public int						maxDuration		= 2;
	public float					density			= 0.80F;
	public int						maxOctave		= 1;
	public int						maxInterval		= 12;
	public int						maxSteps		= 12;
	
	public Arpeggiator copy() {
		Arpeggiator r = new Arpeggiator();
		r.maxDuration = maxDuration;
		r.density = density;
		r.maxOctave = maxOctave;
		r.maxInterval = maxInterval;
		return r;
	}
	
	public void generateMidiSequenceOnTrack(Track track, PatternSequence sequence, ArpConvertor convertor) {
		long sequenceEndTick = MidiSequenceUtil.getSequenceEndTick(sequence.rythm) * sequence.getSequencedPatterns().size();
		long nextActiveTick = (sequenceEndTick - 1);
		int ticksPerStep = MidiSequenceUtil.getTicksPerStep(sequence.rythm);
		int totalSteps = sequence.getTotalSteps();
		
		int generate = totalSteps;
		if (maxSteps>0 && maxSteps<totalSteps) {
			generate = maxSteps;
		}
		int[] durations = new int[generate];
		boolean[] accents = new boolean[generate];
		for (int s = 0; s < generate; s++) {
			if (s==0 || Rand.getRandomFloat(0, 1)<=density) {
				durations[s] = Rand.getRandomInt(minDuration,maxDuration);
				while (s + durations[s] >= generate) {
					durations[s]--;
				}
				if (durations[s] < minDuration) {
					durations[s] = 0;
				}
			} else {
				durations[s] = 0;
			}
			if (durations[s]>0) {
				if (s==0) {
					accents[s] = true;
				} else {
					accents[s] = Rand.getRandomInt(0,1) == 1;
				}
				s += durations[s] - 1;
			}
		}
		
		int pNote = -1;
		for (int s = 0; s < totalSteps; s++) {
			int duration = durations[s % generate];
			if (duration>0) {
				boolean accent = accents[s % generate];
				SequenceChord chord = sequence.getChordForStep(s,false);
				List<Integer> chordNotes = new ArrayList<Integer>();
				chordNotes.add(chord.baseNote);
				for (int i = 0; i < chord.interval.length; i++) {
					int note = chord.baseNote + chord.interval[i];
					if (note<chord.baseNote+12 && !chordNotes.contains(note)) {
						chordNotes.add(note);
					}
				}
				List<Integer> allNotes = new ArrayList<Integer>();
				for (int c = 0; c <= maxOctave; c++) {
					for (Integer note: chordNotes) {
						int addNote = note + (c * 12);
						int diff = 0;
						if (pNote>-1) {
							if (pNote>addNote) {
								diff = pNote - addNote;
							} else if (pNote<addNote) {
								diff = addNote - pNote;
							}
						}
						if (diff<=maxInterval) {
							allNotes.add(addNote);
						}
					}
				}
				int note = allNotes.get(Rand.getRandomInt(0, allNotes.size()-1));
				List<MidiNote> mns = convertor.getMidiNotesForArpeggiatorNote(note, duration, accent);
				MidiSequenceUtil.addMidiNotesToTrack(mns,track,sequence.rythm,s,nextActiveTick,ticksPerStep);
				pNote = note;
				s += duration - 1;
			}
		}
	}
}
