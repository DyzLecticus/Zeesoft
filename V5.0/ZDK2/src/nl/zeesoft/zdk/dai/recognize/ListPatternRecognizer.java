package nl.zeesoft.zdk.dai.recognize;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.dai.ObjMapComparator;
import nl.zeesoft.zdk.dai.ObjMapList;

public class ListPatternRecognizer {
	public List<Integer>			indexes			= new ArrayList<Integer>();
	
	public ObjMapList				baseList		= null;
	public float					similarity		= 0F;
	public List<Integer>			startIndexes	= new ArrayList<Integer>();
	
	@Override
	public String toString() {
		return indexes + " -> " + startIndexes + " = " + similarity;
	}
	
	public float calculatePatternSimilarity(ObjMapList history, ObjMapComparator comparator, int maxDepth) {
		baseList = history.getSubList(0,indexes);
		similarity = 0F;
		startIndexes.clear();
		int max = history.list.size();
		if (max > maxDepth) {
			max = maxDepth;
		}
		for (int s = 1; s < max; s++) {
			checkSublist(history, comparator, baseList, s);
		}
		return similarity;
	}
	
	public void checkSublist(ObjMapList history, ObjMapComparator comparator, ObjMapList baseList, int start) {
		ObjMapList subList = history.getSubList(start, indexes);
		float sim = comparator.calculateSimilarity(baseList, subList);
		if (sim>0F) {
			if (sim > similarity) {
				similarity = sim;
				startIndexes.clear();
			}
			if (sim == similarity) {
				startIndexes.add(start);
			}
		}
	}
}
