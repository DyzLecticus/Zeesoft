package nl.zeesoft.zdk.test.im;

import java.util.List;

import nl.zeesoft.zdk.Console;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.im.Machine;
import nl.zeesoft.zdk.im.ObjectArray;
import nl.zeesoft.zdk.im.SimilarityCalculator;
import nl.zeesoft.zdk.im.pattern.FibonacciPatternRecognizer;
import nl.zeesoft.zdk.im.pattern.PatternRecognizer;
import nl.zeesoft.zdk.im.pattern.PatternRecognizerPrediction;
import nl.zeesoft.zdk.im.pattern.PatternRecognizerResult;
import nl.zeesoft.zdk.im.pattern.PowerPatternRecognizer;

public class TestMachine {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);

		ObjectArray mi = new ObjectArray(0.1F, -2F);
		testSimilarity(mi, mi, 1F);
		testSimilarity(mi, new ObjectArray(0.1F, -2.1F), 0.9883721F);
		testSimilarity(mi, new ObjectArray(0.1F, 3F), 0.7222222F);
		testSimilarity(mi, new ObjectArray(0.1F, 40F), 0.54347825F);
		testSimilarity(mi, new ObjectArray(0.1F), 0.5F);

		testSimilarity(mi, new ObjectArray(null, "Pizza", 5), 0F);
		mi = new ObjectArray(null, "Pizza");
		testSimilarity(mi, new ObjectArray(null, "Pizza", 5), 0.6666667F);
		testSimilarity(mi, new ObjectArray(null, "Pizz", 5), 0.33333334F);
		testSimilarity(mi, new ObjectArray(null, 1, 5), 0.33333334F);
		testSimilarity(mi, new ObjectArray("Pizza", null), 0F);
		testSimilarity(new ObjectArray(), new ObjectArray(), 1F);
		
		Machine machine = new Machine();
		machine.generatePatternRecognizers(4, 8);
		machine.patternRecognizers.add(new PowerPatternRecognizer(8));
		machine.patternRecognizers.add(new FibonacciPatternRecognizer(8));
		for (PatternRecognizer pr: machine.patternRecognizers) {
			Console.log(pr.indexes);
		}
		
		feedPattern(machine);
		runPatternRecognizers(machine);
		
		feedPattern(machine);
		runPatternRecognizers(machine);
		
		feedPattern(machine);
		runPatternRecognizers(machine);
		
		feedPattern(machine);
		runPatternRecognizers(machine);
	}
	
	private static void testSimilarity(ObjectArray a, ObjectArray b, float expectedSimilarity) {
		SimilarityCalculator calculator = new SimilarityCalculator();
		//Console.log(calculator.calculateSimilarity(a, b));
		assert calculator.calculateSimilarity(a, b) == expectedSimilarity;
	}
	
	private static void feedPattern(Machine machine) {
		machine.addInput(new ObjectArray(2F, 1F, 0F));
		machine.addInput(new ObjectArray(0F, 0.5F, 0F));
		machine.addInput(new ObjectArray(0F, 2F, 0F));
		machine.addInput(new ObjectArray(0F, 0.5F, 0F));
		machine.addInput(new ObjectArray(2F, 1F, 2F));
		machine.addInput(new ObjectArray(0F, 0.5F, 0F));
		machine.addInput(new ObjectArray(0F, 2F, 0F));
		machine.addInput(new ObjectArray(0F, 0.5F, 0F));
		machine.addInput(new ObjectArray(2F, 1F, 0F));
		machine.addInput(new ObjectArray(0F, 0.5F, 0F));
		machine.addInput(new ObjectArray(0F, 2F, 0F));
		machine.addInput(new ObjectArray(0F, 0.5F, 0F));
		machine.addInput(new ObjectArray(2F, 1F, 2F));
		machine.addInput(new ObjectArray(0F, 0.5F, 0F));
		machine.addInput(new ObjectArray(1F, 2F, 0F));
		machine.addInput(new ObjectArray(0F, 0.5F, 0F));		
	}
	
	private static void runPatternRecognizers(Machine machine) {
		Console.log("");
		List<PatternRecognizerResult> results = machine.runPatternRecognizers(0F);
		int i = 0;
		for (PatternRecognizerResult result: results) {
			PatternRecognizer pr = machine.patternRecognizers.get(i);
			Console.log(pr.indexes + " = " + result.startIndexes + " " + result.similarity);
			for (PatternRecognizerPrediction prediction: result.predictions) {
				Console.log(" -> " + prediction.predictedInput + ": " + prediction.votes + " = " + prediction.confidence);
			}
			i++;
		}
	}
}
