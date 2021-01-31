package nl.zeesoft.zdbd.midi.convertors;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;

import nl.zeesoft.zdbd.midi.Arpeggiator;
import nl.zeesoft.zdbd.midi.MidiSequenceUtil;
import nl.zeesoft.zdbd.pattern.InstrumentPattern;
import nl.zeesoft.zdbd.pattern.PatternSequence;
import nl.zeesoft.zdbd.pattern.Rythm;
import nl.zeesoft.zdbd.pattern.SequenceChord;
import nl.zeesoft.zdbd.pattern.instruments.Bass;
import nl.zeesoft.zdbd.pattern.instruments.Hihat;
import nl.zeesoft.zdbd.pattern.instruments.Note;
import nl.zeesoft.zdbd.pattern.instruments.Octave;
import nl.zeesoft.zdbd.pattern.instruments.PatternInstrument;
import nl.zeesoft.zdk.thread.Lock;

public class PatternSequenceConvertor {
	protected Lock					lock				= new Lock();
	
	protected InstrumentConvertors	convertors			= new InstrumentConvertors();
	
	public static List<String> getTrackNames() {
		List<String> r = new ArrayList<String>();
		for (PatternInstrument inst: InstrumentPattern.INSTRUMENTS) {
			if (!inst.name().equals(Octave.NAME) &&
				!inst.name().equals(Note.NAME)
				) {
				r.add(inst.name());
			}
		}
		r.add(Arpeggiator.class.getSimpleName());
		return r;
	}
	
	public static int getTrackNum(String name) {
		return getTrackNames().indexOf(name);
	}
	
	public Sequence generateSequenceForPatternSequence(PatternSequence sequence,Arpeggiator arp) {
		Sequence r = createSequence();
		if (r!=null) {
			lock.lock(this);
			List<InstrumentPattern> patterns = sequence.getSequencedPatterns();
			long startTick = 0;
			for (InstrumentPattern pattern: patterns) {
				Sequence seq = generateNoteSequenceForPattern(pattern,sequence.rythm);
				for (int t = 0; t < seq.getTracks().length; t++) {
					Track track = seq.getTracks()[t];
					for (int e = 0; e < track.size(); e++) {
						MidiEvent event = track.get(e);
						event.setTick(event.getTick() + startTick);
						r.getTracks()[t].add(event);
					}
				}
				startTick += MidiSequenceUtil.getSequenceEndTick(sequence.rythm);
			}
			if (arp!=null) {
				String name = Arpeggiator.class.getSimpleName();
				Track track = r.getTracks()[getTrackNum(name)];
				SynthConvertor convertor = (SynthConvertor) convertors.get(name);
				arp.generateMidiSequenceOnTrack(track, sequence, convertor);
			}
			lock.unlock(this);
		}
		return r;
	}
	
	public Sequence generateSequenceForPattern(InstrumentPattern pattern, Rythm rythm) {
		lock.lock(this);
		Sequence r = generateNoteSequenceForPattern(pattern,rythm);
		lock.unlock(this);
		return r;
	}
	
	protected Sequence generateNoteSequenceForPattern(InstrumentPattern pattern, Rythm rythm) {
		Sequence r = createSequence();
		addNotesToSequence(r,pattern,rythm);
		MidiSequenceUtil.alignTrackEndings(r,rythm);
		return r;
	}
	
	protected Sequence createSequence() {
		return MidiSequenceUtil.createSequence(getTrackNames().size());
	}
	
	protected void addNotesToSequence(Sequence sequence, InstrumentPattern pattern, Rythm rythm) {
		long sequenceEndTick = MidiSequenceUtil.getSequenceEndTick(rythm);
		int ticksPerStep = MidiSequenceUtil.getTicksPerStep(rythm);
		int stepsPerPattern = rythm.getStepsPerPattern();
		for (PatternInstrument inst: pattern.instruments) {
			InstrumentConvertor convertor1 = null;
			InstrumentConvertor convertor2 = null;
			if (inst.name().equals(Hihat.NAME)) {
				convertor1 = convertors.get(InstrumentConvertors.getInstrumentName(InstrumentConvertors.CLOSED_HIHAT));
				convertor2 = convertors.get(InstrumentConvertors.getInstrumentName(InstrumentConvertors.OPEN_HIHAT));
			} else {
				convertor1 = convertors.get(inst.name());
			}
			if (convertor1!=null) {
				for (int s = 0; s < stepsPerPattern; s++) {
					List<MidiNote> mns = null;
					if (inst.name().equals(Hihat.NAME) && InstrumentPattern.isOpenHihat(inst.stepValues[s])) {
						mns = convertor2.getMidiNotesForPatternValue(inst.stepValues[s]);
					} else {
						mns = convertor1.getMidiNotesForPatternValue(inst.stepValues[s]);
						if (inst.name().equals(Bass.NAME)) {
							int octave = pattern.getInstrument(Octave.NAME).stepValues[s];
							int note = pattern.getInstrument(Note.NAME).stepValues[s];
							SequenceChord chord = PatternSequence.getChordForStep(pattern.chords, s);
							if (note>0) {
								note = chord.baseNote + chord.interval[note - 1];
							} else {
								note = chord.baseNote;
							}
							BassConvertor.applyOctaveNote(mns, octave, note);
						}
					}
					if (mns.size()>0) {
						long nextActiveTick = (sequenceEndTick - 1);
						for (int ns = (s+1); ns < stepsPerPattern; ns++) {
							if (inst.stepValues[ns]!=PatternInstrument.OFF) {
								nextActiveTick = MidiSequenceUtil.getStepTick(rythm,ns);
								break;
							}
						}
						Track track = sequence.getTracks()[inst.index];
						MidiSequenceUtil.addMidiNotesToTrack(mns,track,rythm,s,nextActiveTick,ticksPerStep);
					}
				}
			}
		}
	}
}
