package nl.zeesoft.zdk.dai.recognize;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.dai.ObjMapComparator;
import nl.zeesoft.zdk.dai.ObjMapList;

public class ListPatternRecognizer {
	public List<Integer>			indexes			= new ArrayList<Integer>();
	
	public float					similarity		= 0F;
	public List<Integer>			startIndexes	= new ArrayList<Integer>();
	
	public float					accuracy		= 1F;
	public float					weight			= 1F;
	
	@Override
	public String toString() {
		return indexes + " -> " + startIndexes + " = " + similarity;
	}
	
	public float calculatePatternSimilarity(ObjMapList history, ObjMapComparator comparator, int maxDepth) {
		similarity = 0F;
		startIndexes.clear();
		ObjMapList baseList = getSubList(history, 0);
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
		ObjMapList subList = getSubList(history, start);
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
	
	public ObjMapList getSubList(ObjMapList history, int start) {
		ObjMapList subList = new ObjMapList();
		for (Integer index: indexes) {
			int i = start + index;
			if (i < history.list.size()) {
				subList.list.add(history.list.get(i));
			}
		}
		return subList;
	}			
}
