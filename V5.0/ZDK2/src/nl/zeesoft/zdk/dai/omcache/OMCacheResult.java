package nl.zeesoft.zdk.dai.omcache;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.dai.ObjMapList;

public class OMCacheResult {
	public float					similarity		= 0F;
	public List<OMCacheElement>		elements		= new ArrayList<OMCacheElement>();
	public OMCacheResult			secondary		= null;
	
	public List<OMCacheResult>		subResults		= new ArrayList<OMCacheResult>();
	
	public OMCacheResult() {
		
	}
	
	public OMCacheResult(List<OMCacheElement> elements, float similarity) {
		this.elements.addAll(elements);
		this.similarity = similarity;
	}
	
	public boolean addSimilarElement(OMCacheElement element, float sim) {
		boolean r = false;
		if (sim > similarity) {
			similarity = sim;
			elements.clear();
		}
		if (sim == similarity) {
			elements.add(element);
			r = true;
		}
		return r;
	}
	
	public void addSubResults(ObjMapList key, float minSimilarity) {
		for (OMCacheElement ce: elements) {
			OMCacheResult subResult = ce.subCache.lookup(key, minSimilarity);
			if (subResult.elements.size()>0) {
				subResults.add(subResult);
			}
		}
	}
}
