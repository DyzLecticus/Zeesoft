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
	private static State		state		= null;
	private static Synth		synth		= null;
	private static PatchConfig	patchConfig	= null;
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
	
	public static State getState() {
		MidiSys self = new MidiSys();
		lock.lock(self);
		State r = getStateNoLock();
		lock.unlock(self);
		return r;
	}

	public static PatchConfig getPatchConfig() {
		MidiSys self = new MidiSys();
		lock.lock(self);
		PatchConfig r = getPatchConfigNoLock();
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
			PatchConfig config = getPatchConfigNoLock();
			if (synth!=null && config!=null) {
				MidiSys.notePlayer = new NotePlayer(getLoggerNoLock(),getStateNoLock(),config,synth);
			}
		}
		return MidiSys.notePlayer;
	}
	
	private static PatchConfig getPatchConfigNoLock() {
		if (MidiSys.patchConfig==null) {
			Synth synth = getSynthesizerNoLock();
			if (synth!=null) {
				MidiSys.patchConfig = new PatchConfig(getLoggerNoLock(),synth);
			}
		}
		return MidiSys.patchConfig;
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
	
	private static State getStateNoLock() {
		if (MidiSys.state==null) {
			MidiSys.state = new State(getLoggerNoLock());
		}
		return MidiSys.state;
	}
	
	private static Logger getLoggerNoLock() {
		if (MidiSys.logger==null) {
			MidiSys.logger = new Logger();
		}
		return MidiSys.logger;
	}
}
