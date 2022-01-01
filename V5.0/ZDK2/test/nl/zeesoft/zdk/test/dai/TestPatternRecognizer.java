package nl.zeesoft.zdk.test.dai;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.dai.History;
import nl.zeesoft.zdk.dai.ObjMapComparator;
import nl.zeesoft.zdk.dai.Prediction;
import nl.zeesoft.zdk.dai.recognize.PatternRecognizer;

public class TestPatternRecognizer {
	private static TestPatternRecognizer	self	= new TestPatternRecognizer();
	
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);

		History history = new History(2000);
		
		ObjMapComparator comparator = new ObjMapComparator();
		
		PatternRecognizer recognizer = new PatternRecognizer();
		recognizer.generateDefaultPatternRecognizers();
		assert recognizer.patternRecognizers.size() == 4;
		
		recognizer.detectPatterns(history, comparator);
		Logger.debug(self, "Recognizers;\n" + recognizer);
		assert recognizer.toString().startsWith("[0, 1, 2, 3, 4, 5, 6, 7] -> [] = 0.0");
		assert recognizer.toString().endsWith("[0, 1, 3, 9, 27, 81, 243, 729] -> [] = 0.0");
		assert recognizer.toString().length() == 158;
		
		history.addAll(TestHistory.getPattern());
		recognizer.detectPatterns(history, comparator);
		Logger.debug(self, "Recognizers;\n" + recognizer);
		assert recognizer.toString().startsWith("[0, 1, 2, 3, 4, 5, 6, 7] -> [8] = 0.9583334");
		assert recognizer.toString().endsWith("[0, 1, 3, 9, 27, 81, 243, 729] -> [4] = 0.8333334");
		
		history.addAll(TestHistory.getPattern());
		recognizer.detectPatterns(history, comparator);
		Logger.debug(self, "Recognizers;\n" + recognizer);
		assert recognizer.toString().startsWith("[0, 1, 2, 3, 4, 5, 6, 7] -> [16] = 1.0");
		assert recognizer.toString().endsWith("[0, 1, 3, 9, 27, 81, 243, 729] -> [4, 16] = 0.8");
		
		history.addAll(TestHistory.getPattern());
		recognizer.detectPatterns(history, comparator);
		Logger.debug(self, "Recognizers;\n" + recognizer);
		assert recognizer.toString().endsWith("[0, 1, 3, 9, 27, 81, 243, 729] -> [16] = 1.0");
		
		Prediction prediction = recognizer.getPrediction(history, recognizer);
		Logger.debug(self, "Prediction;\n" + prediction);
	}
}
