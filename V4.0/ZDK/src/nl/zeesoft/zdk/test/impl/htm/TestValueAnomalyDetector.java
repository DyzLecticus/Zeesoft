package nl.zeesoft.zdk.test.impl.htm;

import java.util.HashMap;

import nl.zeesoft.zdk.htm.sdr.SDRMap;
import nl.zeesoft.zdk.htm.stream.BufferedPredictionStream;
import nl.zeesoft.zdk.htm.stream.Stream;
import nl.zeesoft.zdk.htm.stream.StreamEncoder;
import nl.zeesoft.zdk.htm.stream.StreamFactory;
import nl.zeesoft.zdk.htm.stream.StreamListener;
import nl.zeesoft.zdk.htm.stream.StreamResult;
import nl.zeesoft.zdk.htm.stream.ValueAnomalyDetector;
import nl.zeesoft.zdk.htm.stream.ValueAnomalyDetectorListener;
import nl.zeesoft.zdk.test.Tester;

public class TestValueAnomalyDetector extends TestAnomalyDetector implements StreamListener, ValueAnomalyDetectorListener {
	private BufferedPredictionStream	stream					= null;
	private ValueAnomalyDetector		detector	= null;
	private StreamResult				previousResult			= null;
	
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

		StreamFactory factory = new StreamFactory(1024,21);
		System.out.println(factory.getDescription());
		
		stream = factory.getNewBufferedPredictionStream(true);
		detector = stream.getNewValueAnomalyDetector();
		stream.addListener(this);
		detector.addDetectorListener(this);

		System.out.println();
		testStream(stream, inputSDRMap, 60);
		
		assertDetection();
		
		stream.destroy();
	}

	@Override
	public void processedResult(Stream stream, StreamResult result) {
		counter++;
		if (counter % (500) == 0) {
			if (previousResult!=null && previousResult.outputSDRs.size()>3) {
				System.out.println("Processed SDRs: " + counter +
					", average accuracy: " + df.format(detector.getAverageAccuracy(StreamEncoder.VALUE_KEY)) +
					", average deviation: " + df.format(detector.getAverageDeviation(StreamEncoder.VALUE_KEY)) +
					", average range accuracy: " + df.format(detector.getAverageRangeAccuracy())
				);
			}
		}
		previousResult = result;
	}

	@Override
	public void detectedAnomaly(String valueKey,HashMap<String,Object> predictedValues,float difference,StreamResult result) {
		System.out.println("Detected anomaly at: " + result.id + ", property: " + valueKey + ", difference: " + difference);
		numDetected = (int) result.id;
		if (numDetected>=numExpected) {
			stream.stop();
		}
	}
}
