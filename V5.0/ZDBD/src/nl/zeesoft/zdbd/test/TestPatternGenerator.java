package nl.zeesoft.zdbd.test;

import javax.sound.midi.Sequence;

import nl.zeesoft.zdbd.midi.MidiSys;
import nl.zeesoft.zdbd.neural.NetworkConfigFactory;
import nl.zeesoft.zdbd.neural.PatternGenerator;
import nl.zeesoft.zdbd.pattern.InstrumentPattern;
import nl.zeesoft.zdbd.pattern.PatternFactory;
import nl.zeesoft.zdbd.pattern.PatternSequence;
import nl.zeesoft.zdk.FileIO;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.neural.network.Network;
import nl.zeesoft.zdk.neural.network.NetworkConfig;
import nl.zeesoft.zdk.neural.network.NetworkIO;
import nl.zeesoft.zdk.test.util.TestObject;
import nl.zeesoft.zdk.test.util.Tester;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.ProgressBar;
import nl.zeesoft.zdk.thread.Waiter;

public class TestPatternGenerator extends TestObject {
	public TestPatternGenerator(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestPatternGenerator(new Tester())).runTest(args);
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
		Logger.setLoggerDebug(true);
				
		NetworkConfig config = NetworkConfigFactory.getNetworkConfig();
		config.directory = "dist";
		
		Network network = new Network();
		if (!config.fileExists()) {
			System.out.println(config.getDescription());
			network = TestInstrumentNetwork.createAndTrainNetwork();
		} else {
			FileIO.mockIO = false;
			network.initializeAndLoad(config);
			FileIO.mockIO = true;
		}
		
		NetworkIO lastIO = network.getLastIO();
		assertNotNull(lastIO,"Last network IO does not match expectation");
		
		if (lastIO!=null) {
			System.out.println();
			MidiSys.initialize();
			
			// Load soundbanks
			CodeRunnerChain chain = MidiSys.getCodeRunnerChainForSoundbankFiles(
				"../../V3.0/ZeeTracker/resources/ZeeTrackerSynthesizers.sf2",
				"../../V3.0/ZeeTracker/resources/ZeeTrackerDrumKit.sf2"
			);
			chain.addProgressListener(new ProgressBar("Loading soundbanks"));
			Waiter.startAndWaitFor(chain,3000);
			
			PatternSequence sequence = PatternFactory.getFourOnFloorInstrumentPatternSequence();

			Sequence midiSequence = null;
			midiSequence = MidiSys.convertor.generateSequenceForPatternSequence(sequence);
			MidiSys.sequencePlayer.setSequence(midiSequence);
			MidiSys.sequencePlayer.start();
			
			PatternGenerator generator = new PatternGenerator();
			generator.prevIO = lastIO;
			generator.skipInstruments.add(InstrumentPattern.RIDE);
			generator.skipInstruments.add(InstrumentPattern.CYMBAL);
			generator.skipInstruments.add(InstrumentPattern.BASS);
	
			sequence = generator.generatePatternSequence(network, sequence);
			midiSequence = MidiSys.convertor.generateSequenceForPatternSequence(sequence);
			MidiSys.sequencePlayer.setNextSequence(midiSequence);
			MidiSys.sequencePlayer.start();
			sleep(30000);
			MidiSys.sequencePlayer.stop();
					
			MidiSys.closeDevices();
		}
	}
}
