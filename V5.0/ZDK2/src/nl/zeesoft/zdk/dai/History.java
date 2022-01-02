package nl.zeesoft.zdk.dai;

import nl.zeesoft.zdk.dai.cache.Cache;
import nl.zeesoft.zdk.dai.cache.CacheElement;
import nl.zeesoft.zdk.dai.cache.CacheResult;

public class History extends ObjMapList {
	public Cache				cache			= new Cache();
	public boolean				updateCache		= true;
	
	public History() {
		
	}
	
	public History(int maxSize) {
		super(maxSize);
	}

	@Override
	protected void addMap(ObjMap map) {
		super.addMap(map);
		if (updateCache) {
			hitCache();
		}
	}
	
	public CacheResult getCacheResult(ObjMapComparator comparator, float minSimilarity) {
		return getCacheResult(comparator, minSimilarity, 0);
	}
	
	public CacheResult getCacheResult(ObjMapComparator comparator, float minSimilarity, int index) {
		if (index >= list.size()) {
			index = list.size() - 1;
		}
		CacheResult r = cache.getCacheResult(getSubList(index, cache.indexes), comparator, minSimilarity);
		if (list.size()==1) {
			CacheElement ce = new CacheElement();
			ce.nextMap = list.get(0);
			r.results.add(ce);
			r.similarity = 1F;
		}
		return r;
	}
	
	public CacheElement hitCache() {
		CacheElement r = null;
		if (updateCache && list.size()>1 && cache.indexes.size()>0) {
			r = cache.hitCache(getSubList(1,cache.indexes),list.get(0));
		}
		return r;
	}
}
