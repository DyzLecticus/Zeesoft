package nl.zeesoft.zdk.midi;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.thread.Lock;

public class MidiSys {
	private static Lock			lock		= new Lock();
	private static Logger		logger		= null;
	private static Synth		synth		= null;
	private static PatchConfig	instConfig	= null;
	private static NotePlayer	notePlayer	= null;
	
	public static void setLogger(Logger logger) {
		MidiSys self = new MidiSys();
		lock.lock(self);
		MidiSys.logger = logger;
		lock.unlock(self);
		lock.setLogger(self,logger);
	}
	
	public static Logger getLogger() {
		MidiSys self = new MidiSys();
		lock.lock(self);
		Logger r = getLoggerNoLock();
		lock.unlock(self);
		return r;
	}
	
	public static PatchConfig getInstrumentConfig() {
		MidiSys self = new MidiSys();
		lock.lock(self);
		PatchConfig r = getInstrumentConfigNoLock();
		lock.unlock(self);
		return r;
	}
	
	public static NotePlayer getNotePlayer() {
		MidiSys self = new MidiSys();
		lock.lock(self);
		NotePlayer r = getNotePlayerNoLock();
		lock.unlock(self);
		return r;
	}
	
	public static int getMsPerStep(int beatsPerMinute, int stepsPerBeat) {
		return (60000 / beatsPerMinute) / stepsPerBeat;
	}
	
	private static NotePlayer getNotePlayerNoLock() {
		if (MidiSys.notePlayer==null) {
			Synth synth = getSynthesizerNoLock();
			PatchConfig config = getInstrumentConfigNoLock();
			if (synth!=null && config!=null) {
				MidiSys.notePlayer = new NotePlayer(getLoggerNoLock(),config,synth);
			}
		}
		return MidiSys.notePlayer;
	}
	
	private static PatchConfig getInstrumentConfigNoLock() {
		if (MidiSys.instConfig==null) {
			Synth synth = getSynthesizerNoLock();
			if (synth!=null) {
				MidiSys.instConfig = new PatchConfig(getLoggerNoLock(),synth);
			}
		}
		return MidiSys.instConfig;
	}
	
	private static Synth getSynthesizerNoLock() {
		if (MidiSys.synth==null) {
			try {
				Synthesizer synthesizer = MidiSystem.getSynthesizer();
				synthesizer.open();
				MidiSys.synth = new Synth(getLoggerNoLock(),synthesizer);
			} catch (MidiUnavailableException e) {
				getLoggerNoLock().error(new MidiSys(),new Str("Failed to initialize Synthesizer"),e);
			}
		}
		return MidiSys.synth;
	}
	
	private static Logger getLoggerNoLock() {
		if (MidiSys.logger==null) {
			MidiSys.logger = new Logger();
		}
		return MidiSys.logger;
	}
}
