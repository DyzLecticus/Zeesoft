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
import nl.zeesoft.zdbd.pattern.instruments.Shift;
import nl.zeesoft.zdbd.pattern.instruments.Stab;
import nl.zeesoft.zdk.thread.Lock;

public class PatternSequenceConvertor {
	protected Lock					lock				= new Lock();
	
	protected InstrumentConvertors	convertors			= new InstrumentConvertors();
	protected long					changed				= System.currentTimeMillis();
	
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
	
	public InstrumentConvertor getConvertor(String name) {
		lock.lock(this);
		InstrumentConvertor r = convertors.get(name);
		lock.unlock(this);
		return r;
	}
	
	public void setConvertorLayerProperty(int convertor, int layer, String property, Object value) {
		lock.lock(this);
		convertors.setConvertorLayerProperty(convertor, layer, property, value);
		changed = System.currentTimeMillis();
		lock.unlock(this);
	}
	
	public Sequence generateSequenceForPatternSequence(PatternSequence sequence,Arpeggiator arp) {
		Sequence r = createSequence();
		if (r!=null) {
			lock.lock(this);
			long startTick = 0;
			for (int i = 0; i < sequence.getSequencedPatterns().size(); i++) {
				Sequence seq = generateNoteSequenceForPattern(sequence,i);
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

	public long getChanged() {
		lock.lock(this);
		long r = changed;
		lock.unlock(this);
		return r;
	}

	protected Sequence generateNoteSequenceForPattern(PatternSequence patternSequence, int patternIndex) {
		Sequence r = createSequence();
		addNotesToSequence(r,patternSequence,patternIndex);
		MidiSequenceUtil.alignTrackEndings(r,patternSequence.rythm);
		return r;
	}
	
	protected Sequence createSequence() {
		return MidiSequenceUtil.createSequence(getTrackNames().size());
	}
	
	protected void addNotesToSequence(Sequence sequence, PatternSequence patternSequence, int patternIndex) {
		Rythm rythm = patternSequence.rythm;
		InstrumentPattern pattern = patternSequence.getSequencedPatterns().get(patternIndex);
		int baseStep = rythm.getStepsPerPattern() * patternIndex;
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
					SequenceChord chord = patternSequence.getChordForStep(baseStep + s,false);
					if (inst.name().equals(Hihat.NAME) && InstrumentPattern.isOpenHihat(inst.stepValues[s])) {
						mns = convertor2.getMidiNotesForPatternValue(inst.stepValues[s]);
					} else {
						mns = convertor1.getMidiNotesForPatternValue(inst.stepValues[s]);
						if (inst.name().equals(Bass.NAME)) {
							int octave = pattern.getInstrument(Octave.NAME).stepValues[s];
							int note = pattern.getInstrument(Note.NAME).stepValues[s];
							if (note>0) {
								note = chord.baseNote + chord.interval[note - 1];
							} else {
								note = chord.baseNote;
							}
							BassConvertor.applyOctaveNote(mns, octave, note);
						} else if (inst.name().equals(Stab.NAME)) {
							int shift = pattern.getInstrument(Shift.NAME).stepValues[s];
							StabConvertor.applyChordNotes(mns, 0, chord, shift);
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
						Track track = sequence.getTracks()[getTrackNum(inst.name())];
						MidiSequenceUtil.addMidiNotesToTrack(mns,track,rythm,s,nextActiveTick,ticksPerStep);
					}
				}
			}
		}
	}
}
