package nl.zeesoft.zdk.dai.predict;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.dai.ObjMapComparator;
import nl.zeesoft.zdk.dai.cache.CacheIndexesGenerator;

public class PredictorConfig {
	public ObjMapComparator		comparator		= new ObjMapComparator();
	public int					maxHistorySize	= 1000;

	public List<Integer>		cacheIndexes	= CacheIndexesGenerator.generate();
	public List<CacheConfig>	cacheConfigs	= new ArrayList<CacheConfig>();
	
	public PredictorConfig() {
		float subtract = 0F;
		for (int i = 0; i < 3; i++) {
			CacheConfig cc = new CacheConfig();
			cc.mergeSimilarity = cc.mergeSimilarity - subtract;
			cacheConfigs.add(cc);
			subtract += 0.1F;
		}
	}
}
