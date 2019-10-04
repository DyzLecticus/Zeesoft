package nl.zeesoft.zdk.test.impl.htm;

import java.text.DecimalFormat;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.proc.Memory;
import nl.zeesoft.zdk.htm.proc.Pooler;
import nl.zeesoft.zdk.htm.proc.StatsLog;
import nl.zeesoft.zdk.htm.stream.AnomalyDetectorListener;
import nl.zeesoft.zdk.htm.stream.AnomalyDetector;
import nl.zeesoft.zdk.htm.stream.DefaultStream;
import nl.zeesoft.zdk.htm.stream.Stream;
import nl.zeesoft.zdk.htm.stream.StreamFactory;
import nl.zeesoft.zdk.htm.stream.StreamListener;
import nl.zeesoft.zdk.htm.stream.StreamResult;
import nl.zeesoft.zdk.htm.util.SDRMap;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestAnomalyDetector extends TestObject implements StreamListener, AnomalyDetectorListener {
	protected int				counter			= 0;
	protected DecimalFormat		df				= new DecimalFormat("0.000");
	protected int				numDetected		= 0;
	
	protected int				numExpected		= 0;
	
	private DefaultStream		stream			= null;
	private AnomalyDetector		detector 		= null;
	
	public TestAnomalyDetector(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestAnomalyDetector(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to use an *AnomalyDetector* to detect anomalies in an SDR *Stream*.");
		System.out.println("It uses a *StreamFactory* to create a *PredictionStream* and then uses that to create an *AnomalyDetector*.");
		System.out.println("The *AnomalyDetectorListener* interface can be used to listen for anomaly detections.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the stream factory");
		System.out.println("StreamFactory factory = new StreamFactory(1024,21);");
		System.out.println("// Configure the stream to output the burst SDR");
		System.out.println("factory.setOutputActivationSDR(false);");
		System.out.println("// Create the stream");
		System.out.println("DefaultStream stream = factory.getNewDefaultStream(true);");
		System.out.println("// Create the anomaly detector");
		System.out.println("AnomalyDetector detector = stream.getNewAnomalyDetector();");
		System.out.println("// Attach a listener (implement the AnomalyDetectorListener interface)");
		System.out.println("detector.addListener(this);");
		System.out.println("// Start the stream");
		System.out.println("stream.start();");
		System.out.println("stream.waitForStart();");
		System.out.println("// Add some values to the stream (include an anomaly after 5000 inputs)");
		System.out.println("stream.addValue(1);");
		System.out.println("stream.addValue(2);");
		System.out.println("// Remember to stop and destroy the stream after use");
		System.out.println("stream.stop();");
		System.out.println("stream.waitForStop();");
		System.out.println("stream.destroy();");
		System.out.println("~~~~");
		System.out.println();
		getTester().describeMock(MockAnomalySDRMap.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestAnomalyDetector.class));
		System.out.println(" * " + getTester().getLinkForClass(StreamFactory.class));
		System.out.println(" * " + getTester().getLinkForClass(DefaultStream.class));
		System.out.println(" * " + getTester().getLinkForClass(AnomalyDetector.class));
		System.out.println(" * " + getTester().getLinkForClass(AnomalyDetectorListener.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows;  ");
		System.out.println(" * Information about the stream factory  ");
		System.out.println(" * A JSON export of the stream factory  ");
		System.out.println(" * The average prediction accuracy  ");
		System.out.println(" * The detected anomaly  ");
		System.out.println(" * Information about the stream after passing the SDR test set through it  ");
	}
	
	@Override
	protected void test(String[] args) {
		SDRMap inputSDRMap = getInputSDRMap();

		StreamFactory factory = new StreamFactory(1024,21);
		System.out.println(factory.getDescription());

		System.out.println("");
		System.out.println("Stream factory JSON;");
		System.out.println(factory.toJson().toStringBuilderReadFormat());
		
		stream = factory.getNewDefaultStream(true);
		detector = stream.getNewAnomalyDetector();
		stream.addListener(this);
		detector.addListener(this);

		System.out.println();
		testStream(stream, inputSDRMap, 60);
		
		assertDetection();
		
		JsFile json = stream.toJson();
		ZStringBuilder oriJs = json.toStringBuilderReadFormat();
		
		DefaultStream streamNew = factory.getNewDefaultStream(false);
		streamNew.fromJson(json);
		ZStringBuilder newJs = streamNew.toJson().toStringBuilderReadFormat();
		
		assertEqual(oriJs.length(),newJs.length(),"Stream JSON does not match expectation");

		StreamFactory factoryOri = new StreamFactory(2304,46);
		factoryOri.getEncoder().setScale(4);
		factoryOri.getEncoder().setEncodeProperties(false,false,false,true,true,true);
		factoryOri.getEncoder().setValueMinMax(0,50);
		factoryOri.setDepth(8);
		factoryOri.setBoostStrength(10);
		factoryOri.setValueKey("test");
		
		StreamFactory factoryNew = new StreamFactory(1024,21);
		this.testJsAble(factoryOri, factoryNew, "Factory JSON does not match expectation");
		
		stream.destroy();
	}
	
	protected SDRMap getInputSDRMap() {
		SDRMap inputSDRMap = (SDRMap) getTester().getMockedObject(MockAnomalySDRMap.class.getName());
		assertEqual(inputSDRMap.size(),15330,"Input SDR map size does not match expectation");
		
		numExpected = (inputSDRMap.size() / 2) + 1; // Add 1 because result ID counter starts at 1
		System.out.println("Test set anomaly detection is expected at: " + numExpected);
		System.out.println();
		
		return inputSDRMap;
	}

	protected void assertDetection() {
		assertEqual(numDetected >= numExpected && numDetected < numExpected + 48,true,"Failed to detect the expected anomaly");
	}

	protected void testStream(Stream stream,SDRMap inputSDRMap, int maxSeconds) {
		stream.setLogStats(true);
		
		long started = System.currentTimeMillis();
		stream.start();
		stream.waitForStart();
		System.out.println("Started stream");

		for (int i = 0; i < inputSDRMap.size(); i++) {
			stream.addSDR(inputSDRMap.getSDR(i));
		}
		
		int i = 0;
		while(stream.isWorking()) {
			sleep(100);
			i++;
			if (i >= maxSeconds * 10) {
				break;
			}
		}
		
		stream.stop();
		stream.waitForStop();
		long streamMs = (System.currentTimeMillis() - started);
		System.out.println("Stopped stream after " + streamMs + " ms");
		
		List<StatsLog> statsLogs = stream.getStats();
		long totalNs = 0;
		for (StatsLog statsLog: statsLogs) {
			System.out.println();
			System.out.println(statsLog.source.getClass().getSimpleName() + ";");
			System.out.println(statsLog.getSummary());
			if (statsLog.source instanceof Pooler || statsLog.source instanceof Memory) {
				totalNs += statsLog.getTotals().get("total") / statsLog.log.size();
			}
		}
		
		System.out.println();
		System.out.println("Total processing time per SDR: " + df.format(totalNs / 1000000F) + " ms");
		System.out.println("Total stream time per SDR:     " + df.format(streamMs / (float) statsLogs.get(0).log.size()) + " ms");
	}
	
	@Override
	public void processedResult(Stream stream, StreamResult result) {
		counter++;
		if (counter % (500) == 0) {
			System.out.println("Processed SDRs: " + counter + ", average accuracy: " + df.format(detector.getAverageAccuracy()) + ", latest: " + df.format(detector.getLatestAccuracy()));
		}
	}

	@Override
	public void detectedAnomaly(float averageAccuracy, float latestAccuracy, float difference, StreamResult result) {
		System.out.println("Detected anomaly at: " + result.id + ", average accuracy: " + averageAccuracy + ", latest: " + latestAccuracy + ", difference: " + difference);
		numDetected = (int) result.id;
		if (numDetected>=numExpected) {
			stream.stop();
		}
	}
}
