package nl.zeesoft.zdbd.midi;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import nl.zeesoft.zdbd.pattern.Rythm;

public class MidiSequenceUtil {
	public static final int			TEMPO				= 0x51;
	public static final int			TEXT				= 0x01;
	public static final int			RESOLUTION			= 960;
	
	public static void addTempoMetaEventToSequence(Sequence sequence, int trackNum, float beatsPerMinute) {
		Track track = sequence.getTracks()[trackNum];
		int tempo = (int)(60000000 / beatsPerMinute);
		byte[] b = new byte[3];
		int tmp = tempo >> 16;
		b[0] = (byte) tmp;
		tmp = tempo >> 8;
		b[1] = (byte) tmp;
		b[2] = (byte) tempo;
		createMetaEventOnTrack(track,TEMPO,b,b.length,0);
	}

	public static void alignTrackEndings(Sequence sequence, Rythm rythm) {
		long sequenceEndTick = getSequenceEndTick(rythm);
		for (int t = 0; t < sequence.getTracks().length; t++) {
			createEventOnTrack(sequence.getTracks()[t],ShortMessage.NOTE_OFF,0,0,0,sequenceEndTick - 1);
		}
	}
	
	public static long getSequenceEndTick(Rythm rythm) {
		return getTicksPerStep(rythm) * rythm.getStepsPerPattern() ;
	}
	
	public static long getStepTick(Rythm rythm,int step) {
		long r = 0;
		if (step>0) {
			int ticksPerStep = getTicksPerStep(rythm);
			r = step * ticksPerStep;
			int beatStep = step % rythm.stepsPerBeat;
			float delay = rythm.stepDelays[beatStep];
			if (delay>0 && delay<0.8F) {
				r = r + (long)(delay * (float)ticksPerStep);
			}
		}
		return r;
	}
	
	public static int getTicksPerStep(Rythm rythm) {
		return RESOLUTION / rythm.stepsPerBeat;
	}
	
	public static void createEventOnTrack(Track track, int type, int channel, int num, int val, long tick) {
		ShortMessage message = new ShortMessage();
		try {
			message.setMessage(type,channel,num,val); 
			MidiEvent event = new MidiEvent(message,tick);
			track.add(event);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}
	
	public static void createMetaEventOnTrack(Track track, int type, byte[] data, int length, long tick) {
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
