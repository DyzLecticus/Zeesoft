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
	
	public CacheElement getElement(ObjMapList baseList, ObjMap nextInput) {
		CacheElement r = null;
		for (CacheElement ce: elements) {
			if (ce.nextMap.equals(nextInput) && ce.baseList.equals(baseList)) {
				r = ce;
				break;
			}
		}
		return r;
	}
	
	public CacheElement getElement(ObjMapList baseList, ObjMap nextMap, float minSimilarity, ObjMapComparator comparator) {
		CacheElement r = null;
		float max = 0F;
		for (CacheElement ce: elements) {
			float mapSim = comparator.calculateSimilarity(nextMap, ce.nextMap);
			if (mapSim>minSimilarity) {
				float listSim = comparator.calculateSimilarity(baseList, ce.baseList);
				if (listSim>minSimilarity && (mapSim + listSim) > max) {
					max = mapSim + listSim;
					r = ce;
				}
			}
		}
		return r;
	}
	
	public CacheElement hitCache(ObjMapList baseList, ObjMap nextMap) {
		return hitCache(baseList,nextMap,1F,null);
	}
	
	public CacheElement hitCache(ObjMapList baseList, ObjMap nextMap, float minSimilarity, ObjMapComparator comparator) {
		CacheElement r = null;
		if (minSimilarity < 1F && comparator!=null) {
			r = getElement(baseList, nextMap, minSimilarity, comparator);
		} else {
			r = getElement(baseList, nextMap);
		}
		if (r==null) {
			r = new CacheElement(baseList, nextMap);
		} else {
			elements.remove(r);
		}
		elements.add(0, r);
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
