package nl.zeesoft.zdk.midi;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Transmitter;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.Lock;

public class MidiSys {
	private static Lock				lock			= new Lock();
	private static Logger			logger			= null;
	private static StateManager		stateManager	= null;
	private static Synth			synth			= null;
	private static SynthManager		synthManager	= null;
	private static LfoManager		lfoManager		= null;
	private static NotePlayer		notePlayer		= null;
	private static SequencePlayer	sequencePlayer	= null;
	
	private static Sequencer		seq				= null;
	private static Synthesizer 		syn				= null;
	
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
	
	public static void closeDevices() {
		MidiSys self = new MidiSys();
		lock.lock(self);
		if (MidiSys.seq!=null && MidiSys.seq.isOpen()) {
			MidiSys.seq.close();
		}
		if (MidiSys.syn!=null && MidiSys.syn.isOpen()) {
			MidiSys.syn.close();
		}
		lock.unlock(self);
	}
	
	public static CodeRunnerChain getCodeRunnerChainForSoundbankFiles(String ... filePaths) {
		CodeRunnerChain r = null;
		MidiSys self = new MidiSys();
		lock.lock(self);
		Synthesizer synthesizer = getSynNoLock();
		if (synthesizer!=null) {
			r = SoundbankLoader.getCodeRunnerChainForSoundbankFiles(synthesizer, filePaths);
		}
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
	
	public static SynthManager getSynthManager() {
		MidiSys self = new MidiSys();
		lock.lock(self);
		SynthManager r = getSynthManagerNoLock();
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
	
	public static SequencePlayer getSequencPlayer() {
		MidiSys self = new MidiSys();
		lock.lock(self);
		SequencePlayer r = getSequencePlayerNoLock();
		lock.unlock(self);
		return r;
	}
	
	private static SequencePlayer getSequencePlayerNoLock() {
		if (MidiSys.sequencePlayer==null) {
			Sequencer sequencer = getSeqNoLock();
			if (sequencer!=null) {
				MidiSys.sequencePlayer = new SequencePlayer(getLoggerNoLock(),sequencer,getStateManagerNoLock(),getLfoManagerNoLock());
			}
		}
		return MidiSys.sequencePlayer;
	}
	
	private static NotePlayer getNotePlayerNoLock() {
		if (MidiSys.notePlayer==null) {
			Synth synth = getSynthNoLock();
			SynthManager manager = getSynthManagerNoLock();
			if (synth!=null && manager!=null) {
				MidiSys.notePlayer = new NotePlayer(getLoggerNoLock(),getStateManagerNoLock(),manager,synth);
			}
		}
		return MidiSys.notePlayer;
	}
	
	private static LfoManager getLfoManagerNoLock() {
		if (MidiSys.lfoManager==null) {
			Synth synth = getSynthNoLock();
			if (synth!=null) {
				MidiSys.lfoManager = new LfoManager(getLoggerNoLock(),synth,getStateManagerNoLock());
			}
		}
		return MidiSys.lfoManager;
	}
	
	private static SynthManager getSynthManagerNoLock() {
		if (MidiSys.synthManager==null) {
			Synth synth = getSynthNoLock();
			if (synth!=null) {
				MidiSys.synthManager = new SynthManager(getLoggerNoLock(),synth,getLfoManagerNoLock());
			}
		}
		return MidiSys.synthManager;
	}
	
	private static Synth getSynthNoLock() {
		if (MidiSys.synth==null) {
			Synthesizer synthesizer = getSynNoLock();
			if (synthesizer!=null) {
				MidiSys.synth = new Synth(getLoggerNoLock(),synthesizer);
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
	
	private static Sequencer getSeqNoLock() {
		if (seq==null) {
			try {
				seq = MidiSystem.getSequencer(false);
				if (seq!=null) {
					seq.open();
				} else {
					getLoggerNoLock().error(new MidiSys(),new Str("Sequencer device is not supported"));
				}
			} catch (MidiUnavailableException e) {
				getLoggerNoLock().error(new MidiSys(),new Str("Failed to initialize sequencer"),e);
			}
		}
		return seq;
	}
	
	private static Synthesizer getSynNoLock() {
		if (syn==null) {
			Sequencer seq = getSeqNoLock();
			if (seq!=null) {
				if (seq instanceof Synthesizer) {
					syn = (Synthesizer) seq;
				} else {
					try {
						syn = MidiSystem.getSynthesizer();
						if (syn!=null) {
							syn.open();
						} else {
							getLoggerNoLock().error(new MidiSys(),new Str("Synthesizer device is not supported"));
						}
					} catch (MidiUnavailableException e) {
						getLoggerNoLock().error(new MidiSys(),new Str("Failed to initialize synthesizer"),e);
					}
					if (syn!=null) {
						if (seq.getTransmitters().size()>0) {
							for (Transmitter trm: seq.getTransmitters()) {
								try {
									trm.setReceiver(syn.getReceiver());
								} catch (MidiUnavailableException e) {
									getLoggerNoLock().error(new MidiSys(),new Str("Failed to link sequencer to synthesizer"),e);
								}
							}
						} else {
							try {
								seq.getTransmitter().setReceiver(syn.getReceiver());
							} catch (MidiUnavailableException e) {
								getLoggerNoLock().error(new MidiSys(),new Str("Failed to link sequencer to synthesizer"),e);
							}
						}
					}
				}
			}
		}
		return syn;
	}
}
