package nl.zeesoft.zmmt.gui;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class InitializeMidiDevicesWorker extends Worker {
	private Controller				controller		= null;

	public InitializeMidiDevicesWorker(Messenger msgr, WorkerUnion union,Controller controller) {
		super(msgr, union);
		setSleep(1);
		this.controller = controller;
	}
	
	@Override
	public void whileWorking() {
		Sequencer seq = null;
		try {
			seq = MidiSystem.getSequencer(true);
			if (seq!=null) {
				seq.open();
				if (seq.isOpen()) {
					seq.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
					controller.setSequencer(seq);
				}
			}
		} catch (MidiUnavailableException e) {
			controller.showErrorMessage(this,"Failed to initialize MIDI sequencer",e);
		}
		if (seq!=null) {
			if (seq instanceof Synthesizer) {
				controller.setSynthesizer((Synthesizer) seq);
			} else {
				Synthesizer synth = null;
				try {
					synth = MidiSystem.getSynthesizer();
					if (synth!=null) {
						synth.open();
						if (synth.isOpen()) {
							controller.setSynthesizer(synth);
						}
					}
				} catch (MidiUnavailableException e) {
					controller.showErrorMessage(this,"Failed to initialize MIDI syntesizer",e);
				}
			}
		}
		stop();
	}
}
