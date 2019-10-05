package nl.zeesoft.zdk.test.impl.htm;

import java.text.DecimalFormat;
import java.util.List;

import nl.zeesoft.zdk.htm.proc.Classification;
import nl.zeesoft.zdk.htm.stream.ClassificationStream;
import nl.zeesoft.zdk.htm.stream.StreamFactory;
import nl.zeesoft.zdk.htm.stream.StreamResult;
import nl.zeesoft.zdk.htm.stream.ValueClassifier;
import nl.zeesoft.zdk.htm.stream.ValueClassifierListener;
import nl.zeesoft.zdk.htm.util.DateTimeSDR;
import nl.zeesoft.zdk.htm.util.HistoricalFloats;
import nl.zeesoft.zdk.htm.util.SDRMap;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestValueClassifier extends TestObject implements ValueClassifierListener {
	private int						counter					= 0;
	private DecimalFormat			df						= new DecimalFormat("0.000");
	private ClassificationStream	stream					= null;
	private Classification			previousClassification	= null;
	private HistoricalFloats		averageAccuracy			= new HistoricalFloats(); 
	
	public TestValueClassifier(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestValueClassifier(new Tester())).test(args);
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
		SDRMap inputSDRMap = (SDRMap) getTester().getMockedObject(MockRegularSDRMap.class.getName());

		StreamFactory factory = new StreamFactory(1024,21);
		factory.getPredictSteps().add(1);
		System.out.println(factory.getDescription());
		
		stream = factory.getNewClassificationStream(true);
		ValueClassifier classifier = stream.getNewValueClassifier();
		classifier.addListener(this);

		System.out.println();
		incrementSleepMs(TestAnomalyDetector.testStream(stream, inputSDRMap, 60, df));

		assertEqual(averageAccuracy.average > 0.9F,true,"Prediction accuracy is lower than expected");
		
		stream.destroy();
	}

	@Override
	public void classifiedValue(StreamResult result, List<Classification> classifications) {
		counter++;
		if (previousClassification!=null) {
			DateTimeSDR inputSDR = (DateTimeSDR) result.inputSDR;
			
			Object value = inputSDR.keyValues.get(DateTimeSDR.VALUE_KEY);
			float accuracy = 0;
			Classification classification = previousClassification;
			for (Object predicted: classification.maxCountedValues) {
				if (predicted.equals(value)) {
					accuracy = 1;
					break;
				}
			}
			averageAccuracy.addFloat(accuracy);
			
			if (counter % (500) == 0) {
				System.out.println("Processed SDRs: " + counter + ", accuracy: " + df.format(averageAccuracy.average));
			}
		}
		previousClassification = classifications.get(0);
		if (counter>=5000) {
			stream.stop();
		}
	}
}
