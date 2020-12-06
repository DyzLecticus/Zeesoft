package nl.zeesoft.zdbd.test;

import java.io.File;

import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Track;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import com.sun.media.sound.AudioSynthesizer;

import nl.zeesoft.zdbd.midi.MidiSys;
import nl.zeesoft.zdbd.pattern.PatternFactory;
import nl.zeesoft.zdbd.pattern.PatternSequence;
import nl.zeesoft.zdk.test.util.TestObject;
import nl.zeesoft.zdk.test.util.Tester;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.ProgressBar;
import nl.zeesoft.zdk.thread.Waiter;

public class TestMidiSys extends TestObject {
	public TestMidiSys(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestMidiSys(new Tester())).runTest(args);
	}

	@Override
	protected void describe() {
		/* TODO: Describe
		System.out.println("This test shows how a *Str* instance can be used to split a comma separated string into a list of *Str* instances. ");
		System.out.println("The *Str* class is designed to add features of the Java String to a Java StringBuilder. ");
		System.out.println("It also contains methods for file writing and reading. ");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the Str");
		System.out.println("Str str = new Str(\"qwer,asdf,zxcv\");");
		System.out.println("// Split the Str");
		System.out.println("List<Str> strs = str.split(\",\");");
		System.out.println("~~~~");
		System.out.println();
		getTester().describeMock(MockStr.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestController.class));
		System.out.println(" * " + getTester().getLinkForClass(Str.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the string input and lists the *Str* objects.  ");
		*/
	}

	@Override
	protected void test(String[] args) {
		MidiSys.initialize();
		PatternSequence sequence = PatternFactory.getFourOnFloorInstrumentPatternSequence();
		Sequence midiSequence = MidiSys.convertor.generateSequenceForPatternSequence(sequence);
		MidiSys.closeDevices();
		
		File file = new File("dist/sequence.wav");
		render(midiSequence, file);
	}

	/*
	 * Render sequence into wave audio file.
	 */
	public static void render(Sequence sequence,File file) {
		try {
			// Find available AudioSynthesizer.
			AudioSynthesizer synth = findAudioSynthesizer();
			if (synth == null) {
				System.out.println("No AudioSynhtesizer was found!");
				System.exit(1);
			}
			
			// Open AudioStream from AudioSynthesizer.
			AudioInputStream stream = synth.openStream(null, null);

			MidiSys.synthesizer = synth;
			CodeRunnerChain chain = MidiSys.getCodeRunnerChainForSoundbankFiles(
				"../../V3.0/ZeeTracker/resources/ZeeTrackerSynthesizers.sf2",
				"../../V3.0/ZeeTracker/resources/ZeeTrackerDrumKit.sf2"
			);
			chain.addProgressListener(new ProgressBar("Loading soundbanks"));
			Waiter.startAndWaitFor(chain,3000);
			
			// Play Sequence into AudioSynthesizer Receiver.
			double total = send(sequence, synth.getReceiver());

			// Calculate how long the WAVE file needs to be.
			long len = (long) (stream.getFormat().getFrameRate() * (total + 4));
			stream = new AudioInputStream(stream, stream.getFormat(), len);

			// Write WAVE file to disk.
			AudioSystem.write(stream, AudioFileFormat.Type.WAVE, file);

			// We are finished, close synthesizer.
			synth.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Find available AudioSynthesizer.
	 */
	public static AudioSynthesizer findAudioSynthesizer()
			throws MidiUnavailableException {
		// First check if default synthesizer is AudioSynthesizer.
		Synthesizer synth = MidiSystem.getSynthesizer();
		if (synth instanceof AudioSynthesizer)
			return (AudioSynthesizer) synth;
		/*
		// If default synhtesizer is not AudioSynthesizer, check others.
		Info[] infos = MidiSystem.getMidiDeviceInfo();
		for (int i = 0; i < infos.length; i++) {
			MidiDevice dev = MidiSystem.getMidiDevice(infos[i]);
			if (dev instanceof AudioSynthesizer)
				return (AudioSynthesizer) dev;
		}
		*/

		// No AudioSynthesizer was found, return null.
		return null;
	}

	/*
	 * Send entiry MIDI Sequence into Receiver using timestamps.
	 */
	public static double send(Sequence seq, Receiver recv) {
		float divtype = seq.getDivisionType();
		//assert (seq.getDivisionType() == Sequence.PPQ);
		Track[] tracks = seq.getTracks();
		int[] trackspos = new int[tracks.length];
		int mpq = 500000;
		int seqres = seq.getResolution();
		long lasttick = 0;
		long curtime = 0;
		while (true) {
			MidiEvent selevent = null;
			int seltrack = -1;
			for (int i = 0; i < tracks.length; i++) {
				int trackpos = trackspos[i];
				Track track = tracks[i];
				if (trackpos < track.size()) {
					MidiEvent event = track.get(trackpos);
					if (selevent == null
							|| event.getTick() < selevent.getTick()) {
						selevent = event;
						seltrack = i;
					}
				}
			}
			if (seltrack == -1)
				break;
			trackspos[seltrack]++;
			long tick = selevent.getTick();
			if (divtype == Sequence.PPQ)
				curtime += ((tick - lasttick) * mpq) / seqres;
			else
				curtime = (long) ((tick * 1000000.0 * divtype) / seqres);
			lasttick = tick;
			MidiMessage msg = selevent.getMessage();
			if (msg instanceof MetaMessage) {
				if (divtype == Sequence.PPQ)
					if (((MetaMessage) msg).getType() == 0x51) {
						byte[] data = ((MetaMessage) msg).getData();
						mpq = ((data[0] & 0xff) << 16)
								| ((data[1] & 0xff) << 8) | (data[2] & 0xff);
					}
			} else {
				if (recv != null)
					recv.send(msg, curtime);
			}
		}
		return curtime / 1000000.0;
	}

}
