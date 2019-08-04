package nl.zeesoft.zdk.test.impl;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.genetic.GeneticCode;
import nl.zeesoft.zdk.genetic.GeneticNN;
import nl.zeesoft.zdk.json.JsFile;
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
		System.out.println("This test shows how to use a *GeneticNN* to generate a *GeneticCode* and corresponding *NeuralNet*.");
		System.out.println("It uses a *TrainingProgram* to train and test the *NeuralNet*.");
		System.out.println("It keeps generating neural nets until it finds one that passes all the tests.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the genetic neural network");
		System.out.println("GeneticNN gnn = new GeneticNN(inputNeurons,maxHiddenLayers,maxHiddenNeurons,outputNeurons,codePropertyStart);");
		System.out.println("// Generate a new genetic code and corresponding neural network");
		System.out.println("gnn.generateNewNN();");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestGeneticNN.class));
		System.out.println(" * " + getTester().getLinkForClass(GeneticNN.class));
		System.out.println(" * " + getTester().getLinkForClass(GeneticCode.class));
		System.out.println(" * " + getTester().getLinkForClass(NeuralNet.class));
		System.out.println(" * " + getTester().getLinkForClass(TrainingProgram.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the training program outputs of one or more generated XOR neural nets.  ");
	}
	
	@Override
	protected void test(String[] args) {
		GeneticNN gnn = new GeneticNN(2,1,2,1,0);
		TrainingProgram tp = null;
		while (tp==null) {
			// XOR NN
			NeuralNet nn = gnn.neuralNet;
			tp = TestNeuralNet.testXORNeuralNet(nn,false);
			if (!tp.latestResults.success) {
				gnn.generateNewNN();
				tp = null;
				System.out.println("================================================================================");
			}
		}
		
		System.out.println();
		JsFile json = tp.toJson();
		ZStringBuilder oriStr = json.toStringBuilderReadFormat();
		
		TrainingProgram tpCopy = new TrainingProgram(gnn.neuralNet,tp.baseTestSet);
		tpCopy.fromJson(json);
		ZStringBuilder newStr = tpCopy.toJson().toStringBuilderReadFormat();
		assertEqual(newStr.equals(oriStr),true,"Training program JSON does not match expectation");
		if (!newStr.equals(oriStr)) {
			System.out.println(oriStr);
			System.err.println(newStr);
		}
	}
}
