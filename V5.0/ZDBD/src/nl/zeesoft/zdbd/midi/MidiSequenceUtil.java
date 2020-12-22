package nl.zeesoft.zdbd.midi;

import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import com.sun.media.sound.AudioSynthesizer;

import nl.zeesoft.zdbd.pattern.Rythm;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.RunCode;
import nl.zeesoft.zdk.thread.Waiter;

public class MidiSequenceUtil {
	public static final int			TEMPO				= 0x51;
	public static final int			TEXT				= 0x01;
	public static final int			RESOLUTION			= 960;
	
	public static Sequence createSequence(int tracks) {
		Sequence r = null;
		try {
			r = new Sequence(Sequence.PPQ,MidiSequenceUtil.RESOLUTION,tracks);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
		return r;
	}
	
	public static Sequence copySequence(Sequence ori) {
		Sequence r = createSequence(ori.getTracks().length);
		for (int t = 0; t < ori.getTracks().length; t++) {
			Track track = ori.getTracks()[t];
			for (int e = 0; e < track.size(); e++) {
				r.getTracks()[t].add(track.get(e));
			}
		}
		return r;
	}

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

	public static void alignTrackEndings(Sequence sequence, long sequenceEndTick) {
		for (int t = 0; t < sequence.getTracks().length; t++) {
			createEventOnTrack(sequence.getTracks()[t],ShortMessage.NOTE_OFF,0,0,0,sequenceEndTick - 1);
		}
	}

	public static void alignTrackEndings(Sequence sequence, Rythm rythm) {
		alignTrackEndings(sequence, getSequenceEndTick(rythm));
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

	public static RunCode getRenderSequenceToMidiFileRunCode(Sequence sequence, String path) {
		return new RunCode() {
			@Override
			protected boolean run() {
				renderSequenceToMidiFile(sequence, path);
				return true;
			}
		};
	}
	
	public static void renderSequenceToMidiFile(Sequence sequence, String path) {
		File file = new File(path);
        int[] fileTypes = MidiSystem.getMidiFileTypes(sequence);
        if (fileTypes.length > 0) {
			try {
				MidiSystem.write(sequence,fileTypes[0],file);
			} catch (IOException e) {
				Logger.err(new MidiSequenceUtil(), new Str("Caught IO exception while writing MIDI file"), e);
			}
        }
	}

	public static RunCode getRenderSequenceToAudioFileRunCode(Sequence sequence, String path) {
		return new RunCode() {
			@Override
			protected boolean run() {
				renderSequenceToAudioFile(sequence, path);
				return true;
			}
		};
	}

	public static void renderSequenceToAudioFile(Sequence sequence, String path) {
		if (MidiSys.synthesizer!=null && MidiSys.synthesizer instanceof AudioSynthesizer) {
			MidiSequenceUtil self = new MidiSequenceUtil();
			MidiSys.sequencer.stop();
			MidiSys.closeSynthesizer();
			File file = new File(path);
			try {
				AudioSynthesizer synth = (AudioSynthesizer) MidiSystem.getSynthesizer();
				AudioInputStream stream = synth.openStream(null, null);
				MidiSys.synthesizer = synth;
				CodeRunnerChain chain = MidiSys.getCodeRunnerChainForSoundbankFiles(MidiSys.loadedSoundbanks);
				if (Waiter.startAndWaitFor(chain,10000)) {
					// Play Sequence into AudioSynthesizer Receiver.
					double total = send(sequence, synth.getReceiver());
					// Calculate how long the WAVE file needs to be.
					long len = (long) (stream.getFormat().getFrameRate() * (total + 4));
					stream = new AudioInputStream(stream, stream.getFormat(), len);
					// Write WAVE file to disk.
					AudioSystem.write(stream, AudioFileFormat.Type.WAVE, file);
					// We are finished, close synthesizer.
					synth.close();
				}
			} catch (Exception e) {
				Logger.err(self,new Str("Caught exception while rendering sequence"),e);
			}
			MidiSys.openDevices();
			CodeRunnerChain chain = MidiSys.getCodeRunnerChainForSoundbankFiles(MidiSys.loadedSoundbanks);
			Waiter.startAndWaitFor(chain,10000);
		}
	}

	/*
	 * Send entire MIDI Sequence into Receiver using time stamps.
	 */
	private static double send(Sequence seq, Receiver recv) {
		float divtype = seq.getDivisionType();
		Track[] tracks = seq.getTracks();
		int[] trackspos = new int[tracks.length];
		int mpq = 500000;
		int seqres = seq.getResolution();
		long lasttick = 0;
		long curtime = 0;
		while (true) {
			MidiEvent selevent = null;
			int seltrack = -1;
			for (int i = 0; i < tracks.length; i++) {
				int trackpos = trackspos[i];
				Track track = tracks[i];
				if (trackpos < track.size()) {
					MidiEvent event = track.get(trackpos);
					if (selevent == null
							|| event.getTick() < selevent.getTick()) {
						selevent = event;
						seltrack = i;
					}
				}
			}
			if (seltrack == -1)
				break;
			trackspos[seltrack]++;
			long tick = selevent.getTick();
			if (divtype == Sequence.PPQ)
				curtime += ((tick - lasttick) * mpq) / seqres;
			else
				curtime = (long) ((tick * 1000000.0 * divtype) / seqres);
			lasttick = tick;
			MidiMessage msg = selevent.getMessage();
			if (msg instanceof MetaMessage) {
				if (divtype == Sequence.PPQ)
					if (((MetaMessage) msg).getType() == TEMPO) {
						byte[] data = ((MetaMessage) msg).getData();
						mpq = ((data[0] & 0xff) << 16)
								| ((data[1] & 0xff) << 8) | (data[2] & 0xff);
					}
			} else {
				if (recv != null)
					recv.send(msg, curtime);
			}
		}
		return curtime / 1000000.0;
	}
}
