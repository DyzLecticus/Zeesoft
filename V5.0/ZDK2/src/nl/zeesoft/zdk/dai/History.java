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
		return cache.getCacheResult(getSubList(0, cache.indexes), comparator, minSimilarity);
	}
	
	public CacheElement hitCache() {
		CacheElement r = null;
		if (updateCache && list.size()>1 && cache.indexes.size()>0) {
			r = cache.hitCache(getSubList(1,cache.indexes),list.get(0));
		}
		return r;
	}
}
