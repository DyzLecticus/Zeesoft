package nl.zeesoft.zdk.dai.cache;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.dai.ObjMap;
import nl.zeesoft.zdk.dai.ObjMapComparator;
import nl.zeesoft.zdk.dai.ObjMapList;

public class CacheElements {
	public List<CacheElement>	elements	= new ArrayList<CacheElement>();
	public int					maxSize		= 1000;
	
	public CacheElements() {
		
	}
	
	public CacheElements(int maxSize) {
		this.maxSize = maxSize;
	}
	
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
	
	public CacheElement hitCache(ObjMapList baseList, ObjMap nextMap, float minSimilarity, ObjMapComparator comparator) {
		CacheElement r = null;
		if (baseList.list.size()>0) {
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
		}
		applyMaxSize();
		return r;
	}
	
	public void applyMaxSize() {
		while(elements.size()>maxSize) {
			elements.remove(elements.size() - 1);
		}
	}
}
