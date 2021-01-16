package nl.zeesoft.zdbd.midi;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.RunCode;

public class MidiSys {
	public static Synthesizer				synthesizer			= null;
	public static MidiSequencer				sequencer			= null;
	public static List<String>				loadedSoundbanks	= new ArrayList<String>();

	public static RunCode getInitializeRunCode() {
		return new RunCode() {
			@Override
			protected boolean run() {
				initialize();
				return true;
			}
		};
	}
	
	public static void initialize() {
		if (!isInitialized()) {
			Logger.dbg(new MidiSys(), new Str("Initializing MIDI system ..."));
			openDevices();
			Logger.dbg(new MidiSys(), new Str("Initialized MIDI system"));
		}
	}
	
	public static boolean isInitialized() {
		boolean r = false;
		if (synthesizer!=null && synthesizer.isOpen() && sequencer!=null && sequencer.isOpen()) {
			r = true;
		}
		return r;
	}
	
	public static void openDevices() {
		try {
			synthesizer = MidiSystem.getSynthesizer();
			if (synthesizer!=null) {
				Logger.dbg(new MidiSys(), new Str("Initializing synthesizer ..."));
				synthesizer.open();
			} else {
				Logger.err(new MidiSys(),new Str("Synthesizer device is not supported"));
			}
			if (synthesizer!=null) {
				SynthConfig config = new SynthConfig();
				config.configureSynthesizer(synthesizer);
				Logger.dbg(new MidiSys(), new Str("Initialized synthesizer"));
				Logger.dbg(new MidiSys(), new Str("Initializing sequencer ..."));
				if (sequencer==null) {
					sequencer = new MidiSequencer();
				}
				sequencer.setSynthConfig(config);
				Logger.dbg(new MidiSys(), new Str("Initialized sequencer"));
			}
		} catch (MidiUnavailableException e) {
			Logger.err(new MidiSys(),new Str("Failed to initialize synthesizer"),e);
		}
	}
	
	public static void destroy() {
		if (isInitialized()) {
			Logger.dbg(new MidiSys(), new Str("Destroying MIDI system ..."));
			closeDevices();
			Logger.dbg(new MidiSys(), new Str("Destroying MIDI system"));
		}
	}
	
	public static void closeDevices() {
		closeSequencer();
		closeSynthesizer();
	}
	
	public static void closeSequencer() {
		if (sequencer!=null && sequencer.isOpen()) {
			Logger.dbg(new MidiSys(), new Str("Closing sequencer ..."));
			sequencer.close();
			Logger.dbg(new MidiSys(), new Str("Closed sequencer"));
		}
	}
	
	public static void closeSynthesizer() {
		if (synthesizer!=null && synthesizer.isOpen()) {
			allSoundOff();
			Logger.dbg(new MidiSys(), new Str("Closing synthesizer ..."));
			synthesizer.close();
			Logger.dbg(new MidiSys(), new Str("Closed synthesizer"));
		}
	}
	
	public static void allSoundOff() {
		if (synthesizer!=null) {
			MidiChannel[] channels = synthesizer.getChannels();
			for (int c = 0; c < channels.length; c++) {
				MidiChannel channel = channels[c];
				channel.allSoundOff();
			}
		}
	}

	public static CodeRunnerChain getCodeRunnerChainForSoundbankFiles(List<String> paths) {
		CodeRunnerChain r = new CodeRunnerChain();
		List<RunCode> codes = new ArrayList<RunCode>();
		for (String path: paths) {
			codes.add(getLoadSoundbankRunCode(path));
		}
		r.addAll(codes);
		return r;
	}

	public static CodeRunnerChain getCodeRunnerChainForSoundbankFiles(String... paths) {
		CodeRunnerChain r = new CodeRunnerChain();
		r.addAll(getLoadSoundbankRunCodes(paths));
		return r;
	}
	
	public static List<RunCode> getLoadSoundbankRunCodes(String... paths) {
		List<RunCode> r = new ArrayList<RunCode>();
		for (String path: paths) {
			r.add(getLoadSoundbankRunCode(path));
		}
		return r;
	}
	
	public static RunCode getLoadSoundbankRunCode(String path) {
		return new RunCode() {
			@Override
			protected boolean run() {
				if (synthesizer==null) {
					initialize();
				}
				Soundbank soundbank = loadSoundBank(synthesizer,path);
				replaceSoundBankInstruments(synthesizer, soundbank);
				return true;
			}
		};
	}
	
	protected static Soundbank loadSoundBank(Synthesizer synthesizer, String path) {
		Soundbank r = null;
		InputStream is = (new SoundbankLoader()).getClass().getResourceAsStream(path);
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
		if (r!=null) {
			if (!loadedSoundbanks.contains(path)) {
				loadedSoundbanks.add(path);
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
