package nl.zeesoft.zdk.dai.predict;

import nl.zeesoft.zdk.dai.ObjMapComparator;
import nl.zeesoft.zdk.dai.cache.Cache;
import nl.zeesoft.zdk.dai.cache.CacheBuilder;

public class CacheRebuilder implements Runnable {
	private Predictor			predictor		= null;
	private CacheBuilder		cacheBuilder	= null;
	private ObjMapComparator	comparator		= null;
	
	public CacheRebuilder(Predictor predictor, CacheBuilder cacheBuilder, ObjMapComparator comparator) {
		this.predictor = predictor;
		this.cacheBuilder = cacheBuilder;
		this.comparator = comparator;
	}
	
	@Override
	public void run() {
		rebuildCache(Thread.currentThread(), 0, 0);
	}
	
	protected void rebuildCache(Thread rebuilder, int fromIndex, int toIndex) {
		if (rebuildCache(predictor.getCache(fromIndex),predictor.getCache(toIndex))) {
			toIndex++;
			if (toIndex>1) {
				fromIndex++;
			}
			rebuildCache(rebuilder, fromIndex, toIndex);
		} else {
			predictor.rebuilderIsDone(Thread.currentThread());
		}
	}
	
	protected boolean rebuildCache(PredictorCache from, PredictorCache to) {
		boolean r = false;
		if (from!=null && to!=null) {
			if (from!=to || to.mergeSimilarity < 1F) {
				Cache cache = from.getCacheCopy();
				cache = cacheBuilder.buildSuperCache(cache, comparator, to.mergeSimilarity);
				to.setCache(cache);
			}
			r = true;
		}
		return r;
	}
}
