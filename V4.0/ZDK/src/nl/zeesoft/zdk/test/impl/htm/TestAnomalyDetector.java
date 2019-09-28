package nl.zeesoft.zdk.test.impl.htm;

import java.text.DecimalFormat;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.proc.Memory;
import nl.zeesoft.zdk.htm.proc.Pooler;
import nl.zeesoft.zdk.htm.proc.StatsLog;
import nl.zeesoft.zdk.htm.sdr.SDRMap;
import nl.zeesoft.zdk.htm.stream.AnomalyDetector;
import nl.zeesoft.zdk.htm.stream.AnomalyDetectorListener;
import nl.zeesoft.zdk.htm.stream.PredictionStream;
import nl.zeesoft.zdk.htm.stream.Stream;
import nl.zeesoft.zdk.htm.stream.StreamFactory;
import nl.zeesoft.zdk.htm.stream.StreamListener;
import nl.zeesoft.zdk.htm.stream.StreamResult;
import nl.zeesoft.zdk.json.JsFile;
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
		
		stream = factory.getNewPredictionStream();
		detector = stream.getNewAnomalyDetector();
		stream.addListener(this);
		detector.addListener(this);

		System.out.println();
		testStream(stream, inputSDRMap, 60);
		
		assertDetection();
		
		JsFile json = stream.toJson();
		ZStringBuilder oriJs = json.toStringBuilderReadFormat();
		
		PredictionStream streamNew = factory.getNewPredictionStream();
		streamNew.fromJson(json);
		ZStringBuilder newJs = streamNew.toJson().toStringBuilderReadFormat();
		
		assertEqual(oriJs.length(),newJs.length(),"Stream JSON does not match expectation");
		
		stream.destroy();
	}
	
	protected SDRMap getInputSDRMap() {
		SDRMap inputSDRMap = (SDRMap) getTester().getMockedObject(MockAnomalySDRMap.class.getName());
		assertEqual(inputSDRMap.size(),17521,"Input SDR map size does not match expectation");
		
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
		stream.stop();
	}
}
