package nl.zeesoft.zdk.dai.predict;

import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.dai.ObjMap;

public class AutoPredictor extends Predictor {
	protected PredictorRequest		request				= new PredictorRequest();
	protected PredictionLog			predictionLog		= new PredictionLog();
	
	protected boolean				waitForCacheRebuild	= true;
	
	protected boolean				predict				= true;
	
	@Override
	public synchronized void configure(PredictorConfig config) {
		super.configure(config);
		if (config instanceof AutoPredictorConfig) {
			AutoPredictorConfig cfg = (AutoPredictorConfig) config;
			predictionLog.setComparator(comparator);
			predictionLog.setMaxSize(cfg.maxLogSize);
			waitForCacheRebuild = cfg.waitForCacheRebuild;
		}
	}

	public synchronized void setPredict(boolean predict) {
		this.predict = predict;
	}

	public synchronized boolean isPredict() {
		return predict;
	}

	public synchronized boolean isPredicting() {
		return request.isProcessing();
	}
	
	@Override
	public void add(ObjMap map) {
		super.add(map);
		if (isPredict()) {
			if (waitForCacheRebuild && isRebuildingCache()) {
				while(isRebuildingCache()) {
					Util.sleepNs(100000);
				}
			}
			PredictorRequest req = getRequest();
			processRequest(req);
			while(req.isProcessing()) {
				Util.sleepNs(100000);
			}
			predictionLog.add(getHistory(0), req.getPrediction());
		}
	}
		
	public synchronized PredictionLog getPredictionLog() {
		return predictionLog.copy();
	}
	
	protected synchronized PredictorRequest getRequest() {
		return request;
	}
	
	protected synchronized ObjMap getHistory(int index) {
		return history.list.get(0);
	}
}
