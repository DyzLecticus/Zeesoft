package nl.zeesoft.zdk.test.dai;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.dai.KeyPredictions;
import nl.zeesoft.zdk.dai.ObjMap;
import nl.zeesoft.zdk.dai.ObjMapList;
import nl.zeesoft.zdk.dai.Prediction;
import nl.zeesoft.zdk.dai.predict.AutoPredictor;
import nl.zeesoft.zdk.dai.predict.AutoPredictorConfig;
import nl.zeesoft.zdk.dai.predict.MsLogger;
import nl.zeesoft.zdk.dai.predict.PredictionLog;
import nl.zeesoft.zdk.dai.predict.PredictorCacheResult;
import nl.zeesoft.zdk.dai.predict.PredictorRequest;
import nl.zeesoft.zdk.json.Json;
import nl.zeesoft.zdk.json.JsonConstructor;
import nl.zeesoft.zdk.json.ObjectConstructor;

public class TestAutoPredictor {
	private static TestAutoPredictor	self	= new TestAutoPredictor();
	
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);

		PredictorCacheResult pcr = new PredictorCacheResult();
		assert pcr != null;
		pcr.timeNs = 1000000;
		assert pcr.toString().equals("Merge similarity: 1.0\nnull\nTime: 1.0 ms");
		
		PredictorRequest request = new PredictorRequest();
		request.setMinSimilarity(0.1F);
		assert request.getMinSimilarity() == 0.1F;
		request.setMaxCacheIndex(1);
		assert request.getMaxCacheIndex() == 1;
		request.setMinCacheIndex(1);
		assert request.getMinCacheIndex() == 1;
		
		AutoPredictorConfig config = new AutoPredictorConfig();
		assert config.cacheConfigs.size() == 3;
		assert config.cacheConfigs.get(0).mergeSimilarity == 1F;
		assert config.cacheConfigs.get(1).mergeSimilarity == 0.9F;
		assert config.cacheConfigs.get(2).mergeSimilarity == 0.8F;
		
		config.maxHistorySize = 500;
		config.cacheConfigs.get(0).maxSize = 2000;
		
		config.cacheConfigs.get(1).mergeSimilarity = 0.95F;
		config.cacheConfigs.get(1).maxSize = 1000;
		config.cacheConfigs.get(1).linkedMergeSimilarity = 1F;
		
		config.cacheConfigs.get(2).mergeSimilarity = 0.9F;
		config.cacheConfigs.get(2).maxSize = 600;
		config.cacheConfigs.get(2).linkedMergeSimilarity = 0.95F;
		config.cacheConfigs.get(2).linkedMaxSize = 300;
		
		//config.transformer = null;
		
		AutoPredictor predictor = new AutoPredictor();
		assert predictor.toString().startsWith("nl.zeesoft");
		predictor.configure(config);
		Logger.debug(self, "Predictor;\n" + predictor);
		assert predictor.toString().startsWith("History max size: 500, processed: 0");
		assert predictor.toString().endsWith("- 0.9 / 0.95 (0 / 180000 [600 * 300])");
		
		predictor.setPredict(false);
		
		ObjMapList history = new ObjMapList(5000);
		int num = TestCachePerformance.readInputFile(history);
		Logger.debug(self, "Adding " + num + " records ...");		
		for (int i = history.list.size() - 1; i >= 0; i--) {
			if (i==1000) {
				Logger.debug(self, "Predictor;\n" + predictor);
				Logger.debug(self, "Predicting ...");
				assert predictor.getProcessed() == 3389L;
				predictor.setPredict(true);
			}
			predictor.add(history.list.get(i));
		}
		Logger.debug(self, "Predictor;\n" + predictor);
		
		assert predictor.getRequest().getResults().size() == 3;
		
		PredictionLog log = predictor.getPredictionLog();
		assert log.toString().length() > 100;
		
		Json json = JsonConstructor.fromObjectUseConvertors(log.getPredictions().get(0).keyPredictions);
		KeyPredictions kpc = (KeyPredictions)ObjectConstructor.fromJson(json);
		Json json2 = JsonConstructor.fromObjectUseConvertors(kpc);
		assert json2.toStringBuilderReadFormat().toString().equals(json.toStringBuilderReadFormat().toString());
		
		json = JsonConstructor.fromObjectUseConvertors(log);
		PredictionLog plc = (PredictionLog) ObjectConstructor.fromJson(json);
		json2 = JsonConstructor.fromObjectUseConvertors(plc);
		assert json2.toStringBuilderReadFormat().toString().equals(json.toStringBuilderReadFormat().toString());
		
		Prediction prediction = log.getPredictions().get(1);
		Logger.debug(self, "Request prediction;\n" + prediction);
		ObjMap actual = log.getHistory().get(0);
		Logger.debug(self, "Predicted: " + prediction.getPredictedMap() + ", weighted: " + prediction.getWeightedMap() + ", actual: " + actual);
		
		Logger.debug(self, "Accuracy: " + log.getKeyAccuracy("3", false) + ", deviation: " + log.getKeyAccuracyStdDev("3", false) + ", trend: " + log.getKeyAccuracyTrend("3", false));
		Logger.debug(self, "Weight: " + log.getKeyWeight("3") + ", deviation: " + log.getKeyWeightStdDev("3"));
		Logger.debug(self, "Weighted accuracy: " + log.getKeyAccuracy("3", true) + ", deviation: " + log.getKeyAccuracyStdDev("3", true) + ", trend: " + log.getKeyAccuracyTrend("3", true));

		assert log.getKeyAccuracy("3", false) > 0.9F;
		assert log.getKeyAccuracy("3", false) != log.getKeyAccuracyTrend("3", false);
		assert log.getKeyAccuracy("3", true) > 0.9F;
		assert log.getKeyAccuracy("3", true) != log.getKeyAccuracyTrend("3", true);
		
		Logger.debug(self, "Add ms: " + predictor.getAddMsLogger());
		for (int i = 0; i < config.cacheConfigs.size(); i++) {
			Logger.debug(self, "Cache " + i + " hit ms: " + predictor.getHitMsLogger(i));
			Logger.debug(self, "Cache " + i + " request ms; " + predictor.getRequestMsLogger(i));
		}

		json = JsonConstructor.fromObjectUseConvertors(predictor.getAddMsLogger());
		MsLogger ml = (MsLogger)ObjectConstructor.fromJson(json);
		json2 = JsonConstructor.fromObjectUseConvertors(ml);
		assert json2.toStringBuilderReadFormat().toString().equals(json.toStringBuilderReadFormat().toString());

		/*
		Logger.debug(self, "Converting to JSON ...");
		json = JsonConstructor.fromObjectUseConvertors(predictor);
	    try {
			FileWriter writer = new FileWriter("dist/predictor.json");
			Logger.debug(self, "Writing to file ...");
			writer.write(json.toStringBuilderReadFormat().toString());
			writer.close();
	    } catch (IOException e) {
			e.printStackTrace();
		}
		*/
	}
}
