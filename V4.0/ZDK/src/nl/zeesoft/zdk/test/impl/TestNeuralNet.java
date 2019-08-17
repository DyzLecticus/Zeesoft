package nl.zeesoft.zdk.test.impl;

import java.text.DecimalFormat;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.functions.StaticFunctions;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.neural.NeuralNet;
import nl.zeesoft.zdk.neural.Prediction;
import nl.zeesoft.zdk.neural.Test;
import nl.zeesoft.zdk.neural.TestSet;
import nl.zeesoft.zdk.neural.TrainingProgram;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestNeuralNet extends TestObject {	
	public TestNeuralNet(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestNeuralNet(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to create, train and use a *NeuralNet*.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the neural net");
		System.out.println("NeuralNet nn = new NeuralNet(inputNeurons,hiddenLayers,hiddenNeurons,outputNeurons);");
		System.out.println("// Initialize the weights");
		System.out.println("nn.randomizeWeightsAndBiases();");
		System.out.println("nn.applyWeightFunctions();");
		System.out.println("// Get a new prediction");
		System.out.println("Prediction p = nn.getNewPrediction();");
		System.out.println("// Set the prediction inputs (0.0 - 1.0)");
		System.out.println("p.inputs[0] = 0.0F;");
		System.out.println("p.inputs[1] = 1.0F;");
		System.out.println("// Let the neural net predict the outputs");
		System.out.println("n.predict(p);");
		System.out.println("// Get a new test set");
		System.out.println("TestSet ts = nn.getNewTestSet();");
		System.out.println("// Get a new test");
		System.out.println("Test t = ts.addNewTest();");
		System.out.println("// Set the test inputs (0.0 - 1.0)");
		System.out.println("t.inputs[0] = 0.0F;");
		System.out.println("t.inputs[1] = 1.0F;");
		System.out.println("// Set the test expectations (0.0 - 1.0)");
		System.out.println("t.expectations[0] = 1.0F;");
		System.out.println("// Let the neural net predict the test outputs and calculate the error and loss");
		System.out.println("n.test(ts);");
		System.out.println("// Randomize the order of the tests");
		System.out.println("ts.randomizeOrder();");
		System.out.println("// Use the test set to train the neural net");
		System.out.println("n.train(ts);");
		System.out.println("// Repeat randomization and training until the network reaches the desired state");
		System.out.println("// ... or a maximum number of times because sometimes they fail to converge");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestNeuralNet.class));
		System.out.println(" * " + getTester().getLinkForClass(NeuralNet.class));
		System.out.println(" * " + getTester().getLinkForClass(Prediction.class));
		System.out.println(" * " + getTester().getLinkForClass(TestSet.class));
		System.out.println(" * " + getTester().getLinkForClass(Test.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows;  ");
		System.out.println(" * The test results for 2 XOR neural net implementations before and after training.  ");
		System.out.println(" * The second neural net JSON structure.  ");
	}
	
	@Override
	protected void test(String[] args) {
		// XOR test set
		TestSet tSet = getXORTestSet(false);
		JsFile tsJs = tSet.toJson();
		ZStringBuilder oriStr = tsJs.toStringBuilderReadFormat();
		TestSet tSetCopy = new TestSet(tsJs);
		ZStringBuilder newStr = tSetCopy.toJson().toStringBuilderReadFormat();
		assertEqual(newStr.equals(oriStr),true,"Test set JSON does not match expectation");
		if (!newStr.equals(oriStr)) {
			System.out.println(oriStr);
			System.err.println(newStr);
		} else {
			// XOR NN
			NeuralNet nn = new NeuralNet(2,1,2,1);
			nn.randomizeWeightsAndBiases();
			nn.applyWeightFunctions();
			testXORNeuralNet(nn,false);
	
			System.out.println("================================================================================");
			
			// XOR NN as classifier
			nn = new NeuralNet(2,1,2,1);
			nn.randomizeWeightsAndBiases();
			testXORNeuralNet(nn,true);
			
			System.out.println();
			System.out.println("Neural net JSON;");
			JsFile json = nn.toJson();
			oriStr = json.toStringBuilderReadFormat();
			System.out.println(oriStr);
			
			NeuralNet nnCopy = new NeuralNet(json);
			newStr = nnCopy.toJson().toStringBuilderReadFormat();
			assertEqual(newStr.equals(oriStr),true,"Neural net JSON does not match expectation");
			if (!newStr.equals(oriStr)) {
				System.err.println(newStr);
			}
		}
	}
	
	public static TrainingProgram testXORNeuralNet(NeuralNet nn,boolean classifier) {
		DecimalFormat df = new DecimalFormat("0.00");
		TestSet tSet = getXORTestSet(false);
		
		if (classifier) {
			nn.outputActivator = StaticFunctions.SOFTMAX_TOP;
			tSet.lossFunction = StaticFunctions.MEAN_SQUARED_ERROR;
		}
		
		if (nn.outputActivator!=null) {
			System.out.println("Neural net activator: " + nn.activator.getClass().getName() + ", output activator: " + nn.outputActivator.getClass().getName() + ", learning rate: " + nn.learningRate);
		} else {
			System.out.println("Neural net activator: " + nn.activator.getClass().getName() + ", learning rate: " + nn.learningRate);
		}
		
		nn.test(tSet);
		System.out.println("Initial test results;");
		trainingSetToSystemOut(tSet,df);
		
		TrainingProgram tp = new TrainingProgram(nn,tSet);
		for (int i = 0; i < 5000; i++) {
			if (tp.train(10)) {
				break;
			}
		}
		System.out.println("Latest test results;");
		trainingSetToSystemOut(tp.latestResults,df);
		System.out.println("Trained epochs: " + tp.trainedEpochs + ", error change rate: " + tp.getErrorChangeRate() + ", loss change rate: " + tp.getLossChangeRate());
		return tp;
	}
	
	private static void trainingSetToSystemOut(TestSet tSet,DecimalFormat df) {
		for (Test t: tSet.tests) {
			System.out.println(
				"  Input: [" + df.format(t.inputs[0]) + "|" + df.format(t.inputs[1]) + "]" + 
				", output: [" + df.format(t.outputs[0]) + "]" + 
				", expectation: [" + df.format(t.expectations[0]) + "]" + 
				", error: " + df.format(t.errors[0]) +
				", loss: " + tSet.lossFunction.calculateLoss(t.outputs,t.expectations)
				);
		}
		System.out.println("  Average error: " + df.format(tSet.averageError) + ", average loss: " + df.format(tSet.averageLoss) + ", success: " + tSet.success);
	}

	public static TestSet getXORTestSet(boolean randomize) {
		TestSet r = new TestSet(2,1);
		for (int i = 0; i<4; i++) {
			addTest(r,i);
		}
		if (randomize) {
			r.randomizeOrder();
		}
		return r;
	}
	
	private static void addTest(TestSet tSet,int index) {
		Test r = tSet.addNewTest();
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
	}
			
}
