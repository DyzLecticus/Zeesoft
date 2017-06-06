package nl.zeesoft.zmmt.test;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Transmitter;

import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestMidiLayout extends TestObject {
	public TestMidiLayout(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestMidiLayout(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test displays the available system audio resources.");
	}

	@Override
	protected void test(String[] args) {
		Synthesizer synth = null;
		try {
			synth = MidiSystem.getSynthesizer();
			synth.open();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
		if (synth!=null && synth.isOpen()) {
			Instrument[] instruments = synth.getLoadedInstruments();
			System.out.println("Synthesizer: " + synth.getDeviceInfo().getName() + " (poly: " + synth.getMaxPolyphony() + ")");
			System.out.println("Instrument: " + synth.getLoadedInstruments()[0].getName());
			System.out.println("Synthesizer instruments; ");
			for (int i = 0; i < instruments.length; i++) {
				//System.out.println("  " + i + ": "+ instruments[i].getName());
				System.out.println("  " + i + ": "+ instruments[i]);
				if (instruments[i].getName().startsWith("Organ 1")) {
					synth.getChannels()[4].programChange(0,i);
					//System.out.println("Loading " + instruments[i].getName() + " on channel 4");
				} else if (instruments[i].getName().startsWith("Synth Dru")) {
					synth.getChannels()[9].programChange(0,i);
					//synth.getChannels()[9].controlChange(arg0, arg1);
					//synth.getChannels()[9].
				}
			}
			Sequencer seq = null;
			try {
				seq = MidiSystem.getSequencer(true);
				seq.open();
			} catch (MidiUnavailableException e) {
				e.printStackTrace();
			}
			if (seq!=null && seq.isOpen()) {
				for (Transmitter t: seq.getTransmitters()) {
					System.out.println("Sequencer is connected to: " + t.getReceiver() + " (= synth: " + (t.getReceiver() == synth) + ")");
				}
			}

			synth.getChannels()[1].controlChange(8,124);
			//synth.getChannels()[1].controlChange(8,0);
			synth.getChannels()[1].controlChange(91,124);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			System.out.println("Playing ...");
			synth.getChannels()[1].noteOn(60, 127);

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			System.out.println("Playing ...");
			synth.getChannels()[4].noteOn(63, 100);

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			synth.getChannels()[1].noteOff(60);
			System.out.println("Stopped");

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			synth.getChannels()[4].noteOff(63);
			System.out.println("Stopped");

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			
			System.out.println("Playing drums ...");
			for (int i = 35; i < 81; i ++) {
				System.out.println("Playing drum: " +  i);
				for (int i2 = 0; i2 < 4; i2 ++) {
					synth.getChannels()[9].noteOn(i, 127);
					try {
						Thread.sleep(250);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					synth.getChannels()[9].noteOff(i);
				}
				if (i>=0) {
					break;
				}
			}
		}
	}
}
