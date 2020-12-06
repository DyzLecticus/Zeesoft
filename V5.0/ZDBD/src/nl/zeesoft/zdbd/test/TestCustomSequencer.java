package nl.zeesoft.zdbd.test;

import javax.sound.midi.Sequence;

import nl.zeesoft.zdbd.midi.MidiSys;
import nl.zeesoft.zdbd.midi.sequencer.CustomSequencer;
import nl.zeesoft.zdbd.pattern.PatternFactory;
import nl.zeesoft.zdbd.pattern.PatternSequence;
import nl.zeesoft.zdk.test.util.TestObject;
import nl.zeesoft.zdk.test.util.Tester;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.ProgressBar;
import nl.zeesoft.zdk.thread.Waiter;

public class TestCustomSequencer extends TestObject {
	public TestCustomSequencer(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestCustomSequencer(new Tester())).runTest(args);
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
		CodeRunnerChain chain = MidiSys.getCodeRunnerChainForSoundbankFiles(
			"../../V3.0/ZeeTracker/resources/ZeeTrackerSynthesizers.sf2",
			"../../V3.0/ZeeTracker/resources/ZeeTrackerDrumKit.sf2"
		);
		chain.addProgressListener(new ProgressBar("Loading soundbanks"));
		Waiter.startAndWaitFor(chain,3000);
		
		PatternSequence sequence = PatternFactory.getFourOnFloorInstrumentPatternSequence();
		Sequence midiSequence = MidiSys.convertor.generateSequenceForPatternSequence(sequence);
		
		CustomSequencer sequencer = new CustomSequencer();
		sequencer.setSynthesizer(MidiSys.synthesizer);
		sequencer.setSequence(midiSequence);
		
		sequencer.start();
		
		sleep(20000);
		
		sequencer.stop();
		
		MidiSys.closeDevices();
	}
}
