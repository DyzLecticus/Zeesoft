package nl.zeesoft.zso.test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Control;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Port;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestSystemAudioResources extends TestObject {
	public TestSystemAudioResources(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestSystemAudioResources(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test displays the available system audio resources.");
	}

	@Override
	protected void test(String[] args) {
		InputStream is = null;
		try {
			is = new FileInputStream("samples/Snare.wav");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (is!=null) {
	        AudioInputStream stream = null;
			try {
				stream = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
			} catch (UnsupportedAudioFileException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (stream!=null) {
				TargetDataLine line = null;
				AudioFormat format = stream.getFormat();
				DataLine.Info info = new DataLine.Info(TargetDataLine.class,format);
				try {
				    line = (TargetDataLine) AudioSystem.getLine(info);
				    line.open(format);
				} catch (LineUnavailableException e) {
					e.printStackTrace();
				}
				if (line!=null && line.isOpen()) {
					System.out.println("Got WAV audio line: " + line);
					Control[] controls = line.getControls();
					if (controls.length>0) {
						System.out.println("Line controls; ");
						for (Control control: controls) {
							System.out.println("  - " + control.getType());
						}
					}
					line.close();
				}
			}
		}
		Line line = null;
		try {
			line = (Port) AudioSystem.getLine(Port.Info.SPEAKER);
			line.open();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		if (line!=null && line.isOpen()) {
			System.out.println("Got speaker line: " + line);
			Control[] controls = line.getControls();
			if (controls.length>0) {
				System.out.println("Line controls; ");
				for (Control control: controls) {
					System.out.println("  - " + control.getType());
				}
			}
			line.close();
		}
		Synthesizer synth = null;
		/*
		try {
			synth = MidiSystem.getSynthesizer();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
		if (synth!=null) {
			System.out.println("Default synthesizer instruments; ");
			Instrument[] instruments = synth.getLoadedInstruments();
			for (Instrument instr: instruments) {
				System.out.println("  - " + instr.getName());
			}
		}
		*/
		MidiDevice.Info[] devices = MidiSystem.getMidiDeviceInfo();
		for (MidiDevice.Info info: devices) {
			MidiDevice device = null;
			try {
				device = MidiSystem.getMidiDevice(info);
			} catch (MidiUnavailableException e) {
				e.printStackTrace();
			}
			if (device!=null && device instanceof Synthesizer) {
				try {
					device.open();
				} catch (MidiUnavailableException e) {
					e.printStackTrace();
				}
				if (device.isOpen()) {
					synth = (Synthesizer) device;
					System.out.println("Synthesizer: " + device.getDeviceInfo().getName() + " (poly: " + synth.getMaxPolyphony() + ")");
					System.out.println("Instrument: " + synth.getLoadedInstruments()[0].getName());
					System.out.println("Synthesizer instruments; ");
					Instrument[] instruments = synth.getLoadedInstruments();
					for (int i = 0; i < instruments.length; i++) {
						System.out.println("  - " + instruments[i].getName());
						if (instruments[i].getName().startsWith("Organ 1")) {
							synth.getChannels()[4].programChange(0,i);
							System.out.println("Loading " + instruments[i].getName() + " on channel 4");
						}
					}

				    /*
				    try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				    ShortMessage myMsg = new ShortMessage();
				    // Play the note Middle C (48) moderately loud
				    // (velocity = 93) on channel 4 (zero-based).
				    try {
						myMsg.setMessage(ShortMessage.NOTE_ON, 4, 36, 127);
					} catch (InvalidMidiDataException e) {
						e.printStackTrace();
					} 
				    Receiver synthRcvr = null;
					try {
						synthRcvr = synth.getReceiver();
					} catch (MidiUnavailableException e) {
						e.printStackTrace();
					}
					if (synthRcvr!=null) {
					    synthRcvr.send(myMsg, -1); // -1 means no time stamp					
					    
					    try {
							myMsg.setMessage(ShortMessage.NOTE_ON, 4, 79, 44);
						} catch (InvalidMidiDataException e) {
							e.printStackTrace();
						} 
					    synthRcvr.send(myMsg, -1); // -1 means no time stamp					
					    try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

					    try {
							myMsg.setMessage(ShortMessage.NOTE_OFF, 4, 79);
						} catch (InvalidMidiDataException e) {
							e.printStackTrace();
						} 
					    synthRcvr.send(myMsg, -1); // -1 means no time stamp
					    try {
							myMsg.setMessage(ShortMessage.NOTE_OFF, 4, 36);
						} catch (InvalidMidiDataException e) {
							e.printStackTrace();
						} 
					    synthRcvr.send(myMsg, -1); // -1 means no time stamp
					    try {
							myMsg.setMessage(ShortMessage.NOTE_OFF, 4, 0);
						} catch (InvalidMidiDataException e) {
							e.printStackTrace();
						} 
					    synthRcvr.send(myMsg, -1); // -1 means no time stamp
					    System.out.println("Sent note off");
					    try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					    //synthRcvr.close();
					}
				    
				    try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				    */

					System.out.println("Playing ...");
					synth.getChannels()[1].noteOn(60, 100);

				    try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					System.out.println("Playing ...");
					synth.getChannels()[4].noteOn(63, 100);

				    try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				    synth.getChannels()[1].noteOff(60);
					System.out.println("Stopped");

				    try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				    synth.getChannels()[4].noteOff(63);
					System.out.println("Stopped");

				    try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					device.close();
					
}
			}
		}
	}
}
