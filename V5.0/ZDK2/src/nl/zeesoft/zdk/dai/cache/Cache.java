package nl.zeesoft.zdk.dai.cache;

import java.util.List;

import nl.zeesoft.zdk.dai.ObjMap;
import nl.zeesoft.zdk.dai.ObjMapComparator;
import nl.zeesoft.zdk.dai.ObjMapList;

public class Cache {
	public List<Integer>	indexes		= CacheIndexesGenerator.generate();
	public CacheElements	elements	= null;
	
	public Cache() {
		elements = new CacheElements();
	}
	
	public Cache(int maxSize) {
		elements = new CacheElements(maxSize);
	}
	
	public Cache(int maxSize, float linkedMinSimilarity, int linkedMaxSize) {
		elements = new CacheElementsLinked(maxSize, linkedMinSimilarity, linkedMaxSize);
	}
	
	public CacheResult getCacheResult(ObjMapList baseList, ObjMapComparator comparator, float minSimilarity) {
		return elements.getCacheResult(baseList, comparator, minSimilarity);
	}

	public CacheElement hitCache(ObjMapList history) {
		return hitCache(history, 1F, null);
	}

	public CacheElement hitCache(ObjMapList history, float minSimilarity, ObjMapComparator comparator) {
		CacheElement r = null;
		if (history.list.size()>1 && indexes.size()>0) {
			r = elements.hitCache(history.getSubList(1,indexes),history.list.get(0), minSimilarity, comparator);
		}
		return r;
	}

	public CacheElement hitCache(ObjMapList baseList, ObjMap nextMap, float minSimilarity, ObjMapComparator comparator) {
		return elements.hitCache(baseList, nextMap, minSimilarity, comparator);
	}
}
