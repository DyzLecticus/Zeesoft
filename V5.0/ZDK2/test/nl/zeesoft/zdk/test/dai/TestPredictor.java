package nl.zeesoft.zdk.test.dai;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.dai.ObjMapList;
import nl.zeesoft.zdk.dai.cache.CacheResult;
import nl.zeesoft.zdk.dai.predict.Predictor;
import nl.zeesoft.zdk.dai.predict.PredictorConfig;
import nl.zeesoft.zdk.dai.predict.PredictorRequest;

public class TestPredictor {
	private static TestPredictor	self	= new TestPredictor();
	
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);

		PredictorConfig config = new PredictorConfig();
		assert config.cacheConfigs.size() == 3;
		assert config.cacheConfigs.get(0).mergeSimilarity == 1F;
		assert config.cacheConfigs.get(1).mergeSimilarity == 0.9F;
		assert config.cacheConfigs.get(2).mergeSimilarity == 0.8F;
		
		config.maxHistorySize = 500;
		config.rebuildCache = 200;
		config.cacheConfigs.get(2).mergeSimilarity = 0.85F;
		
		Predictor predictor = new Predictor();
		predictor.configure(config);
		Logger.debug(self, "Predictor;\n" + predictor);
		assert predictor.toString().equals("History max size: 500, rebuild: 200, processed: 0\nCaches;\n- 1.0 / 0\n- 0.9 / 0\n- 0.85 / 0");
		
		ObjMapList history = new ObjMapList(5000);
		int num = TestCachePerformance.readInputFile(history);
		Logger.debug(self, "Adding " + (num - 1000) + " records ...");		
		for (int i = history.list.size() - 1; i > 1000; i--) {
			predictor.add(history.list.get(i));
		}
		while(predictor.isRebuildingCache()) {
			Util.sleep(100);
		}
		Logger.debug(self, "Predictor;\n" + predictor);
		
		
		
		PredictorRequest request = new PredictorRequest();
		Logger.debug(self, "Processing request ...");
		predictor.processRequest(request);
		while(request.isProcessing()) {
			Util.sleep(1);
		}
		Logger.debug(self, "Request results;" + request.getResults().size());
		assert request.getResults().size() == 3;
		for (CacheResult result: request.getResults()) {
			Logger.debug(self, "Request result;\n" + result);
		}
		
		
		
		for (int i = 1000; i > 900; i--) {
			predictor.add(history.list.get(i));
		}
		while(predictor.isRebuildingCache()) {
			Util.sleep(100);
		}
		
		request = new PredictorRequest();
		Logger.debug(self, "Processing request ...");
		predictor.processRequest(request);
		while(request.isProcessing()) {
			Util.sleep(1);
		}
		Logger.debug(self, "Request results;" + request.getResults().size());
		assert request.getResults().size() == 3;
		for (CacheResult result: request.getResults()) {
			Logger.debug(self, "Request result;\n" + result);
		}
		
	}
}
