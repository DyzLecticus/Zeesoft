package nl.zeesoft.zmmt.gui;

import java.io.File;
import java.io.IOException;

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
	private Controller		controller		= null;
	private File			soundFont		= null;

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
				controller.setSynthesizer(synth);
				if (soundFont!=null) {
					controller.setBusy(this,"Loading custom sound font",soundFont.getAbsolutePath());
					Soundbank defaultSoundbank = synth.getDefaultSoundbank();
					Soundbank customSoundbank = null;
					try {
						customSoundbank = MidiSystem.getSoundbank(soundFont);
					} catch (InvalidMidiDataException | IOException e) {
						controller.showErrorMessage(this,"Failed to load custom sound font: " + soundFont.getAbsolutePath(),e);
					}
					if (customSoundbank!=null) {
						synth.unloadAllInstruments(defaultSoundbank);
						synth.loadAllInstruments(customSoundbank);
					}
					controller.setDone(this);
				}
			}
		}
		stop();
	}
}
