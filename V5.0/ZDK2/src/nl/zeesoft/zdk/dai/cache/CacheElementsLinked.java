package nl.zeesoft.zdk.dai.cache;

import nl.zeesoft.zdk.dai.ObjMap;
import nl.zeesoft.zdk.dai.ObjMapComparator;
import nl.zeesoft.zdk.dai.ObjMapList;

public class CacheElementsLinked extends CacheElements {
	public float	linkedMinSimilarity	= 1F;
	public int		linkedMaxSize		= 1000;
	
	public CacheElementsLinked() {
		
	}
	
	public CacheElementsLinked(int maxSize, float linkedMinSimilarity, int linkedMaxSize) {
		super(maxSize);
		this.linkedMinSimilarity = linkedMinSimilarity;
		this.linkedMaxSize = linkedMaxSize;
	}
	
	@Override
	public CacheResult getCacheResult(ObjMapList baseList, ObjMapComparator comparator, float minSimilarity) {
		CacheResult r = super.getCacheResult(baseList, comparator, minSimilarity);
		if (r!=null && r.results.size()>0) {
			r = r.results.get(0).elements.getCacheResult(baseList, comparator, minSimilarity);
		}
		return r;
	}
	
	@Override
	public CacheElement hitCache(ObjMapList baseList, ObjMap nextMap, float minSimilarity, ObjMapComparator comparator) {
		CacheElement r = super.hitCache(baseList, nextMap, minSimilarity, comparator);
		if (r!=null) {
			if (r.elements==null) {
				r.elements = new CacheElements(linkedMaxSize);
			}
			r = r.elements.hitCache(baseList, nextMap, linkedMinSimilarity, comparator);
		}
		return r;
	}
}
