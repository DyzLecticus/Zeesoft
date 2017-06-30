package nl.zeesoft.zeetracker.test;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Control;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;
import javax.sound.sampled.TargetDataLine;

import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestLineOut extends TestObject {
	public TestLineOut(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestLineOut(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test explores the internal audio mixers.");
	}

	@Override
	protected void test(String[] args) {
		Synthesizer synth = null;
		try {
			synth = MidiSystem.getSynthesizer();
			synth.open();
		} catch (MidiUnavailableException e1) {
			e1.printStackTrace();
		}
		
		Mixer.Info[] infos = AudioSystem.getMixerInfo();
		for (int m = 0; m < infos.length; m++) {
			System.out.println("Mixer: " + infos[m]);
			Mixer mixer = AudioSystem.getMixer(infos[m]);
			try {
				mixer.open();
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
			if (mixer.isOpen()) {
				System.out.println("  controls;");
				Control[] controls = mixer.getControls();
				for (int c = 0; c < controls.length; c++) {
					System.out.println("  control: " + controls[c]);
				}
				
				System.out.println("  source lines;");
				Line.Info[] lines = mixer.getSourceLineInfo();
				for (int l = 0; l < lines.length; l++) {
					System.out.println("  source line: " + lines[l]);
				}
				
				//Port port = null;
				
				System.out.println("  target lines;");
				lines = mixer.getTargetLineInfo();
				for (int l = 0; l < lines.length; l++) {
					System.out.println("  target line: " + lines[l]);
					try {
						if (mixer.getLine(lines[l]) instanceof Port) {
							//port = (Port) mixer.getLine(lines[l]);
							//port.open();
							System.out.println("Found target port: " + lines[l]);
						} else if (mixer.getLine(lines[l]) instanceof TargetDataLine) {
							System.out.println("Found target data line: " + lines[l]);
						}
					} catch (LineUnavailableException e) {
						e.printStackTrace();
					}
				}
				
				/*
				if (port!=null && port.isOpen()) {
					synth.getChannels()[0].noteOn(60,100);
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					synth.getChannels()[0].noteOff(60,100);
					
					FloatControl volume = null;
				
					System.out.println("  port controls;");
					controls = port.getControls();
					for (int c = 0; c < controls.length; c++) {
						System.out.println("  port control: " + controls[c]);
					}
					volume = (FloatControl) port.getControl(FloatControl.Type.VOLUME);
					System.out.println("Volume: " + volume);
					volume.setValue(0.1F);
					System.out.println("Volume: " + volume);

					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					synth.getChannels()[0].noteOn(60,100);
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					synth.getChannels()[0].noteOff(60,100);
	
					if (volume!=null) {
						volume.setValue(1.0F);
					}
					
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				*/
				mixer.close();
			}
		}
		synth.close();
		System.out.println("");
	}
}
