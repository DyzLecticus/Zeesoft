package nl.zeesoft.zdk.test.impl.htm;

import nl.zeesoft.zdk.htm.proc.MemoryConfig;
import nl.zeesoft.zdk.htm.proc.Pooler;
import nl.zeesoft.zdk.htm.proc.PoolerConfig;
import nl.zeesoft.zdk.htm.proc.Predictor;
import nl.zeesoft.zdk.htm.sdr.SDRSet;
import nl.zeesoft.zdk.htm.stream.AnomalyDetector;
import nl.zeesoft.zdk.htm.stream.PredictionStream;
import nl.zeesoft.zdk.htm.stream.Stream;
import nl.zeesoft.zdk.htm.stream.StreamListener;
import nl.zeesoft.zdk.htm.stream.StreamResult;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestAnomalyDetector extends TestObject implements StreamListener {
	private int				counter			= 0;
	private AnomalyDetector	detector 		= null;
	
	public TestAnomalyDetector(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestAnomalyDetector(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		/*
		System.out.println("This test shows how to use a *Memory* instance to learn temporal sequences of SDRs.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the configuration");
		System.out.println("MemoryConfig config = new MemoryConfig(1024);");
		System.out.println("// Create the memory");
		System.out.println("Memory memory = new Memory(config);");
		System.out.println("// Obtain the output SDR for a certain input SDR");
		System.out.println("SDR sdr = memory.getSDRForInput(new SDR(),true);");
		System.out.println("~~~~");
		System.out.println();
		getTester().describeMock(MockRegularSDRSet.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestAnomalyDetector.class));
		System.out.println(" * " + getTester().getLinkForClass(SDR.class));
		System.out.println(" * " + getTester().getLinkForClass(MemoryConfig.class));
		System.out.println(" * " + getTester().getLinkForClass(Memory.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows;  ");
		System.out.println(" * How memory column bursting is reduced after leaning several sequences  ");
		System.out.println(" * Information about the memory after passing the SDR test set through it  ");
		*/
	}
	
	@Override
	protected void test(String[] args) {
		SDRSet inputSDRSet = (SDRSet) getTester().getMockedObject(MockRealSDRSet.class.getName());
		assertEqual(inputSDRSet.size(),17521,"Input SDR set size does not match expectation");
		
		PoolerConfig poolerConfig = new PoolerConfig(inputSDRSet.width(),1024,21);
		Pooler pooler = new Pooler(poolerConfig);
		pooler.randomizeConnections();
		
		MemoryConfig memoryConfig = new MemoryConfig(poolerConfig);
		
		Predictor predictor = new Predictor(memoryConfig);
		
		PredictionStream stream = new PredictionStream(null,null,pooler,predictor);
		detector = stream.getNewAnomalyDetector();
		stream.addListener(this);

		stream.start();
		System.out.println("Started stream");
		
		for (int i = 0; i < inputSDRSet.size(); i++) {
			stream.addSDR(inputSDRSet.get(i));
		}
		
		sleep(60000);
		
		stream.stop();
		System.out.println("Stopping stream ...");
		
		stream.waitForStop();
		System.out.println("Stopped stream");
	}

	@Override
	public void processedResult(Stream stream, StreamResult r) {
		counter++;
		if (counter % (500) == 0) {
			System.out.println("Processed SDRs: " + counter + ", average accuracy: " + detector.getAverageAccuracy());
		}
	}
}
