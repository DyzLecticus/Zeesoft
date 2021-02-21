package nl.zeesoft.zdbd.test;

import java.util.List;

import nl.zeesoft.zdk.test.ZDK;
import nl.zeesoft.zdk.test.util.LibraryObject;
import nl.zeesoft.zdk.test.util.TestObject;
import nl.zeesoft.zdk.test.util.Tester;

/**
 * Documents and tests the ZDBD.
 */
public class ZDBD extends LibraryObject {
	public static String NAME	= "Zeesoft MidiDreamer";
	
	public ZDBD(Tester tester) {
		super(tester);
		setNameAbbreviated("ZDBD");
		setNameFull("Zeesoft Drum & Bass Dreamer");
		setBaseProjectUrl("https://github.com/DyzLecticus/Zeesoft/tree/master/V5.0/ZDBD/");
		setBaseReleaseUrl("https://github.com/DyzLecticus/Zeesoft/raw/master/V5.0/ZDBD/releases/");
		setBaseSrcUrl("https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDBD/");
		getDependencies().add(new ZDK(null));
	}

	public static void main(String[] args) {
		(new ZDBD(new Tester())).describeAndTest(args);
	}

	@Override
	public void describe() {
		System.out.println(NAME);
		System.out.println("===================");
		System.out.println(NAME + " is an application that can be used to generate MIDI using HTM.  ");
		System.out.println();
		describeDependencies();
		System.out.println();
		System.out.println("**Workflow**  ");
		System.out.println(" * Define a sequence of up to 4 drum, bass and chord stab MIDI patterns  ");
		System.out.println(" * Train an HTM network to learn the sequence  ");
		System.out.println(" * Generate specific variations of the sequence using the trained network  ");
		System.out.println(" * Use the sequencer to continuously play and generate new sequences  ");
		System.out.println(" * Use the arpeggiator to generate a unique solo on top  ");
		System.out.println(" * Record the performance as MIDI and/or WAV audio  ");
		System.out.println();
		System.out.println("**Architecture**  ");
		System.out.println("MidiDreamer is a locally running Java application that exposes control over its functionality through a custom HTTP web server.  ");
		System.out.println("It uses the Java MIDI Syntesizer for audio synthesis and a custom Sequencer implementation for sequencing.  ");
		System.out.println("[ZeeTracker](https://github.com/DyzLecticus/Zeesoft/tree/master/ZeeTracker#zeetracker) sound fonts are used to provide modern electronic sounds.  ");
		System.out.println();
		System.out.println("**Screen shots**  ");
		System.out.println("<img alt=\"Main application\" src=\"https://raw.githubusercontent.com/DyzLecticus/Zeesoft/master/V5.0/ZDBD/resources/screenshots/App.bmp\">");
		System.out.println();
		System.out.println("<img alt=\"Chord changes editing panel\" src=\"https://raw.githubusercontent.com/DyzLecticus/Zeesoft/master/V5.0/ZDBD/resources/screenshots/ChordChanges.bmp\">");
		System.out.println();
		System.out.println("<img alt=\"Training sequence editing panel\" src=\"https://raw.githubusercontent.com/DyzLecticus/Zeesoft/master/V5.0/ZDBD/resources/screenshots/TrainingSequence.bmp\">");
		System.out.println();
		System.out.println("<img alt=\"Network trainer and sequence generators\" src=\"https://raw.githubusercontent.com/DyzLecticus/Zeesoft/master/V5.0/ZDBD/resources/screenshots/NetworkAndGenerators.bmp\">");
		System.out.println();
		System.out.println("Click [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V5.0/ZDBD/releases/MidiDreamer" + getVersion() + ".zip) to download the latest MidiDreamer release (8 MB).  ");  
		System.out.println("**Please note** that this is an executable jar file that requires Java 1.8 and 2 GB memory to run.  ");
		System.out.println();
		describeTesting(ZDBD.class);
		System.out.println();
	}

	@Override
	public void addTests(List<TestObject> tests) {
		tests.add(new TestEncoders(getTester()));
		tests.add(new TestInstrumentNetwork(getTester()));
		tests.add(new TestGenerator(getTester()));
	}
}
