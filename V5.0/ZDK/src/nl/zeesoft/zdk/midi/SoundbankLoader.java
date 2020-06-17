package nl.zeesoft.zdk.midi;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;

import nl.zeesoft.zdk.FileIO;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.RunCode;

public class SoundbankLoader {
	public static CodeRunnerChain getCodeRunnerChainForSoundbankFiles(Synthesizer synthesizer, String ... filePaths) {
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
					}
				}
			}
		}
	}	
}
