package nl.zeesoft.zdk.test.impl;

import java.text.DecimalFormat;

import nl.zeesoft.zdk.neural.Exercise;
import nl.zeesoft.zdk.neural.ExerciseSet;
import nl.zeesoft.zdk.neural.NeuralNet;
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
		
		DecimalFormat df = new DecimalFormat("0.00");
		
		ExerciseSet exSet = getExerciseSet(nn,false);
		
		nn.train(exSet);
		
		for (Exercise ex: exSet.exercises) {
			System.out.println(
				"Input: [" + df.format(ex.inputs[0]) + "|" + df.format(ex.inputs[1]) + "]" + 
				", output: [" + df.format(ex.outputs[0]) + "]" + 
				", expectation: [" + df.format(ex.expectations[0]) + "]" + 
				", error " + df.format(ex.errors[0]) + 
				", success: " + ex.success);
		}
		
		for (int i = 0; i < 10000; i++) {
			ExerciseSet exSet2 = exSet.copy();
			exSet2.randomizeOrder();
			nn.train(exSet2);
		}

		nn.train(exSet);
		
		for (Exercise ex: exSet.exercises) {
			System.out.println(
				"Input: [" + df.format(ex.inputs[0]) + "|" + df.format(ex.inputs[1]) + "]" + 
				", output: [" + df.format(ex.outputs[0]) + "]" + 
				", expectation: [" + df.format(ex.expectations[0]) + "]" + 
				", error " + df.format(ex.errors[0]) + 
				", success: " + ex.success);
		}
	}

	private ExerciseSet getExerciseSet(NeuralNet nn, boolean randomize) {
		ExerciseSet r = new ExerciseSet();
		for (int i = 0; i<4; i++) {
			r.exercises.add(getExercise(nn,i));
		}
		if (randomize) {
			r.randomizeOrder();
		}
		return r;
	}
	
	private Exercise getExercise(NeuralNet nn,int index) {
		Exercise r = nn.getNewExercise();
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
