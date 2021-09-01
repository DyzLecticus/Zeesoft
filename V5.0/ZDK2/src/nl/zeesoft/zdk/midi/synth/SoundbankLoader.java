package nl.zeesoft.zdk.midi.synth;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;

import nl.zeesoft.zdk.midi.MidiSys;

public class SoundbankLoader {
	public static Soundbank load(String path) {
		Soundbank r = null;
		if (MidiSys.isInitialized()) {
			r = loadSoundbank(path,false,false);
			if (r!=null) {
				replaceInstruments(MidiSys.synth.getSynthesizer(), r);
			}
		}
		return r;
	}
	
	public static Soundbank loadSoundbank(
		String path, boolean mockException1, boolean mockException2
	) {
		Soundbank r = null;
		try {
			if (mockException1) {
				throw new InvalidMidiDataException("Mock exception");
			}
			if (mockException2) {
				throw new IOException("Mock exception");
			}
			r = loadSoundbank(path);
		} catch (InvalidMidiDataException e) {
			// Ignore
		} catch (IOException e) {
			// Ignore
		}
		return r;
	}
	
	protected static Soundbank loadSoundbank(String path) throws InvalidMidiDataException, IOException {
		Soundbank r = null;
		SoundbankLoader self = new SoundbankLoader();
		InputStream is = self.getClass().getResourceAsStream(path);
		if (is==null) {
			File file = new File(path);
			if (file.exists()) {
				is = new FileInputStream(file);
			}
		}
		if (is!=null) {
			BufferedInputStream bis = new BufferedInputStream(is);
			r = MidiSystem.getSoundbank(bis);
			bis.close();
			is.close();
		}
		return r;
	}
	
	protected static void replaceInstruments(Synthesizer synthesizer, Soundbank soundbank) {
		for (Instrument inst: soundbank.getInstruments()) {
			for (Instrument synthInst: synthesizer.getLoadedInstruments()) {
				// The sound bank is not loaded correctly; the default piano is always loaded.
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
