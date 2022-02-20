package nl.zeesoft.zdk.test.dai;

import nl.zeesoft.zdk.Console;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.dai.ObjMap;
import nl.zeesoft.zdk.dai.ObjMapList;
import nl.zeesoft.zdk.dai.Prediction;
import nl.zeesoft.zdk.dai.supercache.OMCache;
import nl.zeesoft.zdk.dai.supercache.OMCacheConfig;
import nl.zeesoft.zdk.dai.supercache.OMCacheResult;
import nl.zeesoft.zdk.dai.supercache.OMCacheResultSummary;

public class TestOMCache {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);
		
		OMCacheConfig config = new OMCacheConfig();
		config.initializeDefault();
		
		assert config.toString().equals("Merge/Size: 0.85/1000 -> Merge/Size: 0.925/1000 -> Merge/Size: 1.0/1000");
		
		OMCache cache = new OMCache(config);
		
		cache.hit(getKey(0), getValue(0));
		cache.hit(getKey(1), getValue(1));
		cache.hit(getKey(2), getValue(2));
		cache.hit(getKey(3), getValue(3));
		cache.hit(getKey(0), getValue(0));
		cache.hit(getKey(1), getValue(1));
		cache.hit(getKey(2), getValue(2));
		cache.hit(getKey(3), getValue(3));
		cache.hit(getKey(0), getValue(0));
		cache.hit(getKey(1), getValue(1));
		cache.hit(getKey(2), getValue(2));
		cache.hit(getKey(3), getValue(3));
		
		//Console.log(cache.elements.size());
		//Console.log(cache.elements.get(0).subCache.elements.size());
		//Console.log(cache.elements.get(0).subCache.elements.get(0).subCache.elements.size());

		ObjMapList key = getKey(1);
		key.list.get(0).values.put("3", 8.5F);
		OMCacheResult result = cache.lookup(key);
		//Console.log(result.elements.size());
		//Console.log(result.elements.get(0).value);
		//Console.log(result.subResults.get(0).elements.get(0).value);
		
		OMCacheResultSummary summary = new OMCacheResultSummary(result);
		//Console.log(summary.primary.elements.get(0).value);
		//Console.log(summary.addCount);
		Prediction prediction = summary.getPrediction();
		Console.log(prediction);
		Console.log(getValue(1));
	}
	
	public static ObjMapList getKey(int index) {
		float v = 5F + (float) index;
		ObjMapList key = new ObjMapList();
		key.add(new ObjMap(1,index + 1,v));
		v += 1F;
		key.add(new ObjMap(1,index + 2,v));
		v += 1F;
		key.add(new ObjMap(1,index + 3,v));
		v += 1F;
		return key;
	}
	
	public static ObjMap getValue(int index) {
		float v = 9F + (float) index;
		return new ObjMap(1,index + 4,v);
	}
}
