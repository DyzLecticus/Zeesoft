package nl.zeesoft.zdk.test.dai;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.dai.ObjMap;
import nl.zeesoft.zdk.dai.ObjMapComparator;
import nl.zeesoft.zdk.dai.ObjMapList;
import nl.zeesoft.zdk.dai.predict.KeyPrediction;
import nl.zeesoft.zdk.dai.predict.ObjMapPrediction;
import nl.zeesoft.zdk.dai.predict.Prediction;
import nl.zeesoft.zdk.dai.predict.Predictor;
import nl.zeesoft.zdk.dai.recognize.PatternRecognizer;

public class TestPredictor {
	private static TestPredictor	self	= new TestPredictor();
	
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

		TestPatternRecognizer.feedPattern(history);
		patternRecognizer.detectPatterns(history, comparator);
		Prediction prediction = predictor.generatePrediction(history, patternRecognizer);
		Logger.debug(self, "Prediction;\n" + prediction);
		Logger.debug(self, "Predicted map;" + prediction.getPredictedMap() + ", confidences: " + prediction.getPredictedMapConfidences());
		
		assert prediction.getPredictedMap().equals(new ObjMap(2.0F, 2.0F, 0.0F));
		
		TestPatternRecognizer.feedPattern(history);
		patternRecognizer.detectPatterns(history, comparator);
		prediction = predictor.generatePrediction(history, patternRecognizer);
		Logger.debug(self, "Prediction;\n" + prediction);
		Logger.debug(self, "Predicted map; " + prediction.getPredictedMap() + ", confidences: " + prediction.getPredictedMapConfidences());
		
		assert prediction.getPredictedMap().equals(new ObjMap(2.0F, 1.0F, 0.0F));
	}
}
