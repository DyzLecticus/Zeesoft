package nl.zeesoft.zdk.test.impl.htm;

import java.text.DecimalFormat;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.proc.Classification;
import nl.zeesoft.zdk.htm.stream.ClassificationStream;
import nl.zeesoft.zdk.htm.stream.StreamFactory;
import nl.zeesoft.zdk.htm.stream.StreamResult;
import nl.zeesoft.zdk.htm.stream.ValueClassifier;
import nl.zeesoft.zdk.htm.stream.ValueClassifierListener;
import nl.zeesoft.zdk.htm.util.DateTimeSDR;
import nl.zeesoft.zdk.htm.util.HistoricalFloats;
import nl.zeesoft.zdk.htm.util.SDRMap;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
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
		System.out.println("This test shows how to use a *ClassificationStream* and a *ValueClassifier* to classify and/or predict values.");
		System.out.println("It uses a *StreamFactory* to create a *ClassificationStream* and then uses that to create a *ValueClassifer*.");
		System.out.println("The *ValueClassiferListener* interface can be used to listen for classifications and/or predictions.");
		System.out.println("The *ValueClassifer* creates a list of *Classification* objects for each input SDR and then passes that list to its listeners.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the stream factory");
		System.out.println("StreamFactory factory = new StreamFactory(1024,21);");
		System.out.println("// Specify the number of steps to predict (use 0 to specify classification of the current input)");
		System.out.println("factory.getPredictSteps().add(1);");
		System.out.println("// Create the stream");
		System.out.println("ClassificationStream stream = factory.getNewClassificationStream(true);");
		System.out.println("// Create the classifier");
		System.out.println("ValueClassifier classifier = stream.getNewValueClassifier();");
		System.out.println("// Attach a listener (implement the ValueClassifierListener interface)");
		System.out.println("classifier.addListener(this);");
		System.out.println("// Start the stream");
		System.out.println("stream.start();");
		System.out.println("stream.waitForStart();");
		System.out.println("// Add some values to the stream");
		System.out.println("stream.addValue(1);");
		System.out.println("stream.addValue(2);");
		System.out.println("// Remember to stop and destroy the stream after use");
		System.out.println("stream.stop();");
		System.out.println("stream.waitForStop();");
		System.out.println("stream.destroy();");
		System.out.println("~~~~");
		System.out.println();
		getTester().describeMock(MockRegularSDRMap.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestValueClassifier.class));
		System.out.println(" * " + getTester().getLinkForClass(StreamFactory.class));
		System.out.println(" * " + getTester().getLinkForClass(ClassificationStream.class));
		System.out.println(" * " + getTester().getLinkForClass(ValueClassifier.class));
		System.out.println(" * " + getTester().getLinkForClass(ValueClassifierListener.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows;  ");
		System.out.println(" * Information about the stream factory  ");
		System.out.println(" * The average prediction accuracy  ");
		System.out.println(" * Information about the stream after passing the SDR test set through it  ");
		System.out.println(" * A trimmed JSON export of stream encoder and processor state information  ");
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
		
		JsFile json = stream.toJson();
		ZStringBuilder oriJs = json.toStringBuilder();
		assertEqual(json.rootElement.children.size(),5,"Number of stream JSON elements does not match expectation");
		
		ClassificationStream streamNew = factory.getNewClassificationStream(true);
		streamNew.fromJson(json);
		ZStringBuilder newJs = streamNew.toJson().toStringBuilder();
		assertEqual(newJs.length() / 100,oriJs.length() / 100,"Stream JSON does not match expectation");
		
		JsElem procsElem = json.rootElement.getChildByName("processors");
		for (JsElem procElem: procsElem.children) {
			JsElem dataElem = procElem.getChildByName("processorData");
			ZStringBuilder value = dataElem.value.substring(0,80);
			value.append(" ...");
			dataElem.value = value;
		}
		System.out.println();
		System.out.println("Stream state JSON;");
		System.out.println(json.toStringBuilderReadFormat());
		
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
			for (Object predicted: classification.mostCountedValues) {
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
