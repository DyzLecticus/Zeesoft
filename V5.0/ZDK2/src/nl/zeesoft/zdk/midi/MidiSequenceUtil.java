package nl.zeesoft.zdk.midi;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class MidiSequenceUtil {
	public static final int		TEMPO		= 0x51;
	public static final int		RESOLUTION	= 960;
	
	public static Sequence createSequence(int tracks) {
		Sequence r = null;
		try {
			r = new Sequence(Sequence.PPQ,MidiSequenceUtil.RESOLUTION,tracks);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
		return r;
	}
	
	public static long getStepTick(int step) {
		long r = 0;
		if (step>0) {
			int ticksPerStep = getTicksPerStep();
			r = step * ticksPerStep;
			int beatStep = step % MidiSys.groove.getStepsPerBeat();
			float delay = MidiSys.groove.getShuffleForBeatStep(beatStep);
			if (delay>0) {
				r = r + (long)(delay * (float)ticksPerStep);
			}
		}
		return r;
	}
	
	public static int getTicksPerStep() {
		return RESOLUTION / MidiSys.groove.getStepsPerBeat();
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

	/*
	public static void addTempoMetaEventToSequence(Sequence sequence, int trackNum, float beatsPerMinute, long tick) {
		Track track = sequence.getTracks()[trackNum];
		int tempo = (int)(60000000 / beatsPerMinute);
		byte[] b = new byte[3];
		int tmp = tempo >> 16;
		b[0] = (byte) tmp;
		tmp = tempo >> 8;
		b[1] = (byte) tmp;
		b[2] = (byte) tempo;
		createMetaEventOnTrack(track,TEMPO,b,b.length,tick);
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
	*/
}
