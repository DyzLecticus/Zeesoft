package nl.zeesoft.zdk.dai.supercache;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Console;
import nl.zeesoft.zdk.dai.ObjMap;
import nl.zeesoft.zdk.dai.ObjMapList;

public class OMCache {
	public OMCacheConfig			config			= null;
	public List<OMCacheElement>		elements		= new ArrayList<OMCacheElement>();
	
	public OMCache(OMCacheConfig config) {
		this.config = config;
	}
	
	public OMCacheElement hit(ObjMapList key, ObjMap value) {
		OMCacheElement r = null;
		if (config.mergeSimilarity<1F) {
			r = get(key, value, config.mergeSimilarity);
		} else {
			r = get(key, value);
		}
		if (r==null) {
			r = new OMCacheElement(key, value);
			if (config.mergeSimilarity<1F && config.subConfig!=null) {
				r.subCache = new OMCache(config.subConfig);
			}
		} else {
			elements.remove(r);
		}
		elements.add(0, r);
		r.count++;
		applyMaxSize();
		if (r.subCache!=null) {
			r = r.subCache.hit(key, value);
		}
		return r;
	}
	
	public OMCacheResult lookup(ObjMapList key) {
		return lookup(key, 0F);
	}
	
	public OMCacheResult lookup(ObjMapList key, float minSimilarity) {
		OMCacheResult r = new OMCacheResult();
		for (OMCacheElement ce: elements) {
			float sim = config.comparator.calculateSimilarity(key, ce.key);
			if (sim>=minSimilarity) {
				if (sim > r.similarity) {
					if (r.elements.size()>0) {
						r.secondary = new OMCacheResult(r.elements, r.similarity);
					}
				}
				if (!r.addSimilarElement(ce, sim) && r.secondary!=null) {
					r.secondary.addSimilarElement(ce, sim);
				}
			}
		}
		Console.log(config.mergeSimilarity + " -> " + r.elements.size() + ", sim: " + r.similarity);
		if (config.mergeSimilarity<1F) {
			r.addSubResults(key);
		}
		return r;
	}
	
	public OMCacheElement get(ObjMapList key, ObjMap value, float minSimilarity) {
		OMCacheElement r = null;
		float max = 0F;
		for (OMCacheElement ce: elements) {
			float valSim = config.comparator.calculateSimilarity(value, ce.value);
			if (valSim>=minSimilarity) {
				float keySim = config.comparator.calculateSimilarity(key, ce.key);
				if (keySim>=minSimilarity && (valSim + keySim) > max) {
					max = valSim + keySim;
					r = ce;
				}
			}
		}
		return r;
	}
	
	public OMCacheElement get(ObjMapList key, ObjMap value) {
		OMCacheElement r = null;
		for (OMCacheElement ce: elements) {
			if (ce.value.equals(value) && ce.key.equals(key)) {
				r = ce;
				break;
			}
		}
		return r;
	}
	
	public void applyMaxSize() {
		while(elements.size()>config.maxSize) {
			elements.remove(elements.size() - 1);
		}
	}
}
