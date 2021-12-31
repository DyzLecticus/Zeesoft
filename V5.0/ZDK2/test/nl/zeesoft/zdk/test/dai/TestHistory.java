package nl.zeesoft.zdk.test.dai;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.dai.CacheResult;
import nl.zeesoft.zdk.dai.History;
import nl.zeesoft.zdk.dai.HistoryCache;
import nl.zeesoft.zdk.dai.KeyPrediction;
import nl.zeesoft.zdk.dai.MapPrediction;
import nl.zeesoft.zdk.dai.ObjMap;
import nl.zeesoft.zdk.dai.ObjMapComparator;
import nl.zeesoft.zdk.dai.Prediction;

public class TestHistory {
	private static TestHistory	self	= new TestHistory();
	
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);

		assert new History() != null;
		assert new HistoryCache() != null;
		assert new MapPrediction() != null;
		assert new KeyPrediction() != null;
		
		History history = new History(16);
		history.maxSize = 16;
		
		ObjMapComparator comparator = new ObjMapComparator();
		
		history.addAll(getPattern());
		history.addAll(getPattern());
	
		assert history.list.size() == 16;
		assert history.cache.size() == 0;
		
		history.cacheIndexes.add(0);
		history.cacheIndexes.add(1);
		history.cacheIndexes.add(2);
		history.cacheIndexes.add(3);
		history.addAll(getPattern());
		history.addAll(getPattern());
		assert history.list.size() == 16;
		assert history.cache.size() == 13;
		assert history.cache.get(0).toString().length() == 122;
		
		CacheResult result = history.getCacheResult(comparator, 0.5F);
		assert result.similarity == 1.0F;
		assert result.results.size() == 1;
		assert result.results.get(0).nextMap.equals(new ObjMap(2.0F, 1.0F, 0F));
		assert result.results.get(0).count == 2;
		
		history = new History(16);
		history.cacheIndexes.add(0);
		history.cacheIndexes.add(1);
		history.cacheIndexes.add(2);
		history.cacheIndexes.add(3);
		history.addAll(getPattern());
		result = history.getCacheResult(comparator, 0.5F);
		assert result.similarity == 0.9166667F;
		Prediction prediction = result.getPrediction();
		Logger.debug(self, "Prediction;\n" + prediction);
		assert prediction.mapPredictions.size() == 1;
		assert prediction.mapPredictions.get(0).confidence == result.similarity;
		assert prediction.mapPredictions.get(0).toString().equals("{1:2.0, 2:1.0, 3:0.0}, votes: 1, confidence: 0.9166667");
		assert prediction.keyPredictions.size() == 3;
		assert prediction.predictedMap.equals(new ObjMap(2.0F, 1.0F, 0F));
		assert prediction.predictedConfidencesMap.equals(new ObjMap(1F, 1F, 1F));
	}

	public static List<ObjMap> getPattern() {
		List<ObjMap> r = new ArrayList<ObjMap>();
		r.add(new ObjMap(2F, 1F, 0F));
		r.add(new ObjMap(0F, 0.5F, 0F));
		r.add(new ObjMap(0F, 2F, 0F));
		r.add(new ObjMap(0F, 0.5F, 0F));
		r.add(new ObjMap(2F, 1F, 2F));
		r.add(new ObjMap(0F, 0.5F, 0F));
		r.add(new ObjMap(0F, 2F, 0F));
		r.add(new ObjMap(0F, 0.5F, 0F));
		r.add(new ObjMap(2F, 1F, 0F));
		r.add(new ObjMap(0F, 0.5F, 0F));
		r.add(new ObjMap(0F, 2F, 0F));
		r.add(new ObjMap(0F, 0.5F, 0F));
		r.add(new ObjMap(2F, 1F, 2F));
		r.add(new ObjMap(0F, 0.5F, 0F));
		r.add(new ObjMap(1F, 2F, 0F));
		r.add(new ObjMap(0F, 0.5F, 0F));		
		return r;
	}
}
