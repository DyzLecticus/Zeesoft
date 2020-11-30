package nl.zeesoft.zdbd.midi;

import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import nl.zeesoft.zdbd.pattern.InstrumentPattern;
import nl.zeesoft.zdbd.pattern.PatternSequence;
import nl.zeesoft.zdbd.pattern.Rythm;
import nl.zeesoft.zdbd.pattern.instruments.Hihat;
import nl.zeesoft.zdbd.pattern.instruments.PatternInstrument;

public class MidiSequenceConvertor {
	public static final int							TEMPO			= 0x51;
	public static final int							TEXT			= 0x01;
	public static final int							RESOLUTION		= 960;
	
	public int										beatsPerMinute	= 120;
	
	public InstrumentConvertors						convertors		= new InstrumentConvertors();

	public Sequence generateSequenceForPatternSequence(PatternSequence sequence) {
		Sequence r = createSequence();
		addInitialSynthConfig(r);
		addTempoMetaEventToSequence(r);
		
		List<InstrumentPattern> patterns = sequence.getSequencedPatterns();
		long startTick = 0;
		for (InstrumentPattern pattern: patterns) {
			Sequence seq = generateNoteSequenceForPattern(pattern);
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
	
	public Sequence generateSequenceForPattern(InstrumentPattern pattern) {
		Sequence r = generateNoteSequenceForPattern(pattern);
		addTempoMetaEventToSequence(r);
		addInitialSynthConfig(r);
		return r;
	}
	
	protected Sequence generateNoteSequenceForPattern(InstrumentPattern pattern) {
		Sequence r = createSequence();
		addNotesToSequence(r,pattern);
		alignTrackEndings(r,pattern.rythm);
		return r;
	}
	
	protected Sequence createSequence() {
		Sequence r = null;
		try {
			r = new Sequence(Sequence.PPQ,RESOLUTION,InstrumentPattern.INSTRUMENTS.size() + 1);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
		return r;
	}

	protected void addInitialSynthConfig(Sequence sequence) {
		int controlTrack = InstrumentPattern.INSTRUMENTS.size();
		for (SynthChannelConfig channelConfig: MidiSys.synthConfig.channels) {
			for (Integer control: SynthConfig.CONTROLS) {
				int value = channelConfig.getControlValue(control);
				createEventOnTrack(
					sequence.getTracks()[controlTrack],ShortMessage.CONTROL_CHANGE,channelConfig.channel,control,value,0
				);
			}
		}
	}
	
	protected void addTempoMetaEventToSequence(Sequence sequence) {
		int controlTrack = InstrumentPattern.INSTRUMENTS.size();
		Track track = sequence.getTracks()[controlTrack];
		int tempo = (60000000 / beatsPerMinute);
		byte[] b = new byte[3];
		int tmp = tempo >> 16;
		b[0] = (byte) tmp;
		tmp = tempo >> 8;
		b[1] = (byte) tmp;
		b[2] = (byte) tempo;
		createMetaEventOnTrack(track,TEMPO,b,b.length,0);
	}
	
	protected void addNotesToSequence(Sequence sequence, InstrumentPattern pattern) {
		long sequenceEndTick = getSequenceEndTick(pattern.rythm);
		int ticksPerStep = getTicksPerStep(pattern.rythm);
		int stepsPerPattern = pattern.rythm.getStepsPerPattern();
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
					}
					for (MidiNote mn: mns) {
						long startTick = getStepTick(pattern.rythm,s);
						if (startTick>sequenceEndTick) {
							startTick = startTick % sequenceEndTick;
						}
						Track track = sequence.getTracks()[inst.index];
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
