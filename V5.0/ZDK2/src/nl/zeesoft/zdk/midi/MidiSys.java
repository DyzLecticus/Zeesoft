package nl.zeesoft.zdk.midi;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.midi.synth.Synth;

public class MidiSys {
	public static Synth		synth	= null;
	public static Groove	groove	= new Groove();
	
	public static synchronized void initialize() {
		initializeSynth(false, false);
	}
	
	public static synchronized boolean isInitialized() {
		return synth!=null;
	}
	
	public static synchronized void destroy() {
		if (isInitialized()) {
			destroySynth();
		}
	}
	
	public static synchronized void initializeSynth(boolean mockNotSupported, boolean mockException) {
		try {
			Synthesizer synthesizer = MidiSystem.getSynthesizer();
			if (synthesizer!=null && !mockNotSupported) {
				if (mockException) {
					throw new MidiUnavailableException("Mock exception");
				}
				synthesizer.open();
				synth = new Synth(synthesizer);
			} else {
				Logger.error(new MidiSys(), "Synthesizer device is not supported");
			}
		} catch (MidiUnavailableException e) {
			Logger.error(new MidiSys(), "Failed to initialize synthesizer",e);
		}
	}
	
	protected static void destroySynth() {
		synth.allSoundOff();
		synth.getSynthesizer().close();
		synth = null;
	}
}
