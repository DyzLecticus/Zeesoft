package nl.zeesoft.zdk.test.dai;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.dai.ObjMap;
import nl.zeesoft.zdk.dai.ObjMapComparator;
import nl.zeesoft.zdk.dai.ObjMapList;
import nl.zeesoft.zdk.dai.recognize.PatternRecognizer;

public class TestPatternRecognizer {
	private static TestPatternRecognizer	self	= new TestPatternRecognizer();
	
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);

		ObjMapList history = new ObjMapList();
		history.maxSize = 2000;
		
		ObjMapComparator comparator = new ObjMapComparator();
		
		PatternRecognizer recognizers = new PatternRecognizer();
		recognizers.generateDefaultPatternRecognizers();
		assert recognizers.patternRecognizers.size() == 8;
		
		recognizers.detectPatterns(history, comparator);
		Logger.debug(self, "Recognizers;\n" + recognizers);
		assert recognizers.toString().startsWith("[0, 1, 2, 3, 4, 5, 6, 7] -> [] = 0.0");
		assert recognizers.toString().endsWith("[0, 1, 2, 4, 8, 16, 32, 64] -> [] = 0.0");
		assert recognizers.toString().length() == 324;
		
		feedPattern(history);
		recognizers.detectPatterns(history, comparator);
		Logger.debug(self, "Recognizers;\n" + recognizers);
		assert recognizers.toString().startsWith("[0, 1, 2, 3, 4, 5, 6, 7] -> [8] = 0.9583334");
		assert recognizers.toString().endsWith("[0, 1, 2, 4, 8, 16, 32, 64] -> [6] = 0.95555556");
		
		feedPattern(history);
		recognizers.detectPatterns(history, comparator);
		Logger.debug(self, "Recognizers;\n" + recognizers);
		assert recognizers.toString().startsWith("[0, 1, 2, 3, 4, 5, 6, 7] -> [16] = 1.0");
		assert recognizers.toString().endsWith("[0, 1, 2, 4, 8, 16, 32, 64] -> [6, 14] = 0.9629629");
		
		feedPattern(history);
		recognizers.detectPatterns(history, comparator);
		Logger.debug(self, "Recognizers;\n" + recognizers);
		assert recognizers.toString().endsWith("[0, 1, 2, 4, 8, 16, 32, 64] -> [6, 14] = 0.96825397");
	}

	public static List<ObjMap> getPattern() {
		List<ObjMap> r = new ArrayList<ObjMap>();
		r.add(new ObjMap(2F, 1F, 0F));
		r.add(new ObjMap(0F, 0.5F, 0F));
		r.add(new ObjMap(0F, 2F, 0F));
		r.add(new ObjMap(0F, 0.5F, 0F));
		r.add(new ObjMap(2F, 1F, 2F));
		r.add(new ObjMap(0F, 0.5F, 0F));
		r.add(new ObjMap(0F, 2F, 0F));
		r.add(new ObjMap(0F, 0.5F, 0F));
		r.add(new ObjMap(2F, 1F, 0F));
		r.add(new ObjMap(0F, 0.5F, 0F));
		r.add(new ObjMap(0F, 2F, 0F));
		r.add(new ObjMap(0F, 0.5F, 0F));
		r.add(new ObjMap(2F, 1F, 2F));
		r.add(new ObjMap(0F, 0.5F, 0F));
		r.add(new ObjMap(1F, 2F, 0F));
		r.add(new ObjMap(0F, 0.5F, 0F));		
		return r;
	}
	
	public static void feedPattern(ObjMapList history) {
		history.addAll(getPattern());
	}
}
