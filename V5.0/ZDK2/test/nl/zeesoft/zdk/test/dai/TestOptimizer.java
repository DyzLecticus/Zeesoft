package nl.zeesoft.zdk.test.dai;

import nl.zeesoft.zdk.Console;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.dai.ObjMapComparator;
import nl.zeesoft.zdk.dai.ObjMapList;
import nl.zeesoft.zdk.dai.optimize.Optimizer;
import nl.zeesoft.zdk.dai.predict.KeyPrediction;
import nl.zeesoft.zdk.dai.predict.ObjMapPrediction;
import nl.zeesoft.zdk.dai.predict.Prediction;
import nl.zeesoft.zdk.dai.predict.Predictor;
import nl.zeesoft.zdk.dai.recognize.PatternRecognizer;

public class TestOptimizer {
	private static TestOptimizer	self	= new TestOptimizer();
	
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);

		assert new Prediction() != null;
		assert new ObjMapPrediction() != null;
		assert new KeyPrediction() != null;
		
		ObjMapList history = new ObjMapList();
		history.maxSize = 2000;
		
		ObjMapComparator comparator = new ObjMapComparator();
		
		PatternRecognizer patternRecognizer = new PatternRecognizer();
		patternRecognizer.generateDefaultPatternRecognizers(8);
		
		Predictor predictor = new Predictor();
		
		Optimizer optimizer = new Optimizer();

		TestPatternRecognizer.feedPattern(history);
		TestPatternRecognizer.feedPattern(history);
		TestPatternRecognizer.feedPattern(history);
		TestPatternRecognizer.feedPattern(history);
		ObjMapList errors = optimizer.getErrors(history, patternRecognizer, comparator, predictor);
		Console.log(errors);

	}
}
