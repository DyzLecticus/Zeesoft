package nl.zeesoft.zdk.dai.predict;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.dai.MapPrediction;
import nl.zeesoft.zdk.dai.ObjMap;
import nl.zeesoft.zdk.dai.ObjMapComparator;
import nl.zeesoft.zdk.dai.ObjMapList;
import nl.zeesoft.zdk.dai.ObjMapTransformer;
import nl.zeesoft.zdk.dai.Prediction;

public class PredictorRequest {
	private float							minSimilarity		= 0F;
	private int								minCacheIndex		= 0;
	private int								maxCacheIndex		= Integer.MAX_VALUE;

	private List<PredictorRequestWorker>	workers				= new ArrayList<PredictorRequestWorker>();
	
	private List<PredictorCacheResult>		predictorResults	= new ArrayList<PredictorCacheResult>();
	
	private ObjMapTransformer				transformer			= null;
	private ObjMap							from				= null;
	
	protected synchronized void process(List<PredictorCache> caches, ObjMapList baseList, ObjMapComparator comparator) {
		predictorResults.clear();
		int max = maxCacheIndex;
		if (max > caches.size() - 1) {
			max = caches.size() - 1;
		}
		int min = minCacheIndex;
		for (int i = max; i >= min; i--) {
			PredictorRequestWorker worker = new PredictorRequestWorker(this, caches.get(i), baseList, comparator, minSimilarity);
			workers.add(worker);
			worker.start();
		}
	}
	
	protected synchronized void workerIsDone(PredictorRequestWorker worker, PredictorCacheResult result) {
		workers.remove(worker);
		predictorResults.add(result);
		if (workers.size()==0) {
			processedRequest();
		}
	}
	
	protected void processedRequest() {
		// Override to extend
	}
	
	protected synchronized void setTransform(ObjMapTransformer transformer, ObjMap from) {
		this.transformer = transformer;
		this.from = from;
	}
	
	public synchronized boolean isProcessing() {
		return workers.size()>0;
	}
	
	public synchronized List<PredictorCacheResult> getResults() {
		return new ArrayList<PredictorCacheResult>(predictorResults);
	}

	public synchronized List<Prediction> getPredictions() {
		List<Prediction> r = new ArrayList<Prediction>();
		for (PredictorCacheResult result: predictorResults) {
			Prediction p = result.cacheResult.getPrediction();
			for (MapPrediction mp: p.mapPredictions) {
				mp.support = mp.support * result.mergeSimilarity;
			}
			p.calculatePredictedMap();
			r.add(p);
		}
		return r;
	}

	public Prediction getPrediction() {
		Prediction r = Prediction.mergePredictions(getPredictions());
		if (transformer!=null && from!=null) {
			transformer.applyTransformation(from, r.keyPredictions.predictedMap, r.keyPredictions.predictedMap);
			transformer.applyTransformation(from, r.keyPredictions.weightedMap, r.keyPredictions.weightedMap);
		}
		return r;
	}
	
	public synchronized float getMinSimilarity() {
		return minSimilarity;
	}

	public synchronized void setMinSimilarity(float minSimilarity) {
		this.minSimilarity = minSimilarity;
	}

	public synchronized int getMinCacheIndex() {
		return minCacheIndex;
	}

	public synchronized void setMinCacheIndex(int minCacheIndex) {
		this.minCacheIndex = minCacheIndex;
	}

	public synchronized int getMaxCacheIndex() {
		return maxCacheIndex;
	}

	public synchronized void setMaxCacheIndex(int maxCacheIndex) {
		this.maxCacheIndex = maxCacheIndex;
	}
}
