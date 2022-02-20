package nl.zeesoft.zdk.dai.ompredict;

import nl.zeesoft.zdk.dai.ObjMap;
import nl.zeesoft.zdk.dai.ObjMapTransformer;
import nl.zeesoft.zdk.dai.Prediction;

public class OMPredictorRequest {
	private float				minSimilarity	= 0F;
	private Prediction			prediction		= null;

	private ObjMapTransformer	transformer		= null;
	private ObjMap				from			= null;
	
	public synchronized float getMinSimilarity() {
		return minSimilarity;
	}
	
	public synchronized void setMinSimilarity(float minSimilarity) {
		this.minSimilarity = minSimilarity;
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
