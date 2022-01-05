package nl.zeesoft.zdk.dai.predict;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.dai.ObjMapComparator;
import nl.zeesoft.zdk.dai.cache.Cache;
import nl.zeesoft.zdk.dai.cache.CacheBuilder;

public class CacheRebuilder implements Runnable {
	private Predictor				predictor		= null;
	private List<PredictorCache>	caches			= new ArrayList<PredictorCache>();
	private CacheBuilder			cacheBuilder	= null;
	private ObjMapComparator		comparator		= null;
	
	public CacheRebuilder(Predictor predictor) {
		this.predictor = predictor;
		this.caches.addAll(predictor.caches);
		this.cacheBuilder = predictor.cacheBuilder;
		this.comparator = predictor.comparator;
	}
	
	@Override
	public void run() {
		rebuildCache(0, 0);
	}
	
	protected PredictorCache getCache(int index) {
		PredictorCache r = null;
		if (index < caches.size()) {
			r = caches.get(index);
		}
		return r;
	}
	
	protected void rebuildCache(int fromIndex, int toIndex) {
		if (rebuildCache(getCache(fromIndex),getCache(toIndex))) {
			toIndex++;
			if (toIndex>1) {
				fromIndex++;
			}
			rebuildCache(fromIndex, toIndex);
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
