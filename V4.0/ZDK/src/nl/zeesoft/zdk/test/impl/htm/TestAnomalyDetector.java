package nl.zeesoft.zdk.test.impl.htm;

import java.text.DecimalFormat;
import java.util.List;

import nl.zeesoft.zdk.htm.proc.MemoryConfig;
import nl.zeesoft.zdk.htm.proc.MemoryStats;
import nl.zeesoft.zdk.htm.proc.Pooler;
import nl.zeesoft.zdk.htm.proc.PoolerConfig;
import nl.zeesoft.zdk.htm.proc.PoolerStats;
import nl.zeesoft.zdk.htm.proc.Predictor;
import nl.zeesoft.zdk.htm.proc.StatsObject;
import nl.zeesoft.zdk.htm.sdr.SDRSet;
import nl.zeesoft.zdk.htm.stream.AnomalyDetector;
import nl.zeesoft.zdk.htm.stream.AnomalyDetectorListener;
import nl.zeesoft.zdk.htm.stream.PredictionStream;
import nl.zeesoft.zdk.htm.stream.Stream;
import nl.zeesoft.zdk.htm.stream.StreamListener;
import nl.zeesoft.zdk.htm.stream.StreamResult;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestAnomalyDetector extends TestObject implements StreamListener, AnomalyDetectorListener {
	protected int				counter			= 0;
	protected DecimalFormat		df				= new DecimalFormat("0.000");
	protected int				numDetected		= 0;
	
	private int					numExpected		= 0;
	
	private PredictionStream	stream			= null;
	private AnomalyDetector		detector 		= null;
	
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
		SDRSet inputSDRSet = getInputSDRSet();

		PoolerConfig poolerConfig = new PoolerConfig(inputSDRSet.width(),1024,21);
		Pooler pooler = new Pooler(poolerConfig);
		pooler.randomizeConnections();
		
		MemoryConfig memoryConfig = new MemoryConfig(poolerConfig);
		
		Predictor predictor = new Predictor(memoryConfig);
		
		stream = new PredictionStream(null,null,pooler,predictor);
		detector = stream.getNewAnomalyDetector();
		stream.addListener(this);
		detector.addListener(this);
		
		System.out.println(poolerConfig.getDescription());
		System.out.println(memoryConfig.getDescription());
		System.out.println();

		testPredictionStream(stream, inputSDRSet);
		
		assertDetection();
	}
	
	protected SDRSet getInputSDRSet() {
		SDRSet inputSDRSet = (SDRSet) getTester().getMockedObject(MockAnomalySDRSet.class.getName());
		assertEqual(inputSDRSet.size(),17521,"Input SDR set size does not match expectation");
		
		numExpected = (inputSDRSet.size() / 2) + 1;
		System.out.println("Test set anomaly detection is expected after: " + numExpected);
		System.out.println();
		
		return inputSDRSet;
	}

	protected void assertDetection() {
		assertEqual(numDetected > numExpected && numDetected < numExpected + 48,true,"Failed to detect the expected anomaly");
	}

	protected void testPredictionStream(Stream stream,SDRSet inputSDRSet) {
		long started = System.currentTimeMillis();
		stream.start();
		System.out.println("Started stream");

		for (int i = 0; i < inputSDRSet.size(); i++) {
			stream.addSDR(inputSDRSet.get(i));
		}
		
		int i = 0;
		while(stream.isWorking()) {
			sleep(100);
			i++;
			if (i >= 600) {
				break;
			}
		}
		
		stream.stop();
		stream.waitForStop();
		long streamMs = (System.currentTimeMillis() - started);
		System.out.println("Stopped stream after " + streamMs + " ms");
		
		List<StatsObject> stats = stream.getStats();
		long totalMs = 0;
		for (StatsObject stat: stats) {
			System.out.println();
			System.out.println(stat.getClass().getSimpleName() + ";");
			System.out.println(stat.getDescription());
			if (stat instanceof PoolerStats || stat instanceof MemoryStats) {
				totalMs += stat.totalNs / 1000000;
			}
		}
		
		System.out.println();
		System.out.println("Total processing time " + totalMs + " ms");
		if (streamMs < totalMs) {
			System.out.println("Net stream processing performance gain over sequential processing per input SDR: " + df.format((totalMs - streamMs) / (float) stats.get(0).total) + " ms");
		}
	}
	
	@Override
	public void processedResult(Stream stream, StreamResult result) {
		counter++;
		if (counter % (500) == 0) {
			System.out.println("Processed SDRs: " + counter + ", average accuracy: " + df.format(detector.getAverageAccuracy()) + ", change: " + df.format(detector.getAverageAccuracyChange()));
		}
	}

	@Override
	public void detectedAnomaly(float averageAccuracy, float averageAccuracyChange, StreamResult result) {
		System.out.println("Detected anomaly at: " + result.id + ", average accuracy: " + averageAccuracy + ", change: " + averageAccuracyChange);
		numDetected = (int) result.id;
		stream.stop();
	}
}
