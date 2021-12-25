package nl.zeesoft.zdk.dai.recognize;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.dai.ObjMapComparator;
import nl.zeesoft.zdk.dai.ObjMapList;

public class PatternRecognizer {
	public List<ListPatternRecognizer>	patternRecognizers	= new ArrayList<ListPatternRecognizer>();

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (ListPatternRecognizer lpr: patternRecognizers) {
			if (str.length()>0) {
				str.append("\n");
			}
			str.append(lpr.toString());
		}
		return str.toString();
	}
	
	public void generateDefaultPatternRecognizers(int size) {
		generatePatternRecognizers(size - 2,size);
		patternRecognizers.add(new FibonacciPatternRecognizer(size));
		patternRecognizers.add(new PowerPatternRecognizer(size));
	}

	public void generatePatternRecognizers(int num, int depth) {
		for (int i = 0; i < num; i++) {
			ListPatternRecognizer pr = new ListPatternRecognizer();
			for (int d = 0; d < depth * (i + 1); d += (i+1)) {
				pr.indexes.add(d);
			}
			patternRecognizers.add(pr);
		}
	}
	
	public void detectPatterns(ObjMapList history, ObjMapComparator comparator) {
		for (ListPatternRecognizer lpr: patternRecognizers) {
			lpr.calculatePatternSimilarity(history, comparator);
		}
	}
}
