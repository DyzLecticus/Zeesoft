package nl.zeesoft.zdk.test.dai;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Console;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.dai.History;
import nl.zeesoft.zdk.dai.KeyPrediction;
import nl.zeesoft.zdk.dai.MapPrediction;
import nl.zeesoft.zdk.dai.ObjMap;
import nl.zeesoft.zdk.dai.ObjMapComparator;
import nl.zeesoft.zdk.dai.Prediction;
import nl.zeesoft.zdk.dai.cache.Cache;
import nl.zeesoft.zdk.dai.cache.CacheElement;
import nl.zeesoft.zdk.dai.cache.CacheResult;
import nl.zeesoft.zdk.dai.cache.SuperCacheBuilder;

public class TestHistory {
	private static TestHistory	self	= new TestHistory();
	
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);

		assert new History() != null;
		assert new CacheElement() != null;
		assert new MapPrediction() != null;
		assert new KeyPrediction() != null;
		
		History history = new History(16);
		history.maxSize = 16;
		history.updateCache = false;
		
		Console.log(history.cache.indexes);
		
		ObjMapComparator comparator = new ObjMapComparator();
		
		history.addAll(getPattern());
		history.addAll(getPattern());
	
		assert history.list.size() == 16;
		assert history.cache.elements.size() == 0;
		
		history.cache.indexes.clear();
		history.cache.indexes.add(0);
		history.cache.indexes.add(1);
		history.cache.indexes.add(2);
		history.cache.indexes.add(3);
		history.updateCache = true;
		history.addAll(getPattern());
		history.addAll(getPattern());
		assert history.list.size() == 16;
		assert history.cache.elements.size() == 13;
		assert history.cache.elements.get(0).toString().length() == 122;
		
		CacheResult result = history.getCacheResult(comparator, 0.5F);
		assert result.similarity == 1.0F;
		assert result.results.size() == 1;
		assert result.results.get(0).nextMap.equals(new ObjMap(2.0F, 1.0F, 0F));
		assert result.results.get(0).count == 2;
		
		history = new History(16);
		history.cache.indexes.clear();
		history.cache.indexes.add(0);
		history.cache.indexes.add(1);
		history.cache.indexes.add(2);
		history.cache.indexes.add(3);
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
		
		SuperCacheBuilder builder = new SuperCacheBuilder();
		
		Cache superCache = builder.buildSuperCache(history.cache, comparator, 0.9F);
		Console.log(superCache.elements.size());
		result = superCache.getCacheResult(history.getSubList(0, superCache.indexes), comparator, 0.5F);
		prediction = result.getPrediction();
		Logger.debug(self, "Super cache prediction;\n" + prediction);
		
		Cache superSuperCache = builder.buildSuperCache(superCache, comparator, 0.6F);
		Console.log(superSuperCache.elements.size());
		result = superSuperCache.getCacheResult(history.getSubList(0, superSuperCache.indexes), comparator, 0.5F);
		prediction = result.getPrediction();
		Logger.debug(self, "Super super cache prediction;\n" + prediction);
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
