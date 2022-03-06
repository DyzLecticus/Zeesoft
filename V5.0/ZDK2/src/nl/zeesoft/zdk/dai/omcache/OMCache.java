package nl.zeesoft.zdk.dai.omcache;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.dai.ObjMap;
import nl.zeesoft.zdk.dai.ObjMapList;

public class OMCache {
	public OMCacheConfig			config			= null;
	public List<OMCacheElement>		elements		= new ArrayList<OMCacheElement>();
	
	public OMCache(OMCacheConfig config) {
		this.config = config;
	}

	@Override
	public String toString() {
		return toStringBuilder(1).toString();
	}

	public StringBuilder toStringBuilder(int level) {
		StringBuilder str = new StringBuilder();
		str.append(config.mergeSimilarity + ", elements: " + elements.size());
		if (config.mergeSimilarity<1.0F) {
			for (OMCacheElement element: elements) {
				str.append("\n");
				for (int i = 0; i < level; i++) {
					str.append("  ");
				}
				str.append(element.subCache.toStringBuilder(level + 1));
			}
		}
		return str;
	}

	public SortedMap<Float,Integer> size() {
		SortedMap<Float,Integer> r = new TreeMap<Float,Integer>();
		size(r);
		return r;
	}

	public void size(SortedMap<Float,Integer> r) {
		Integer s = r.get(config.mergeSimilarity);
		if (s==null) {
			s = new Integer(0);
		}
		r.put(config.mergeSimilarity, (s + elements.size()));
		if (config.mergeSimilarity<1.0F) {
			for (OMCacheElement element: elements) {
				element.subCache.size(r);
			}
		}
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
		return lookup(key, 0F, -1);
	}
	
	public OMCacheResult lookup(ObjMapList key, float minSimilarity, int maxDepth) {
		return lookup(key, 0F, 1, maxDepth);
	}
	
	protected OMCacheResult lookup(ObjMapList key, float minSimilarity, int level, int maxDepth) {
		OMCacheResult r = new OMCacheResult();
		for (OMCacheElement ce: elements) {
			float sim = config.comparator.calculateSimilarity(key, ce.key);
			if (sim>=minSimilarity) {
				if (sim > r.similarity) {
					if (r.elements.size()>0) {
						r.secondary = new OMCacheResult(r.elements, r.similarity);
					}
				}
				if (!r.addSimilarElement(ce, sim)) {
					if (r.secondary==null) {
						r.secondary = new OMCacheResult();
					}
					r.secondary.addSimilarElement(ce, sim);
				}
			}
		}
		if (config.mergeSimilarity<1F && (maxDepth==0 || level < maxDepth)) {
			r.addSubResults(key, minSimilarity, level, maxDepth);
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
