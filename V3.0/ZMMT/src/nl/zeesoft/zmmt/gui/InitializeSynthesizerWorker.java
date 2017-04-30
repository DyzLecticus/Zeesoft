package nl.zeesoft.zmmt.gui;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class InitializeSynthesizerWorker extends Worker {
	private Controller				controller		= null;

	public InitializeSynthesizerWorker(Messenger msgr, WorkerUnion union,Controller controller) {
		super(msgr, union);
		setSleep(1);
		this.controller = controller;
	}
	
	@Override
	public void whileWorking() {
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
		stop();
	}
}
