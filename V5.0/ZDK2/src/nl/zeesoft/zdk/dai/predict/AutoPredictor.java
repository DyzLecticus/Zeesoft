package nl.zeesoft.zdk.dai.predict;

import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.dai.ObjMap;

public class AutoPredictor extends Predictor {
	protected MsLogger				addMsLogger			= new MsLogger();
	protected PredictorRequest		request				= new PredictorRequest();
	protected PredictionLog			predictionLog		= new PredictionLog();
	
	protected boolean				waitForCacheRebuild	= true;
	
	protected boolean				predict				= true;
	
	@Override
	public synchronized void configure(PredictorConfig config) {
		super.configure(config);
		addMsLogger.setMaxSize(config.maxMsLoggerSize);
		if (config instanceof AutoPredictorConfig) {
			AutoPredictorConfig cfg = (AutoPredictorConfig) config;
			predictionLog.setComparator(comparator);
			predictionLog.setMaxSize(cfg.maxLogSize);
		}
	}

	public synchronized void setPredict(boolean predict) {
		this.predict = predict;
	}

	public synchronized boolean isPredict() {
		return predict;
	}

	public synchronized PredictorRequest getRequest() {
		return request;
	}
	
	@Override
	public synchronized void add(ObjMap map) {
		long start = System.nanoTime();
		super.add(map);
		if (isPredict()) {
			processRequest(request);
			while(request.isProcessing()) {
				Util.sleepNs(100000);
			}
			predictionLog.add(history.list.get(0), request.getPrediction());
		}
		addMsLogger.add((float)(System.nanoTime() - start) / 1000000F);
	}
	
	public synchronized PredictionLog getPredictionLog() {
		return predictionLog.copy();
	}
	
	public synchronized MsLogger getAddMsLogger() {
		return addMsLogger;
	}
}
