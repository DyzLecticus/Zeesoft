package nl.zeesoft.zdk.test.dai;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.dai.ObjMap;
import nl.zeesoft.zdk.dai.ObjMapList;
import nl.zeesoft.zdk.dai.Prediction;
import nl.zeesoft.zdk.dai.predict.AutoPredictor;
import nl.zeesoft.zdk.dai.predict.AutoPredictorConfig;
import nl.zeesoft.zdk.dai.predict.PredictionLog;

public class TestAutoPredictor {
	private static TestAutoPredictor	self	= new TestAutoPredictor();
	
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);

		AutoPredictorConfig config = new AutoPredictorConfig();
		assert config.cacheConfigs.size() == 3;
		assert config.cacheConfigs.get(0).mergeSimilarity == 1F;
		assert config.cacheConfigs.get(1).mergeSimilarity == 0.9F;
		assert config.cacheConfigs.get(2).mergeSimilarity == 0.8F;
		
		config.maxHistorySize = 500;
		config.rebuildCache = 200;
		config.cacheConfigs.get(2).mergeSimilarity = 0.85F;
		config.waitForCacheRebuild = false;
		
		AutoPredictor predictor = new AutoPredictor();
		predictor.configure(config);
		Logger.debug(self, "Predictor;\n" + predictor);
		assert predictor.toString().equals("History max size: 500, rebuild: 200, processed: 0\nCaches;\n- 1.0 / 0\n- 0.9 / 0\n- 0.85 / 0");
		
		predictor.setPredict(false);
		
		ObjMapList history = new ObjMapList(5000);
		int num = TestCachePerformance.readInputFile(history);
		Logger.debug(self, "Adding " + num + " records ...");		
		for (int i = history.list.size() - 1; i >= 0; i--) {
			if (i==1000) {
				Logger.debug(self, "Predicting ...");
				predictor.setPredict(true);
			}
			predictor.add(history.list.get(i));
		}
		while(predictor.isRebuildingCache()) {
			Util.sleep(100);
		}
		Logger.debug(self, "Predictor;\n" + predictor);
		
		PredictionLog log = predictor.getPredictionLog();
		Logger.debug(self, "Accuracy: " + log.getKeyAccuracy("3", false) + ", deviation: " + log.getKeyAccuracyStdDev("3", false));
		Logger.debug(self, "Weighted accuracy: " + log.getKeyAccuracy("3", true) + ", deviation: " + log.getKeyAccuracyStdDev("3", true));
		Logger.debug(self, "Confidence: " + log.getKeyConfidence("3") + ", deviation: " + log.getKeyConfidenceStdDev("3"));

		Prediction prediction = log.getPredictions().get(1);
		Logger.debug(self, "Request prediction;\n" + prediction);
		ObjMap actual = log.getHistory().get(0);
		Logger.debug(self, "Predicted: " + prediction.getPredictedMap() + ", weighted: " + prediction.getWeightedMap() + ", actual: " + actual);
	}
}
