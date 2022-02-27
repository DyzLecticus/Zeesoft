package nl.zeesoft.zdk.dai.ompredict;

import nl.zeesoft.zdk.dai.ObjMap;
import nl.zeesoft.zdk.dai.ObjMapTransformer;
import nl.zeesoft.zdk.dai.Prediction;

public class OMPredictorRequest {
	private float				minSimilarity	= 0F;
	private int					maxDepth		= 0;
	
	private ObjMapTransformer	transformer		= null;
	private ObjMap				from			= null;

	private Prediction			prediction		= null;

	public synchronized float getMinSimilarity() {
		return minSimilarity;
	}
	
	public synchronized void setMinSimilarity(float minSimilarity) {
		this.minSimilarity = minSimilarity;
	}

	public synchronized int getMaxDepth() {
		return maxDepth;
	}
	
	public synchronized void setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}

	public synchronized Prediction getPrediction() {
		return prediction;
	}

	protected synchronized void setPrediction(Prediction prediction) {
		if (transformer!=null && from!=null) {
			transformer.applyTransformation(from, prediction.keyPredictions.predictedMap, prediction.keyPredictions.predictedMap);
			transformer.applyTransformation(from, prediction.keyPredictions.weightedMap, prediction.keyPredictions.weightedMap);
		}
		this.prediction = prediction;
	}
	
	protected synchronized void setTransform(ObjMapTransformer transformer, ObjMap from) {
		this.transformer = transformer;
		this.from = from;
	}
}
