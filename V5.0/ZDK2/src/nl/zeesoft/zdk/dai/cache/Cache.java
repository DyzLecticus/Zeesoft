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
					r.similarity = sim;
					r.results.clear();
				}
				if (sim == r.similarity) {
					r.results.add(ce);
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
}
