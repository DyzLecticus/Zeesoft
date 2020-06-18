package nl.zeesoft.zdk.midi;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.Lock;

public class MidiSys {
	private static Lock				lock			= new Lock();
	private static Logger			logger			= null;
	private static StateManager		stateManager	= null;
	private static Synth			synth			= null;
	private static PatchConfig		patchConfig		= null;
	private static LfoManager		lfoManager		= null;
	private static NotePlayer		notePlayer		= null;
	
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
	
	public static StateManager getStateManager() {
		MidiSys self = new MidiSys();
		lock.lock(self);
		StateManager r = getStateManagerNoLock();
		lock.unlock(self);
		return r;
	}
	
	public static CodeRunnerChain getCodeRunnerChainForSoundbankFiles(String ... filePaths) {
		CodeRunnerChain r = null;
		MidiSys self = new MidiSys();
		lock.lock(self);
		Synth synth = getSynthesizerNoLock();
		if (synth!=null) {
			r = SoundbankLoader.getCodeRunnerChainForSoundbankFiles(synth.getSynthesizer(), filePaths);
		}
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

	public static LfoManager getLfoManager() {
		MidiSys self = new MidiSys();
		lock.lock(self);
		LfoManager r = getLfoManagerNoLock();
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
	
	private static NotePlayer getNotePlayerNoLock() {
		if (MidiSys.notePlayer==null) {
			Synth synth = getSynthesizerNoLock();
			PatchConfig config = getPatchConfigNoLock();
			if (synth!=null && config!=null) {
				MidiSys.notePlayer = new NotePlayer(getLoggerNoLock(),getStateManagerNoLock(),config,synth);
			}
		}
		return MidiSys.notePlayer;
	}
	
	private static LfoManager getLfoManagerNoLock() {
		if (MidiSys.lfoManager==null) {
			Synth synth = getSynthesizerNoLock();
			if (synth!=null) {
				MidiSys.lfoManager = new LfoManager(getLoggerNoLock(),synth,getStateManagerNoLock());
			}
		}
		return MidiSys.lfoManager;
	}
	
	private static PatchConfig getPatchConfigNoLock() {
		if (MidiSys.patchConfig==null) {
			Synth synth = getSynthesizerNoLock();
			if (synth!=null) {
				MidiSys.patchConfig = new PatchConfig(getLoggerNoLock(),synth,getLfoManagerNoLock());
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
	
	private static StateManager getStateManagerNoLock() {
		if (MidiSys.stateManager==null) {
			MidiSys.stateManager = new StateManager(getLoggerNoLock());
		}
		return MidiSys.stateManager;
	}
	
	private static Logger getLoggerNoLock() {
		if (MidiSys.logger==null) {
			MidiSys.logger = new Logger();
		}
		return MidiSys.logger;
	}
}
