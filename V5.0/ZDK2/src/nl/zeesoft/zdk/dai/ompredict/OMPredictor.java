package nl.zeesoft.zdk.dai.ompredict;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;

import nl.zeesoft.zdk.dai.ObjMap;
import nl.zeesoft.zdk.dai.ObjMapComparator;
import nl.zeesoft.zdk.dai.ObjMapList;
import nl.zeesoft.zdk.dai.omcache.OMCache;
import nl.zeesoft.zdk.dai.omcache.OMCacheResult;
import nl.zeesoft.zdk.dai.omcache.OMCacheResultSummary;
import nl.zeesoft.zdk.dai.predict.MsLogger;

public class OMPredictor {
	protected ObjMapComparator		comparator			= null;
	protected ObjMapList 			history				= null;
	protected List<Integer>			cacheIndexes		= new ArrayList<Integer>();
	protected OMCache				cache				= null;
	
	public MsLogger					hitMsLogger			= null;
	public MsLogger					lookupMsLogger		= null;
	
	protected int					processed			= 0;
	
	public synchronized void configure(OMPredictorConfig config) {
		comparator = config.comparator;
		history = new ObjMapList(config.maxHistorySize);
		cacheIndexes.clear();
		cacheIndexes.addAll(config.cacheIndexes);
		cache = new OMCache(config.cacheConfig);
		hitMsLogger = new MsLogger();
		hitMsLogger.setMaxSize(config.maxMsLoggerSize);
		lookupMsLogger = new MsLogger();
		lookupMsLogger.setMaxSize(config.maxMsLoggerSize);
	}
	
	@Override
	public synchronized String toString() {
		StringBuilder str = new StringBuilder();
		if (history!=null) {
			str.append("History max size: " + history.maxSize + ", processed: " + processed);
			str.append("\nCaches;");
			SortedMap<Float,Integer> size = cache.size();
			for (Entry<Float,Integer> entry: size.entrySet()) {
				str.append("\n- " + entry.getKey() + ", size: " + entry.getValue());
			}
		} else {
			str.append(super.toString());
		}
		return str.toString();
	}
	
	public synchronized void add(ObjMap map) {
		if (history!=null) {
			history.add(map);
			ObjMapList baseList = history.getSubList(1,cacheIndexes);
			if (baseList.list.size()>0) {
				ObjMap nextMap = history.list.get(0);
				long start = System.nanoTime();
				cache.hit(baseList, nextMap);
				hitMsLogger.add((float)(System.nanoTime() - start) / 1000000F);
			}
			processed++;
		}
	}
	
	public synchronized void processRequest(OMPredictorRequest request) {
		if (history!=null) {
			ObjMapList baseList = history.getSubList(0,cacheIndexes);
			if (baseList.list.size()>0) {
				long start = System.nanoTime();
				OMCacheResult result = cache.lookup(baseList, request.getMinSimilarity(), request.getMaxDepth());
				lookupMsLogger.add((float)(System.nanoTime() - start) / 1000000F);
				OMCacheResultSummary summary = new OMCacheResultSummary(result);
				request.setPrediction(summary.getPrediction());
			}
		}
	}
	
	public synchronized int getProcessed() {
		return processed;
	}
	
	public synchronized MsLogger getHitMsLogger() {
		return hitMsLogger;
	}
	
	public synchronized MsLogger getLookupMsLogger() {
		return lookupMsLogger;
	}
}
