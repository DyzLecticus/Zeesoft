package nl.zeesoft.zdbd.midi;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import nl.zeesoft.zdbd.pattern.DrumAndBassPattern;
import nl.zeesoft.zdbd.pattern.PatternSequence;
import nl.zeesoft.zdbd.pattern.Rythm;

public class MidiSequenceConvertor {
	public static final int							TEMPO			= 0x51;
	public static final int							TEXT			= 0x01;
	public static final int							RESOLUTION		= 960;
	
	public int										beatsPerMinute	= 120;
	
	public SortedMap<String,InstrumentConvertor>	convertors		= new TreeMap<String,InstrumentConvertor>();

	public MidiSequenceConvertor() {
		initializeDefaults();
	}
	
	public Sequence generateSequenceForPatternSequence(PatternSequence sequence) {
		Sequence r = null;	
		List<DrumAndBassPattern> patterns = sequence.getSequencedPatterns();
		r = createSequence();
		long startTick = 0;
		for (DrumAndBassPattern pattern: patterns) {
			Sequence seq = generateSequenceForPattern(pattern);
			for (int t = 0; t < seq.getTracks().length; t++) {
				Track track = seq.getTracks()[t];
				for (int e = 0; e < track.size(); e++) {
					MidiEvent event = track.get(e);
					event.setTick(event.getTick() + startTick);
					r.getTracks()[t].add(event);
				}
			}
			startTick += getSequenceEndTick(pattern.rythm);
		}
		return r;
	}
	
	public Sequence generateSequenceForPattern(DrumAndBassPattern pattern) {
		Sequence r = null;
		r = createSequence();
		addTempoMetaEventToSequence(r);
		addNotesToSequence(r,pattern);
		alignTrackEndings(r,pattern.rythm);
		return r;
	}
	
	public void initializeDefaults() {
		convertors.clear();
		for (int i = 0; i < DrumAndBassPattern.INSTRUMENT_NAMES.length; i++) {
			String name = DrumAndBassPattern.INSTRUMENT_NAMES[i];
			if (i==DrumAndBassPattern.BASS) {
				MonoConvertor bass = new MonoConvertor();
				bass.name = name;
				bass.channel = SynthConfig.BASS_CHANNEL;
				convertors.put(name, bass);
			} else {
				DrumConvertor drum = new DrumConvertor();
				drum.name = name;
				DrumSampleConvertor sample = new DrumSampleConvertor();
				if (i==DrumAndBassPattern.BASEBEAT) {
					sample.midiNote = 35;
				} else if (i==DrumAndBassPattern.SNARE) {
					sample.midiNote = 50;
					sample.velocity = 80;
					sample.accentVelocity = 100;
					sample.hold = 0.8F;
					sample.accentHold = 1.0F;
				} else if (i==DrumAndBassPattern.CLOSED_HIHAT) {
					sample.midiNote = 44;
					sample.velocity = 70;
					sample.accentVelocity = 80;
					sample.hold = 0.2F;
					sample.accentHold = 0.3F;
				} else if (i==DrumAndBassPattern.OPEN_HIHAT) {
					sample.midiNote = 45;
					sample.velocity = 80;
					sample.accentVelocity = 90;
					sample.hold = 0.5F;
					sample.accentHold = 0.8F;
				} else if (i==DrumAndBassPattern.RIDE) {
					sample.midiNote = 69;
					sample.velocity = 80;
					sample.accentVelocity = 90;
					sample.hold = 0.9F;
					sample.accentHold = 1.9F;
				} else if (i==DrumAndBassPattern.CYMBAL) {
					sample.midiNote = 70;
					sample.velocity = 90;
					sample.accentVelocity = 100;
					sample.hold = 1.9F;
					sample.accentHold = 3.9F;
				}
				drum.samples.add(sample);
				convertors.put(name, drum);
			}
		}
	}
	
	protected Sequence createSequence() {
		Sequence r = null;
		try {
			r = new Sequence(Sequence.PPQ,RESOLUTION,DrumAndBassPattern.INSTRUMENT_NAMES.length);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
		return r;
	}

	protected void addTempoMetaEventToSequence(Sequence sequence) {
		Track track = sequence.getTracks()[0];
		int tempo = (60000000 / beatsPerMinute);
		byte[] b = new byte[3];
		int tmp = tempo >> 16;
		b[0] = (byte) tmp;
		tmp = tempo >> 8;
		b[1] = (byte) tmp;
		b[2] = (byte) tempo;
		createMetaEventOnTrack(track,TEMPO,b,b.length,0);
	}
	
	protected void addNotesToSequence(Sequence sequence, DrumAndBassPattern pattern) {
		long sequenceEndTick = getSequenceEndTick(pattern.rythm);
		int ticksPerStep = getTicksPerStep(pattern.rythm);
		int stepsPerPattern = pattern.rythm.getStepsPerPattern();
		for (int i = 0; i < DrumAndBassPattern.INSTRUMENT_NAMES.length; i++) {
			String name = DrumAndBassPattern.INSTRUMENT_NAMES[i];
			InstrumentConvertor convertor = convertors.get(name);
			if (convertor!=null) {
				for (int s = 0; s < stepsPerPattern; s++) {
					List<MidiNote> mns = convertor.getMidiNotesForPatternNote(pattern.pattern[s][i]);
					for (MidiNote mn: mns) {
						long startTick = getStepTick(pattern.rythm,s);
						if (startTick>sequenceEndTick) {
							startTick = startTick % sequenceEndTick;
						}
						Track track = sequence.getTracks()[i];
						createEventOnTrack(track,ShortMessage.NOTE_ON,mn.channel,mn.midiNote,mn.velocity,startTick);
						long add = (long)(mn.hold * (float)ticksPerStep);
						long endTick = startTick + add;
						if (endTick>=sequenceEndTick) {
							endTick = sequenceEndTick - 1;
						}
						createEventOnTrack(track,ShortMessage.NOTE_OFF,mn.channel,mn.midiNote,mn.velocity,endTick);
					}
				}
			}
		}
	}

	protected void alignTrackEndings(Sequence sequence, Rythm rythm) {
		long sequenceEndTick = getSequenceEndTick(rythm);
		for (int t = 0; t < sequence.getTracks().length; t++) {
			createEventOnTrack(sequence.getTracks()[t],ShortMessage.NOTE_OFF,0,0,0,sequenceEndTick - 1);
		}
	}
	
	protected long getSequenceEndTick(Rythm rythm) {
		return getStepTick(rythm,rythm.getStepsPerPattern());
	}
	
	protected long getStepTick(Rythm rythm,int step) {
		long r = 0;
		if (step>0) {
			int ticksPerStep = getTicksPerStep(rythm);
			r = step * ticksPerStep;
			/* TODO: Shuffle
			int beatStep = step % rythm.stepsPerBeat;
			float delayPercentage = state.getStepDelayPercentages()[beatStep];
			if (delayPercentage>0) {
				r = r + (int)(delayPercentage * (float)ticksPerStep);
			}
			*/
		}
		return r;
	}
	
	protected int getTicksPerStep(Rythm rythm) {
		return RESOLUTION / rythm.stepsPerBeat;
	}
	
	protected void createEventOnTrack(Track track, int type, int channel, int num, int val, long tick) {
		ShortMessage message = new ShortMessage();
		try {
			message.setMessage(type,channel,num,val); 
			MidiEvent event = new MidiEvent(message,tick);
			track.add(event);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}
	
	protected void createMetaEventOnTrack(Track track, int type, byte[] data, int length, long tick) {
		MetaMessage message = new MetaMessage();
		try {
			message.setMessage(type,data,length);
			MidiEvent event = new MidiEvent(message,tick);
			track.add(event);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}
}
