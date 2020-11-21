package nl.zeesoft.zdk.test.neural;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.neural.KeyValueSDR;
import nl.zeesoft.zdk.neural.SDR;
import nl.zeesoft.zdk.neural.BasicScalarEncoder;
import nl.zeesoft.zdk.neural.processors.Classification;
import nl.zeesoft.zdk.neural.processors.Classifier;
import nl.zeesoft.zdk.neural.processors.ClassifierConfig;
import nl.zeesoft.zdk.test.util.TestObject;
import nl.zeesoft.zdk.test.util.Tester;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.Waiter;

public class TestClassifier extends TestObject {
	public TestClassifier(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestClassifier(new Tester())).runTest(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to use a *Classifier* to classify and/or predict future input values based on SDR sequences.");
		System.out.println("A *ClassifierConfig* can be used to configure the *Classifier* before initialization.");
		System.out.println("A *Classifier* will combine all input SDRs that match the specified dimension into a single footprint to associate with a certain input value.");
		System.out.println("The input value can be provided by using a *KeyValueSDR* with a key/value pair with a configurable key.");
		System.out.println("The classifications and/or predictions will be attached to the output *KeyValueSDR* using *Classification* objects.");
		System.out.println(ClassifierConfig.DOCUMENTATION);
		TestSpatialPooler.printSDRProcessorInfo();
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the classifier");
		System.out.println("Classifier cl = new Classifier();");
		System.out.println("// Initialize the classifier");
		System.out.println("cl.initialize();");
		System.out.println("// Create and build the processor chain");
		System.out.println("CodeRunnerChain processorChain = new CodeRunnerChain();");
		System.out.println("cl.buildProcessorChain(processorChain);");
		System.out.println("// Set the input (dimensions should match configured X/Y dimensions)");
		System.out.println("cl.setInput(new SDR());");
		System.out.println("// Run the processor chain");
		System.out.println("if (Waiter.startAndWaitFor(processorChain, 1000)) {");
		System.out.println("    // Get the output");
		System.out.println("    SDR output = cl.getOutput();");
		System.out.println("}");
		System.out.println("// Get a Str containing the data");
		System.out.println("Str data = cl.toStr();");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestClassifier.class));
		System.out.println(" * " + getTester().getLinkForClass(Classifier.class));
		System.out.println(" * " + getTester().getLinkForClass(ClassifierConfig.class));
		System.out.println(" * " + getTester().getLinkForClass(KeyValueSDR.class));
		System.out.println(" * " + getTester().getLinkForClass(Classification.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows an example classifier input and output SDR.  ");
		System.out.println("In this case the value is passed to the classifier using a separate key value SDR which is not shown here.  ");
	}

	@Override
	protected void test(String[] args) {
		Logger.setLoggerDebug(true);
		
		Classifier cl = new Classifier();
		ClassifierConfig config = new ClassifierConfig();
		config.sizeX = 10;
		config.sizeY = 10;
		config.maxCount = 12;
		config.logPredictionAccuracy = true;
		cl.configure(config);
		cl.initialize();
		
		CodeRunnerChain processorChain = new CodeRunnerChain();
		cl.buildProcessorChain(processorChain);
		
		List<SDR> inputList = getInputSDRList(100);
		List<KeyValueSDR> valueList = getValueSDRList(100);
		List<SDR> outputList = new ArrayList<SDR>();
		
		System.out.println();
		int num = 0;
		for (SDR input: inputList) {
			KeyValueSDR value = valueList.get(num);
			cl.setInput(input,value);
			Waiter.startAndWaitFor(processorChain, 1000);
			SDR output = cl.getOutput();
			output.sort();
			outputList.add(output);
			if (num == 99) {
				System.out.println();
				System.out.println("Input SDR: " + input.toStr());
				System.out.println("Output SDR: " + output.toStr());
			}
			num++;
		}
		
		KeyValueSDR kvSdr = (KeyValueSDR) outputList.get(outputList.size() - 1);
		Classification cls = (Classification) kvSdr.get(Classifier.CLASSIFICATION_VALUE_KEY + ":1");
		Classification cls2 = new Classification();
		
		cls2.fromStr(cls.toStr());
		assertEqual((Integer)cls2.getMostCountedValues().size(),1,"Predicted values size does not match expectation");
		assertEqual((Integer)cls2.getMostCountedValues().get(0),0,"Predicted value does not match expectation");
		assertEqual(cls2.valueCounts.get(0),160,"Predicted value count does not match expectation");
		
		cls2.valueCounts.put(1, 156);
		assertEqual(cls2.getStandardDeviation(),2.828427F,"Standard deviation does not match expectation");
		
		float accuracy = (float) kvSdr.get(Classifier.ACCURACY_VALUE_KEY);
		float accuracyTrend = (float) kvSdr.get(Classifier.ACCURACY_TREND_VALUE_KEY);
		assertEqual(accuracy, 1.0F, "Accuracy does not match expectation");
		assertEqual(accuracyTrend, 1.0F, "Accuracy trend does not match expectation");
		
		Str str = cl.toStr();
		Classifier cl2 = new Classifier();
		cl2.configure(config);
		cl2.initialize(null);
		cl2.fromStr(str);
		Str str2 = cl2.toStr();
		assertEqual(str2,str,"Classifier Str does not match expectation");
		
		int lastIndex = inputList.size() - 4;
		SDR input = inputList.get(lastIndex);
		cl2.setInput(input,valueList.get(lastIndex));
		cl2.buildProcessorChain(processorChain);
		Waiter.startAndWaitFor(processorChain, 1000);
		SDR output = cl2.getOutput();
		kvSdr = (KeyValueSDR) output;
		cls = (Classification) kvSdr.get(Classifier.CLASSIFICATION_VALUE_KEY + ":1");
		assertEqual(cls.valueCounts.get(1),176,"Predicted value count does not match expectation");
	}
	
	private List<SDR> getInputSDRList(int num) {
		BasicScalarEncoder encoder = new BasicScalarEncoder();
		encoder.setEncodeDimensions(10, 10);
		encoder.setMaxValue(3);
		encoder.setPeriodic(true);
		
		Str err = encoder.testNoOverlap();
		assertEqual(err, new Str(), "Error message does not match expectation");
		
		List<SDR> r = new ArrayList<SDR>();
		for (int i = 0; i < num; i++) {
			r.add(encoder.getEncodedValue(i));
		}
		return r;
	}
	
	private List<KeyValueSDR> getValueSDRList(int num) {
		List<KeyValueSDR> r = new ArrayList<KeyValueSDR>();
		for (int i = 0; i < num; i++) {
			KeyValueSDR sdr = new KeyValueSDR();
			sdr.setValue(i%4);
			r.add(sdr);
		}
		return r;
	}
}
