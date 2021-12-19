package nl.zeesoft.zdk.im.pattern;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.im.ObjectArrayList;
import nl.zeesoft.zdk.im.SimilarityCalculator;

public class PatternRecognizer {
	public int				max			= 32;
	public List<Integer>	indexes		= new ArrayList<Integer>();
	
	public PatternRecognizerResult calculatePatternSimilarity(SimilarityCalculator calculator, ObjectArrayList history) {
		PatternRecognizerResult r = new PatternRecognizerResult();
		ObjectArrayList baseList = getSubList(0, history);
		for (int s = 1; s < history.list.size(); s++) {
			ObjectArrayList subList = getSubList(s, history);
			float sim = calculator.calculateSimilarity(baseList, subList);
			if (sim>0F) {
				if (sim > r.similarity) {
					r.similarity = sim;
					r.startIndexes.clear();
				}
				if (sim == r.similarity) {
					r.startIndexes.add(s);
				}
			}
		}
		return r;
	}
	
	public ObjectArrayList getSubList(int start, ObjectArrayList history) {
		ObjectArrayList subList = new ObjectArrayList();
		for (Integer index: indexes) {
			int i = start + index;
			if (i < history.list.size()) {
				subList.list.add(history.list.get(i));
				if (subList.list.size()>=max) {
					break;
				}
			}
		}
		return subList;
	}
}
