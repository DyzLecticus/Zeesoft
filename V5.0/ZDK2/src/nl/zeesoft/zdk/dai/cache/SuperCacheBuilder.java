package nl.zeesoft.zdk.dai.cache;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.dai.ObjMapComparator;

public class SuperCacheBuilder {
	public Cache buildSuperCache(Cache from, ObjMapComparator comparator, float mergeSimilarity) {
		Cache r = new Cache();
		r.indexes = from.indexes;
		mergeElements(from, r, comparator, mergeSimilarity);
		return r;
	}
	
	public void mergeElements(Cache from, Cache to, ObjMapComparator comparator, float mergeSimilarity) {
		List<CacheElement> merge = new ArrayList<CacheElement>();
		List<CacheElement> orderedByCount = orderByCount(from.elements);
		for (CacheElement ce: orderedByCount) {
			if (!merge.contains(ce)) {
				List<CacheElement> mergeElements = getMergeElementsForElement(ce, orderedByCount, comparator, mergeSimilarity);
				if (mergeElements.size()>0) {
					merge.addAll(mergeElements);
					CacheElement nce = new CacheElement();
					nce.count = ce.count;
					nce.baseList = ce.baseList;
					nce.nextMap = ce.nextMap;
					to.elements.add(nce);
					for (CacheElement mce: mergeElements) {
						nce.count += mce.count;
					}
				} else {
					to.elements.add(ce);
				}
			}
		}
	}
	
	public List<CacheElement> getMergeElementsForElement(CacheElement ce, List<CacheElement> orderedByCount, ObjMapComparator comparator, float mergeSimilarity) {
		List<CacheElement> r = new ArrayList<CacheElement>();
		for (CacheElement ceC: orderedByCount) {
			if (ceC!=ce) {
				float sim = comparator.calculateSimilarity(ce.nextMap, ceC.nextMap);
				if (sim>mergeSimilarity) {
					sim = comparator.calculateSimilarity(ce.baseList, ceC.baseList);
					if (sim>mergeSimilarity) {
						r.add(ceC);
					}
				}
			}
		}
		return r;
	}
	
	public List<CacheElement> orderByCount(List<CacheElement> elements) {
		List<CacheElement> r = new ArrayList<CacheElement>();
		for (CacheElement ce: elements) {
			boolean added = false;
			for (CacheElement rce: r) {
				if (ce.count >= rce.count) {
					r.add(r.indexOf(rce), ce);
					added = true;
					break;
				}
			}
			if (!added) {
				r.add(ce);
			}
		}
		return r;
	}
}
