package nl.zeesoft.zdk.test.neural;

import java.util.List;

import nl.zeesoft.zdk.FileIO;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.neural.KeyValueSDR;
import nl.zeesoft.zdk.neural.SDR;
import nl.zeesoft.zdk.neural.network.Network;
import nl.zeesoft.zdk.neural.network.NetworkConfig;
import nl.zeesoft.zdk.neural.network.NetworkIO;
import nl.zeesoft.zdk.neural.processors.Processor;
import nl.zeesoft.zdk.neural.processors.ProcessorIO;
import nl.zeesoft.zdk.test.util.TestObject;
import nl.zeesoft.zdk.test.util.Tester;

public class TestNetwork extends TestObject {
	public TestNetwork(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestNetwork(new Tester())).runTest(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to use a *Network* to link *Processor* instances together. ");
		System.out.println("A *NetworkConfig* instance can be used to configure the *Network* before initialization. ");
		System.out.println("A *NetworkIO* instance can be used to feed input to the network and gather all the outputs of all processors in the network. ");
		System.out.println("Non-SDR network input values will be wrapped in a *KeyValueSDR* before being passed to a processor. ");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the configuration");
		System.out.println("NetworkConfig config = new NetworkConfig();");
		System.out.println("// Define a network input");
		System.out.println("config.inputNames.add(\"input1\");");
		System.out.println("// Add some processors");
		System.out.println("config.addScalarEncoder(\"EN\");");
		System.out.println("config.addSpatialPooler(\"SP\");");
		System.out.println("config.addTemporalMemory(\"TM\");");
		System.out.println("config.addClassifier(\"CL\");");
		System.out.println("// Link the network inputs and processors");
		System.out.println("config.addLink(\"input1\", 0, \"EN\", 0);");
		System.out.println("config.addLink(\"EN\", 0, \"SP\", 0);");
		System.out.println("config.addLink(\"SP\", 0, \"TM\", 0);");
		System.out.println("config.addLink(\"TM\", 0, \"CL\", 0);");
		System.out.println("config.addLink(\"input1\", 0, \"CL\", 1);");
		System.out.println("// Create the network");
		System.out.println("Network network = new Network();");
		System.out.println("network.configure(config);");
		System.out.println("network.initialize(true);");
		System.out.println("// Use the network");
		System.out.println("NetworkIO io = new NetworkIO();");
		System.out.println("io.setValue(\"input1\", 1);");
		System.out.println("network.processIO(io);");
		System.out.println("// Save the network");
		System.out.println("network.save(\"data/\");");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestNetwork.class));
		System.out.println(" * " + getTester().getLinkForClass(Network.class));
		System.out.println(" * " + getTester().getLinkForClass(Processor.class));
		System.out.println(" * " + getTester().getLinkForClass(NetworkConfig.class));
		System.out.println(" * " + getTester().getLinkForClass(NetworkIO.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows some network debug logging and an example output.  ");
	}

	@Override
	protected void test(String[] args) {
		Logger.setLoggerDebug(true);
		
		NetworkConfig config = new NetworkConfig();
		config.inputNames.add(KeyValueSDR.DEFAULT_VALUE_KEY);
		
		config.addScalarEncoder("EN");
		config.addSpatialPooler("SP");
		config.addTemporalMemory("TM");
		config.addClassifier("CL");
		
		config.addLink(KeyValueSDR.DEFAULT_VALUE_KEY, 0, "EN", 0);
		config.addLink("EN", 0, "SP", 0);
		config.addLink("SP", 0, "TM", 0);
		config.addLink("TM", 0, "CL", 0);
		config.addLink(KeyValueSDR.DEFAULT_VALUE_KEY, 0, "CL", 1);

		Str err = config.testConfiguration();
		assertEqual(err,new Str(),"Error does not match expectation");
		
		Network network = new Network();
		network.configure(config);
		network.initialize(true);
		
		System.out.println();
		Logger.dbg(this, new Str("Processing 100 SDRs ..."));
		NetworkIO io = null;
		for (int i = 0; i < 100; i++) {
			io = new NetworkIO();
			io.setValue(KeyValueSDR.DEFAULT_VALUE_KEY, i%2);
			network.processIO(io);
			assertEqual(io.hasErrors(),false,"Network IO has unexpected errors");
		}
		Logger.dbg(this, new Str("Processed 100 SDRs"));
		
		System.out.println();
		
		network.save("dist");
		List<Str> actionLog = FileIO.getActionLog();
		assertEqual(actionLog.size(),4,"Number of actions does not match expectation");
		
		network.load("dist");
		actionLog = FileIO.getActionLog();
		assertEqual(actionLog.size(),8,"Number of actions does not match expectation");

		System.out.println();
		for (String name: io.getProcessorNames()) {
			System.out.println("Processor: " + name);
			ProcessorIO pIO = io.getProcessorIO(name);
			for (SDR output: pIO.outputs) {
				System.out.println(" > " + output);
			}
		}
	}
}
