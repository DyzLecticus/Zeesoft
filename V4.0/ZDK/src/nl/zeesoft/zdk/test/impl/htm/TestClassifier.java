package nl.zeesoft.zdk.test.impl.htm;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.proc.Classification;
import nl.zeesoft.zdk.htm.proc.Classifier;
import nl.zeesoft.zdk.htm.proc.ClassifierConfig;
import nl.zeesoft.zdk.htm.proc.Memory;
import nl.zeesoft.zdk.htm.proc.MemoryConfig;
import nl.zeesoft.zdk.htm.proc.Pooler;
import nl.zeesoft.zdk.htm.proc.PoolerConfig;
import nl.zeesoft.zdk.htm.util.DateTimeSDR;
import nl.zeesoft.zdk.htm.util.HistoricalFloats;
import nl.zeesoft.zdk.htm.util.SDR;
import nl.zeesoft.zdk.htm.util.SDRMap;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestClassifier extends TestObject {
	private HistoricalFloats	averageAccuracy	= new HistoricalFloats(); 
	private List<Object>		predictedValues	= new ArrayList<Object>();
	private int					counter			= 0;
	
	public TestClassifier(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestClassifier(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to use a *Classifier* to classify and/or predict values.");
		System.out.println("It is not included in the ZDK test set.");
	}
	
	@Override
	protected void test(String[] args) {
		SDRMap inputSDRMap = (SDRMap) getTester().getMockedObject(MockRegularSDRMap.class.getName());
		assertEqual(inputSDRMap.size(),15330,"Input SDR map size does not match expectation");
		
		PoolerConfig poolerConfig = new PoolerConfig(inputSDRMap.length(),1024,21);
		Pooler pooler = new Pooler(poolerConfig);
		pooler.randomizeConnections();
		
		MemoryConfig memoryConfig = new MemoryConfig(poolerConfig);
		Memory memory = new Memory(memoryConfig);
		memory.logStats = true;

		ClassifierConfig classifierConfig = new ClassifierConfig(1);
		Classifier classifier = new Classifier(classifierConfig);
		classifier.logStats = true;
		
		int num = 5000;
		
		List<DateTimeSDR> predictionSDRs = new ArrayList<DateTimeSDR>();
		
		long started = System.currentTimeMillis();
		System.out.println("Processing input SDR map (" + num + "/" + inputSDRMap.size() + ") ...");
		for (int i = 0; i < num; i++) {
			DateTimeSDR inputSDR = (DateTimeSDR) inputSDRMap.getSDR(i);
			SDR outputSDR = pooler.getSDRForInput(inputSDR,true);
			SDR activationSDR = memory.getSDRForInput(outputSDR,true);
			
			List<SDR> context = new ArrayList<SDR>();
			context.add(inputSDR);
			context.add(outputSDR);
			context.add(activationSDR);
			
			List<SDR> classifierSDRs = classifier.getSDRsForInput(activationSDR, context,true);
			if (classifierSDRs.size()>0) {
				DateTimeSDR predictionSDR = (DateTimeSDR) classifierSDRs.get(0);
				if (predictionSDR!=null) {
					processedSDR(inputSDR,predictionSDR);
					predictionSDRs.add(predictionSDR);
				}
			}
		}
		System.out.println("Processing input SDR map took: " + (System.currentTimeMillis() - started) + " ms");
		
		assertEqual(predictionSDRs.size(),num,"Activation SDR list size does not match expectation");
		
		assertEqual(averageAccuracy.average > 0.9F,true,"Prediction accuracy is lower than expected");
		
		System.out.println();
		System.out.println("Performance statistics;");
		System.out.println(classifier.statsLog.getSummary());

		ZStringBuilder strOri = classifier.toStringBuilder();
		Classifier classifierNew = new Classifier(classifierConfig);
		classifierNew.fromStringBuilder(strOri);
		ZStringBuilder strNew = classifier.toStringBuilder();
		if (!assertEqual(strNew.length(),strOri.length(),"Classifier string builder does not match expectation")) {
			System.out.println(strOri.substring(0,500));
			System.err.println(strNew.substring(0,500));
		}
		
		pooler.destroy();
		memory.destroy();
		classifier.destroy();
		classifierNew.destroy();
	}

	private void processedSDR(DateTimeSDR inputSDR,DateTimeSDR predictionSDR) {
		counter++;
		Object value = inputSDR.keyValues.get(DateTimeSDR.VALUE_KEY);
		
		float accuracy = 0;

		for (Object predicted: predictedValues) {
			if (predicted.equals(value)) {
				accuracy = 1;
				break;
			}
		}
		averageAccuracy.addFloat(accuracy);
		
		predictedValues.clear();
		ZStringBuilder predictions = new ZStringBuilder();
		Classification classification = (Classification) predictionSDR.keyValues.get(Classifier.CLASSIFICATION_KEY);
		for (Object pValue: classification.maxCountedValues) {
			if (predictions.length()>0) {
				predictions.append(", ");
			}
			predictions.append("" + pValue);
			predictedValues.add(pValue);
		}
		
		if (counter % (500) == 0) {
			System.out.println("Processed SDRs: " + counter + ", value: " + value + ", prediction(s): " + predictions + ", accuracy: " + averageAccuracy.average);
		}
	}
}
