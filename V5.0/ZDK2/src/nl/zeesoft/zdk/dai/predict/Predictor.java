package nl.zeesoft.zdk.dai.predict;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.dai.ObjMap;
import nl.zeesoft.zdk.dai.ObjMapComparator;
import nl.zeesoft.zdk.dai.ObjMapList;

public class Predictor {
	protected ObjMapComparator		comparator			= null;
	protected ObjMapList 			history				= null;
	protected List<Integer>			cacheIndexes		= new ArrayList<Integer>();
	protected List<PredictorCache>	caches				= new ArrayList<PredictorCache>();
	
	protected long					processed			= 0;
	
	public synchronized void configure(PredictorConfig config) {
		comparator = config.comparator;
		history = new ObjMapList(config.maxHistorySize);
		cacheIndexes.clear();
		cacheIndexes.addAll(config.cacheIndexes);
		caches.clear();
		for (CacheConfig cc: config.cacheConfigs) {
			PredictorCache pc = new PredictorCache();
			pc.mergeSimilarity = cc.mergeSimilarity;
			caches.add(pc);
		}
	}
	
	@Override
	public synchronized String toString() {
		StringBuilder str = new StringBuilder();
		if (history!=null) {
			str.append("History max size: " + history.maxSize + ", processed: " + processed);
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
			for (PredictorCache pc: caches) {
				pc.hitCache(history, comparator);
			}
			processed++;
		}
	}
	
	public synchronized void processRequest(PredictorRequest request) {
		request.process(caches, history.getSubList(0, cacheIndexes), comparator);
	}
}
