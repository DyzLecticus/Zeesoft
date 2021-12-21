package nl.zeesoft.zdk.im.pattern;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.im.ObjectArrayList;
import nl.zeesoft.zdk.im.SimilarityCalculator;

public class PatternRecognizer {
	public List<Integer>	indexes		= new ArrayList<Integer>();
	
	public PatternRecognizerResult calculatePatternSimilarity(SimilarityCalculator calculator, ObjectArrayList history) {
		PatternRecognizerResult r = new PatternRecognizerResult();
		r.patternRecognizer = this;
		ObjectArrayList baseList = getSubList(0, history);
		for (int s = 1; s < history.list.size(); s++) {
			checkSublist(calculator, history, baseList, s, r);
		}
		return r;
	}
	
	public void checkSublist(SimilarityCalculator calculator, ObjectArrayList history, ObjectArrayList baseList, int start, PatternRecognizerResult r) {
		ObjectArrayList subList = getSubList(start, history);
		float sim = calculator.calculateSimilarity(baseList, subList);
		if (sim>0F) {
			if (sim > r.similarity) {
				r.similarity = sim;
				r.startIndexes.clear();
			}
			if (sim == r.similarity) {
				r.startIndexes.add(start);
			}
		}
	}
	
	public ObjectArrayList getSubList(int start, ObjectArrayList history) {
		ObjectArrayList subList = new ObjectArrayList();
		for (Integer index: indexes) {
			int i = start + index;
			if (i < history.list.size()) {
				subList.list.add(history.list.get(i));
			}
		}
		return subList;
	}
}
