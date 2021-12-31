package nl.zeesoft.zdk.dai;

import java.util.ArrayList;
import java.util.List;

public class History extends ObjMapList {
	public List<Integer>		cacheIndexes	= new ArrayList<Integer>();
	public List<HistoryCache>	cache			= new ArrayList<HistoryCache>();
	
	public History() {
		
	}
	
	public History(int maxSize) {
		super(maxSize);
	}

	@Override
	protected void addMap(ObjMap map) {
		super.addMap(map);
		if (list.size()>1 && cacheIndexes.size()>0) {
			hitCache(getSubList(1,cacheIndexes),map);
		}
	}
	
	public CacheResult getCacheResult(ObjMapComparator comparator, float minSimilarity) {
		return getCacheResult(getSubList(0, cacheIndexes), comparator, minSimilarity);
	}
	
	public CacheResult getCacheResult(ObjMapList baseList, ObjMapComparator comparator, float minSimilarity) {
		CacheResult r = new CacheResult();
		for (HistoryCache c: cache) {
			float sim = comparator.calculateSimilarity(baseList, c.baseList);
			if (sim>=minSimilarity) {
				if (sim > r.similarity) {
					r.similarity = sim;
					r.results.clear();
				}
				if (sim == r.similarity) {
					r.results.add(c);
				}
			}
		}
		return r;
	}
	
	public HistoryCache getCache(ObjMapList baseList, ObjMap nextInput) {
		HistoryCache r = null;
		for (HistoryCache c: cache) {
			if (c.nextMap.equals(nextInput) && c.baseList.equals(baseList)) {
				r = c;
				break;
			}
		}
		return r;
	}
	
	public HistoryCache hitCache(ObjMapList baseList, ObjMap nextInput) {
		HistoryCache r = getCache(baseList, nextInput);
		if (r==null) {
			r = new HistoryCache(baseList, nextInput);
			cache.add(r);
		}
		r.count++;
		return r;
	}
}
