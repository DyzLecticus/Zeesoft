package nl.zeesoft.zdk.dai.ompredict;

import nl.zeesoft.zdk.dai.ObjMap;
import nl.zeesoft.zdk.dai.predict.PredictionLog;

public class AutoOMPredictor extends OMTransformationPredictor {
	protected OMPredictorRequest	request				= new OMPredictorRequest();
	protected PredictionLog			predictionLog		= new PredictionLog();
	
	protected boolean				predict				= true;
	
	@Override
	public synchronized void configure(OMPredictorConfig config) {
		super.configure(config);
		predictionLog.setComparator(comparator);
		if (config instanceof AutoOMPredictorConfig) {
			AutoOMPredictorConfig cfg = (AutoOMPredictorConfig) config;
			predictionLog.setMaxSize(cfg.maxLogSize);
		}
	}

	public synchronized void setPredict(boolean predict) {
		this.predict = predict;
	}

	public synchronized boolean isPredict() {
		return predict;
	}

	public synchronized OMPredictorRequest getRequest() {
		return request;
	}
	
	@Override
	public synchronized void add(ObjMap map) {
		super.add(map);
		if (isPredict()) {
			processRequest(request);
			predictionLog.add(absoluteHistory.list.get(0), request.getPrediction());
		}
	}
	
	public synchronized PredictionLog getPredictionLog() {
		return predictionLog.copy();
	}
}
