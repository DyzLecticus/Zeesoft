package nl.zeesoft.zdbd.test;

import javax.sound.midi.Sequence;

import nl.zeesoft.zdbd.midi.MidiSys;
import nl.zeesoft.zdbd.neural.Generator;
import nl.zeesoft.zdbd.neural.NetworkConfigFactory;
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

public class TestGenerator extends TestObject {
	public TestGenerator(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestGenerator(new Tester())).runTest(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how a *Generator* can be used to generate pattern sequences using a trained *Network* and the original training *PatternSequence*. ");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the network configuration");
		System.out.println("NetworkConfig config = NetworkConfigFactory.getNetworkConfig();");
		System.out.println("// Create and load the trained network");
		System.out.println("Network network = new Network();");
		System.out.println("network.initializeAndLoad(config);");
		System.out.println("// Get the training sequence");
		System.out.println("PatternSequence sequence = PatternFactory.getFourOnFloorInstrumentPatternSequence();");
		System.out.println("// Create the generator");
		System.out.println("Generator generator = new Generator();");
		System.out.println("// Generate a new pattern sequence");
		System.out.println("generator.generatePatternSequence(network, network.getLastIO(), sequence);");
		System.out.println("PatternSequence generated = generator.generatedPatternSequence;");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(Generator.class));
		System.out.println(" * " + getTester().getLinkForClass(Network.class));
		System.out.println(" * " + getTester().getLinkForClass(PatternSequence.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the network and generator debug log.  ");
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
			
			if (args==null || args.length==0) {
				System.out.println();
				MidiSys.initialize();
				CodeRunnerChain chain = MidiSys.getCodeRunnerChainForSoundbankFiles(
					"../../V3.0/ZeeTracker/resources/ZeeTrackerSynthesizers.sf2",
					"../../V3.0/ZeeTracker/resources/ZeeTrackerDrumKit.sf2"
				);
				chain.addProgressListener(new ProgressBar("Loading soundbanks"));
				Waiter.startAndWaitFor(chain,3000);
			}
			
			System.out.println();
			PatternSequence sequence = PatternFactory.getFourOnFloorInstrumentPatternSequence();
			Generator generator = new Generator();
			generator.generatePatternSequence(network, lastIO, sequence);
			PatternSequence generated = generator.generatedPatternSequence;
			assertEqual(generated.patterns.size(),3,"Number of generated patterns does not match expectation");
			assertEqual(generated.patterns.get(0).isEmpty(),false,"Generated pattern content does not match expectation");
			
			if (args==null || args.length==0) {
				Sequence midiSequence = null;
				midiSequence = sequence.toDefaultMidiSequence();
				MidiSys.sequencer.setTempoInBPM(sequence.rythm.beatsPerMinute);
				MidiSys.sequencer.setSequence(midiSequence);
				
				midiSequence = generated.toDefaultMidiSequence();
				MidiSys.sequencer.setNextSequence(midiSequence);
				
				MidiSys.sequencer.start();
				sleep(20000);
				MidiSys.sequencer.stop();
						
				MidiSys.closeDevices();
			}
		}
	}
}
