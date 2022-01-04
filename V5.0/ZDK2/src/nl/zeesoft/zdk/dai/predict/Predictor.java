package nl.zeesoft.zdk.dai.predict;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.dai.ObjMap;
import nl.zeesoft.zdk.dai.ObjMapComparator;
import nl.zeesoft.zdk.dai.ObjMapList;
import nl.zeesoft.zdk.dai.cache.CacheBuilder;

public class Predictor {
	protected ObjMapComparator		comparator			= null;
	protected ObjMapList 			history				= null;
	protected List<Integer>			cacheIndexes		= new ArrayList<Integer>();
	protected CacheBuilder			cacheBuilder		= null;
	protected List<PredictorCache>	caches				= new ArrayList<PredictorCache>();
	
	protected long					processed			= 0;
	
	protected int					rebuildCache		= 250;
	protected boolean				updateCache			= true;
	
	protected List<Thread>			activeRebuilders	= new ArrayList<Thread>();
	protected boolean				triggerRebuild		= false;
	
	public synchronized void configure(PredictorConfig config) {
		comparator = config.comparator;
		history = new ObjMapList(config.maxHistorySize);
		cacheIndexes.clear();
		cacheIndexes.addAll(config.cacheIndexes);
		cacheBuilder = config.cacheBuilder;
		caches.clear();
		for (CacheConfig cc: config.cacheConfigs) {
			PredictorCache pc = new PredictorCache();
			pc.mergeSimilarity = cc.mergeSimilarity;
			caches.add(pc);
		}
		rebuildCache = config.rebuildCache;
	}
	
	@Override
	public synchronized String toString() {
		StringBuilder str = new StringBuilder();
		if (history!=null) {
			str.append("History max size: " + history.maxSize + ", rebuild: " + rebuildCache + ", processed: " + processed);
			str.append("\nCaches;");
			for (PredictorCache pc: caches) {
				str.append("\n- " + pc.mergeSimilarity + " / " + pc.getCacheSize());
			}
		} else {
			str.append(super.toString());
		}
		return str.toString();
	}
	
	public synchronized void add(ObjMap map) {
		if (history!=null) {
			history.add(map);
			caches.get(0).hitCache(history);
			processed++;
			if (processed % rebuildCache == 0) {
				rebuildCache();
			}
		}
	}
	
	public synchronized void processRequest(PredictorRequest request) {
		request.process(caches, history.getSubList(0, cacheIndexes), comparator);
	}

	public synchronized boolean isRebuildingCache() {
		return activeRebuilders.size() > 0;
	}
	
	protected void rebuildCache() {
		if (activeRebuilders.size() > 0) {
			triggerRebuild = true;
		} else {
			Thread rebuilder = new Thread(new CacheRebuilder(this, cacheBuilder, comparator));
			activeRebuilders.add(rebuilder);
			rebuilder.start();
		}
	}
	
	protected synchronized void rebuilderIsDone(Thread rebuilder) {
		activeRebuilders.remove(rebuilder);
		if (triggerRebuild && activeRebuilders.size()==0) {
			triggerRebuild = false;
			rebuildCache();
		}
	}
	
	protected synchronized PredictorCache getCache(int index) {
		PredictorCache r = null;
		if (index < caches.size()) {
			r = caches.get(index);
		}
		return r;
	}
}
