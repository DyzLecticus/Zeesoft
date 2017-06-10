package nl.zeesoft.zmmt.gui;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.midi.Instrument;
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
	// TODO: Create synthesizers sound font and implement
	private static final String		INTERNAL_SYNTH		= "resources/ZeeTrackerSynthesizers.sf2";
	
	private Controller				controller			= null;
	private File					soundFont			= null;
	private boolean					useInternalDrum		= false;
	private boolean					useInternalSynth	= false;

	public InitializeMidiDevicesWorker(Messenger msgr, WorkerUnion union,Controller controller,String customSoundFontFileName,boolean useInternalDrum,boolean useInternalSynth) {
		super(msgr, union);
		setSleep(1);
		this.controller = controller;
		this.useInternalDrum = useInternalDrum;
		this.useInternalSynth = useInternalSynth;
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
				}
				if (useInternalDrum) {
					replaceSoundBankInstruments(synth,INTERNAL_DRUM);
				}
				if (useInternalSynth) {
					replaceSoundBankInstruments(synth,INTERNAL_SYNTH);
				}
			}
		}
		controller.setDone(this,true);
		stop();
	}
	
	protected void replaceSoundBankInstruments(Synthesizer synth,String path) {
		Soundbank sb = loadSoundBank(path);
		if (sb!=null) {
			for (Instrument inst: sb.getInstruments()) {
				for (Instrument synthInst: synth.getLoadedInstruments()) {
					if (synthInst.getPatch().getProgram()==inst.getPatch().getProgram()) {
						synth.unloadInstrument(synthInst);
						synth.loadInstrument(inst);
					}
				}
			}
		}
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
