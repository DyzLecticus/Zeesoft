package nl.zeesoft.zmmt.gui;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Transmitter;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class InitializeMidiDevicesWorker extends Worker {
	private static final String		INTERNAL_DRUM		= "resources/ZeeTrackerDrumKit.sf2";
	private static final String		INTERNAL_SYNTH		= "resources/ZeeTrackerSynthesizers.sf2";
	
	private Controller				controller			= null;
	private File					soundFont			= null;

	public InitializeMidiDevicesWorker(Messenger msgr, WorkerUnion union,Controller controller,String customSoundFontFileName) {
		super(msgr, union);
		setSleep(1);
		this.controller = controller;
		if (customSoundFontFileName.length()>0) {
			File sf = new File(customSoundFontFileName);
			if (sf.exists()) {
				this.soundFont = sf;
			} else {
				controller.showErrorMessage(this,"Custom sound font file not found: " + customSoundFontFileName);
			}
		}
	}
	
	@Override
	public void whileWorking() {
		controller.setBusy(this,"Initializing MIDI devices","");
		Sequencer seq = null;
		try {
			seq = MidiSystem.getSequencer(false);
			if (seq!=null) {
				seq.open();
				if (seq.isOpen()) {
					controller.setSequencer(seq);
				}
			}
		} catch (MidiUnavailableException e) {
			controller.showErrorMessage(this,"Failed to initialize MIDI sequencer",e);
		}
		if (seq!=null) {
			Synthesizer synth = null;
			if (seq instanceof Synthesizer) {
				synth = (Synthesizer) seq;
			} else {
				if (synth==null) {
					try {
						synth = MidiSystem.getSynthesizer();
					} catch (MidiUnavailableException e) {
						controller.showErrorMessage(this,"Failed to obtain MIDI synthesizer",e);
					}
				}
				if (synth!=null) {
					try {
						synth.open();
					} catch (MidiUnavailableException e) {
						controller.showErrorMessage(this,"Failed to open MIDI synthesizer",e);
					}
				}
				if (seq.getTransmitters().size()>0) {
					for (Transmitter trm: seq.getTransmitters()) {
						try {
							trm.setReceiver(synth.getReceiver());
						} catch (MidiUnavailableException e) {
							controller.showErrorMessage(this,"Failed to link sequencer to synthesizer",e);
						}
					}
				} else {
					try {
						seq.getTransmitter().setReceiver(synth.getReceiver());
					} catch (MidiUnavailableException e) {
						controller.showErrorMessage(this,"Failed to link sequencer to synthesizer",e);
					}
				}
			}
			if (synth!=null && synth.isOpen()) {
				Soundbank internalDrumKit = loadSoundBank(INTERNAL_DRUM);
				Soundbank internalSynthesizers = loadSoundBank(INTERNAL_SYNTH);
				Soundbank baseSoundFont = synth.getDefaultSoundbank();
				if (soundFont!=null) {
					controller.setBusy(this,"Loading custom sound font",soundFont.getAbsolutePath());
					Soundbank customSoundFont = synth.getDefaultSoundbank();
					try {
						customSoundFont = MidiSystem.getSoundbank(soundFont);
					} catch (InvalidMidiDataException | IOException e) {
						controller.showErrorMessage(this,"Failed to load custom sound font: " + soundFont.getAbsolutePath(),e);
					}
					if (customSoundFont!=null) {
						synth.unloadAllInstruments(baseSoundFont);
						synth.loadAllInstruments(customSoundFont);
						baseSoundFont = customSoundFont;
					}
				}
				controller.setSynthesizer(synth,baseSoundFont,internalDrumKit,internalSynthesizers);
			}
		}
		controller.setDone(this,true);
		stop();
	}
	
	protected Soundbank loadSoundBank(String path) {
		Soundbank r = null;
		InputStream is = getClass().getResourceAsStream(path);
		try {
			if (is!=null) {
				r = MidiSystem.getSoundbank(is);
			} else {
				File file = new File(path);
				if (file.exists()) {
					r = MidiSystem.getSoundbank(file);
				}
			}
		} catch (InvalidMidiDataException e) {
			controller.showErrorMessage(this,"Failed to load internal sound font: " + path,e);
		} catch (IOException e) {
			controller.showErrorMessage(this,"Failed to load internal sound font: " + path,e);
		}
		return r;
	}
}
