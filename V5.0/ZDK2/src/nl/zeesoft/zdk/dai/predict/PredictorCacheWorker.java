package nl.zeesoft.zdk.dai.predict;

import nl.zeesoft.zdk.dai.ObjMap;
import nl.zeesoft.zdk.dai.ObjMapComparator;
import nl.zeesoft.zdk.dai.ObjMapList;

public class PredictorCacheWorker implements Runnable {
	protected PredictorCache	predictorCache		= null;
	protected ObjMapList		baseList			= null;
	protected ObjMap			nextMap				= null;
	protected ObjMapComparator	comparator			= null;
	
	protected PredictorCacheWorker(PredictorCache predictorCache, ObjMapList baseList, ObjMap nextMap, ObjMapComparator comparator) {
		this.predictorCache = predictorCache;
		this.baseList = baseList;
		this.nextMap = nextMap;
		this.comparator = comparator;
	}

	@Override
	public void run() {
		predictorCache.hitCache(baseList, nextMap, comparator);
	}
	
	protected void start() {
		Thread thread = new Thread(this);
		thread.start();
	}
}
