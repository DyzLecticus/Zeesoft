package nl.zeesoft.zdk.test.impl;

import nl.zeesoft.zdk.neural.GeneticNN;
import nl.zeesoft.zdk.neural.NeuralNet;
import nl.zeesoft.zdk.neural.TrainingProgram;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestGeneticNN extends TestObject {	
	public TestGeneticNN(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestGeneticNN(new Tester())).test(args);
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
		GeneticNN gnn = new GeneticNN(2,1,2,1,0);
		TrainingProgram tp = null;
		while (tp==null) {
			// XOR NN
			NeuralNet nn = gnn.neuralNet;
			System.out.println("Generated NN activator: " + nn.activator.getClass().getName() + ", softmax output: " + nn.softmaxOutput + ", learning rate: " + nn.learningRate + ", code length: " + gnn.code.length());
			tp = TestNeuralNet.testXORNeuralNet(nn);
			if (!tp.latestResults.success) {
				gnn.generateNewNN();
				tp = null;
				System.out.println("================================================================================");
			}
		}
	}
}
