package nl.zeesoft.zdbd.midi;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Transmitter;

import nl.zeesoft.zdk.FileIO;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.midi.SoundbankLoader;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.RunCode;

public class MidiSys {
	public static Synthesizer			synthesizer			= null;
	public static SynthConfig			synthConfig			= null;
	public static Sequencer				sequencer			= null;
	public static MidiSequencePlayer	sequencePlayer		= null;
	public static MidiSequenceConvertor convertor			= null;
	
	
	public static void initialize() {
		Logger.dbg(new MidiSys(), new Str("Initializing MIDI system ..."));
		openDevices();
		synthConfig = new SynthConfig(synthesizer);
		sequencePlayer = new MidiSequencePlayer(sequencer);
		convertor = new MidiSequenceConvertor();
		Logger.dbg(new MidiSys(), new Str("Initialized MIDI system"));
	}
	
	public static void openDevices() {
		if (sequencer==null) {
			try {
				sequencer = MidiSystem.getSequencer(false);
				if (sequencer!=null) {
					sequencer.open();
				} else {
					Logger.err(new MidiSys(),new Str("Sequencer device is not supported"));
				}
			} catch (MidiUnavailableException e) {
				Logger.err(new MidiSys(),new Str("Failed to initialize sequencer"),e);
			}
		}
		if (synthesizer==null && sequencer!=null) {
			if (sequencer instanceof Synthesizer) {
				synthesizer = (Synthesizer) sequencer;
			} else {
				try {
					synthesizer = MidiSystem.getSynthesizer();
					if (synthesizer!=null) {
						synthesizer.open();
					} else {
						Logger.err(new MidiSys(),new Str("Synthesizer device is not supported"));
					}
				} catch (MidiUnavailableException e) {
					Logger.err(new MidiSys(),new Str("Failed to initialize synthesizer"),e);
				}
				if (synthesizer!=null) {
					if (sequencer.getTransmitters().size()>0) {
						for (Transmitter trm: sequencer.getTransmitters()) {
							try {
								trm.setReceiver(synthesizer.getReceiver());
							} catch (MidiUnavailableException e) {
								Logger.err(new MidiSys(),new Str("Failed to link sequencer transmitters to synthesizer"),e);
							}
						}
					} else {
						try {
							sequencer.getTransmitter().setReceiver(synthesizer.getReceiver());
						} catch (MidiUnavailableException e) {
							Logger.err(new MidiSys(),new Str("Failed to link sequencer transmitter to synthesizer"),e);
						}
					}
				}
			}
		}
	}
	
	public static void closeDevices() {
		if (sequencer!=null && sequencer.isOpen()) {
			sequencer.close();
			sequencer = null;
		}
		if (synthesizer!=null && synthesizer.isOpen()) {
			synthesizer.close();
			synthesizer = null;
		}
	}

	public static CodeRunnerChain getCodeRunnerChainForSoundbankFiles(String... filePaths) {
		List<String> paths = new ArrayList<String>();
		for (String path: filePaths) {
			paths.add(path);
		}
		return getCodeRunnerChainForSoundbankFiles(synthesizer, paths);
	}

	public static CodeRunnerChain getCodeRunnerChainForSoundbankFiles(List<String> filePaths) {
		if (synthesizer==null) {
			initialize();
		}
		return getCodeRunnerChainForSoundbankFiles(synthesizer, filePaths);
	}

	protected static CodeRunnerChain getCodeRunnerChainForSoundbankFiles(Synthesizer synthesizer, List<String> filePaths) {
		CodeRunnerChain r = new CodeRunnerChain();
		List<RunCode> codes = new ArrayList<RunCode>();
		for (String path: filePaths) {
			Str error = FileIO.checkFile(path);
			if (error.length()==0) {
				codes.add(getRunCodeForSoundbank(synthesizer,path));
			}
		}
		r.addAll(codes);
		return r;
	}

	protected static RunCode getRunCodeForSoundbank(Synthesizer synthesizer, String path) {
		return new RunCode() {
			@Override
			protected boolean run() {
				Soundbank soundbank = loadSoundBank(path);
				replaceSoundBankInstruments(synthesizer, soundbank);
				return true;
			}
		};
	}
	
	protected static Soundbank loadSoundBank(String path) {
		Soundbank r = null;
		InputStream is = (new SoundbankLoader()).getClass().getResourceAsStream("/" + path);
		if (is!=null) {
			try {
				r = MidiSystem.getSoundbank(new BufferedInputStream(is));
			} catch (InvalidMidiDataException e) {
				// Ignore
			} catch (IOException e) {
				// Ignore
			}
		} 
		if (r==null) {
			File file = new File(path);
			try {
				r = MidiSystem.getSoundbank(file);
			} catch (InvalidMidiDataException e) {
				// Ignore
			} catch (IOException e) {
				// Ignore
			}
		}
		return r;
	}

	protected static void replaceSoundBankInstruments(Synthesizer synthesizer, Soundbank soundbank) {
		if (soundbank!=null) {
			for (Instrument inst: soundbank.getInstruments()) {
				for (Instrument synthInst: synthesizer.getLoadedInstruments()) {
					/**
					 * The sound bank number is not loaded correctly and an empty piano is always loaded.
					 */
					if (!synthInst.getName().startsWith("Piano 1") &&
						synthInst.getPatch().getProgram()==inst.getPatch().getProgram() &&
						synthInst.getPatch().getBank()==inst.getPatch().getBank()
						) {
						synthesizer.unloadInstrument(synthInst);
						synthesizer.loadInstrument(inst);
						break;
					}
				}
			}
		}
	}	
}
