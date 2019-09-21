package nl.zeesoft.zdk.test.impl.htm;

import nl.zeesoft.zdk.htm.proc.BufferedPredictor;
import nl.zeesoft.zdk.htm.proc.MemoryConfig;
import nl.zeesoft.zdk.htm.proc.Pooler;
import nl.zeesoft.zdk.htm.proc.PoolerConfig;
import nl.zeesoft.zdk.htm.sdr.DateTimeSDR;
import nl.zeesoft.zdk.htm.sdr.SDRMap;
import nl.zeesoft.zdk.htm.stream.AnomalyDetectorListener;
import nl.zeesoft.zdk.htm.stream.BufferedPredictionStream;
import nl.zeesoft.zdk.htm.stream.Stream;
import nl.zeesoft.zdk.htm.stream.StreamEncoder;
import nl.zeesoft.zdk.htm.stream.StreamListener;
import nl.zeesoft.zdk.htm.stream.StreamResult;
import nl.zeesoft.zdk.htm.stream.ValueAnomalyDetector;
import nl.zeesoft.zdk.test.Tester;

public class TestValueAnomalyDetector extends TestAnomalyDetector implements StreamListener, AnomalyDetectorListener {
	private BufferedPredictionStream	stream			= null;
	private ValueAnomalyDetector		detector 		= null;
	private StreamResult				previousResult	= null;
	
	public TestValueAnomalyDetector(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestValueAnomalyDetector(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		/* TODO: Describe
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
		getTester().describeMock(MockRegularSDRMap.class.getName());
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
		SDRMap inputSDRMap = getInputSDRMap();

		PoolerConfig poolerConfig = new PoolerConfig(inputSDRMap.length(),1024,21);
		Pooler pooler = new Pooler(poolerConfig);
		pooler.randomizeConnections();
		
		MemoryConfig memoryConfig = new MemoryConfig(poolerConfig);
		
		BufferedPredictor predictor = new BufferedPredictor(memoryConfig,StreamEncoder.VALUE_KEY);
		
		stream = new BufferedPredictionStream(pooler,predictor);
		detector = stream.getNewValueAnomalyDetector("value");
		stream.addListener(this);
		detector.addListener(this);
		
		System.out.println(poolerConfig.getDescription());
		System.out.println(memoryConfig.getDescription());
		System.out.println();

		testStream(stream, inputSDRMap, 60);
		
		assertDetection();
	}

	@Override
	public void processedResult(Stream stream, StreamResult result) {
		counter++;
		if (counter % (500) == 0) {
			if (previousResult!=null && previousResult.outputSDRs.size()>3) {
				DateTimeSDR dtsP = (DateTimeSDR) previousResult.outputSDRs.get(3);
				DateTimeSDR dtsI = (DateTimeSDR) result.inputSDR;
				System.out.println("Processed SDRs: " + counter + ", average accuracy: " + df.format(detector.getAverageAccuracy()) + ", change: " + df.format(detector.getAverageAccuracyChange()) + ", predicted value: " + dtsP.keyValues.get("value") + ", input value: " + dtsI.keyValues.get("value"));
			} else {
				System.out.println("Processed SDRs: " + counter + ", average accuracy: " + df.format(detector.getAverageAccuracy()) + ", change: " + df.format(detector.getAverageAccuracyChange()));
			}
		}
		previousResult = result;
	}

	@Override
	public void detectedAnomaly(float averageAccuracy, float averageAccuracyChange, StreamResult result) {
		System.out.println("Detected anomaly at: " + result.id + ", average accuracy: " + averageAccuracy + ", change: " + averageAccuracyChange);
		numDetected = (int) result.id;
		stream.stop();
	}
}
