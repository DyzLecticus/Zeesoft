package nl.zeesoft.zdk.test.neural;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.neural.KeyValueSDR;
import nl.zeesoft.zdk.neural.SDR;
import nl.zeesoft.zdk.neural.ScalarEncoder;
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
		// TODO: Describe
		//System.out.println("This test is not included in the ZDK test set");
	}

	@Override
	protected void test(String[] args) {
		Logger.setLoggerDebug(true);
		
		Classifier cl = new Classifier();
		ClassifierConfig config = new ClassifierConfig();
		config.sizeX = 10;
		config.sizeY = 10;
		config.maxCount = 12;
		cl.configure(config);
		cl.initialize();
		
		CodeRunnerChain processorChain = new CodeRunnerChain();
		cl.buildProcessorChain(processorChain, true);
		
		List<SDR> inputList = getInputSDRList(100);
		List<KeyValueSDR> valueList = getValueSDRList(100);
		List<SDR> outputList = new ArrayList<SDR>();
		
		int num = 0;
		for (SDR input: inputList) {
			KeyValueSDR value = valueList.get(num);
			cl.setInput(input,value);
			Waiter.startAndWaitFor(processorChain, 1000);
			SDR output = cl.getOutput();
			outputList.add(output);
			if (num == 99) {
				System.out.println("Input SDR: " + input.toStr());
				System.out.println("Output SDR: " + output.toStr());
			}
			num++;
		}
		
		KeyValueSDR kvSdr = (KeyValueSDR) outputList.get(outputList.size() - 1);
		Classification cls = (Classification) kvSdr.get(Classifier.CLASSIFICATION_VALUE_KEY + ":1");
		Classification cls2 = new Classification();
		
		cls2.fromStr(cls.toStr());
		assertEqual((Integer)cls.getMostCountedValues().get(0),0,"Predicted value does not match expectation");
		assertEqual(cls.valueCounts.get(0),96,"Predicted value count does not match expectation");
	}
	
	private List<SDR> getInputSDRList(int num) {
		ScalarEncoder encoder = new ScalarEncoder();
		encoder.setEncodeDimensions(10, 10);
		encoder.setMaxValue(4);
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
			sdr.put(Classifier.DEFAULT_VALUE_KEY, i%4);
			r.add(sdr);
		}
		return r;
	}
}
