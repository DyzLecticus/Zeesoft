package nl.zeesoft.zdk.test.impl;

import java.text.DecimalFormat;

import nl.zeesoft.zdk.neural.NeuralNet;
import nl.zeesoft.zdk.neural.Training;
import nl.zeesoft.zdk.neural.TrainingProgram;
import nl.zeesoft.zdk.neural.TrainingSet;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestNeuralNet extends TestObject {	
	private DecimalFormat	df	= new DecimalFormat("0.00");
	
	public TestNeuralNet(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestNeuralNet(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		/* TODO: Describe
		System.out.println("This test shows how to use the *ZStringEncoder* to generate a key and then use that to encode and decode a text.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the ZStringEncoder");
		System.out.println("ZStringEncoder encoder = new ZStringEncoder(\"Example text to be encoded.\");");
		System.out.println("// Generate a key");
		System.out.println("String key = encoder.generateNewKey(1024);");
		System.out.println("// Use the key to encode the text");
		System.out.println("encoder.encodeKey(key,0);");
		System.out.println("// Use the key to decode the encoded text");
		System.out.println("encoder.decodeKey(key,0);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("This encoding mechanism can be used to encode and decode passwords and other sensitive data.");
		System.out.println("The minimum key length is 64. Longer keys provide stronger encoding.");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestNeuralNet.class));
		System.out.println(" * " + getTester().getLinkForClass(ZStringEncoder.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the generated key, the input text, the encoded text, and the decoded text.");
		*/
	}
	
	@Override
	protected void test(String[] args) {
		// XOR NN
		NeuralNet nn = new NeuralNet(2,1,2,1);
		nn.randomizeWeightsAndBiases();
		
		TrainingSet tSet = getTrainingSet(nn,false);
		
		nn.train(tSet);
		trainingSetToSystemOut(tSet);
		
		TrainingProgram tp = new TrainingProgram(nn,tSet);
		for (int i = 0; i < 5000; i++) {
			if (tp.train(10)) {
				break;
			}
		}
		System.out.println();
		if (assertNotNull(tp.finalResults,"Training program final results does not match expectation")) {
			trainingSetToSystemOut(tp.finalResults);
			float errorChange = tp.finalResults.averageError - tp.initialResults.averageError;
			System.out.println("Trained: " + tp.trained + ", error change: " + errorChange + ", learnedRate: " + errorChange / (float) tp.trained);
		} else {
			nn.train(tSet);
			trainingSetToSystemOut(tSet);
			float errorChange = tSet.averageError - tp.initialResults.averageError;
			System.out.println("Trained: " + tp.trained + ", error change: " + errorChange + ", change per training: " + errorChange / (float) tp.trained);
		}
	}
	
	private void trainingSetToSystemOut(TrainingSet tSet) {
		for (Training ex: tSet.trainings) {
			System.out.println(
				"Input: [" + df.format(ex.inputs[0]) + "|" + df.format(ex.inputs[1]) + "]" + 
				", output: [" + df.format(ex.outputs[0]) + "]" + 
				", expectation: [" + df.format(ex.expectations[0]) + "]" + 
				", error " + df.format(ex.errors[0]) + 
				", success: " + ex.success);
		}
		System.out.println("Average error: " + df.format(tSet.averageError) + ", success: " + tSet.success);
	}

	private TrainingSet getTrainingSet(NeuralNet nn, boolean randomize) {
		TrainingSet r = new TrainingSet();
		for (int i = 0; i<4; i++) {
			r.trainings.add(getTraining(nn,i));
		}
		if (randomize) {
			r.randomizeOrder();
		}
		return r;
	}
	
	private Training getTraining(NeuralNet nn,int index) {
		Training r = nn.getNewTraining();
		if (index==0) {
			r.inputs[0] = 0;
			r.inputs[1] = 0;
			r.expectations[0] = 0;
		} else if (index==1) {
			r.inputs[0] = 1;
			r.inputs[1] = 1;
			r.expectations[0] = 0;
		} else if (index==2) {
			r.inputs[0] = 0;
			r.inputs[1] = 1;
			r.expectations[0] = 1;
		} else if (index==3) {
			r.inputs[0] = 1;
			r.inputs[1] = 0;
			r.expectations[0] = 1;
		}
		return r;
	}
			
}
