package nl.zeesoft.zdk.dai.cache;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.dai.ObjMap;
import nl.zeesoft.zdk.dai.ObjMapComparator;
import nl.zeesoft.zdk.dai.ObjMapList;

public class Cache {
	public List<Integer>		indexes		= CacheIndexesGenerator.generate();
	public List<CacheElement>	elements	= new ArrayList<CacheElement>();
	
	public CacheResult getCacheResult(ObjMapList baseList, ObjMapComparator comparator, float minSimilarity) {
		CacheResult r = new CacheResult();
		for (CacheElement ce: elements) {
			float sim = comparator.calculateSimilarity(baseList, ce.baseList);
			if (sim>=minSimilarity) {
				if (sim > r.similarity) {
					if (r.results.size()>0) {
						r.secondary = new CacheResult(r.results, r.similarity);
					}
				}
				if (!r.addSimilarElement(ce, sim) && r.secondary!=null) {
					r.secondary.addSimilarElement(ce, sim);
				}
			}
		}
		return r;
	}
	
	public CacheElement getCache(ObjMapList baseList, ObjMap nextInput) {
		CacheElement r = null;
		for (CacheElement ce: elements) {
			if (ce.nextMap.equals(nextInput) && ce.baseList.equals(baseList)) {
				r = ce;
				break;
			}
		}
		return r;
	}
	
	public CacheElement hitCache(ObjMapList baseList, ObjMap nextInput) {
		CacheElement r = getCache(baseList, nextInput);
		if (r==null) {
			r = new CacheElement(baseList, nextInput);
			elements.add(r);
		}
		r.count++;
		return r;
	}
	
	public Cache copy() {
		Cache r = new Cache();
		r.indexes.clear();
		r.indexes.addAll(indexes);
		for (CacheElement ce: elements) {
			r.elements.add(ce.copy());
		}
		return r;
	}
}
