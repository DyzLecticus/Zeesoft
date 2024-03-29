package nl.zeesoft.zdk.dai.predict;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.dai.ObjMapComparator;
import nl.zeesoft.zdk.dai.cache.CacheIndexesGenerator;

public class PredictorConfig {
	public ObjMapComparator		comparator			= new ObjMapComparator();
	public int					maxHistorySize		= 1000;
	public int					maxMsLoggerSize		= 1000;

	public List<Integer>		cacheIndexes		= CacheIndexesGenerator.generate();
	public List<CacheConfig>	cacheConfigs		= new ArrayList<CacheConfig>();
	
	public PredictorConfig() {
		float subtract = 0F;
		int maxSize = 10000;
		for (int i = 0; i < 3; i++) {
			CacheConfig cc = new CacheConfig();
			cc.mergeSimilarity = cc.mergeSimilarity - subtract;
			cc.maxSize = maxSize;
			if (cc.mergeSimilarity < 1F) {
				cc.linked = true;
				cc.linkedMergeSimilarity = (1F + cc.mergeSimilarity) / 2F;
				cc.linkedMaxSize = cc.maxSize / 2;
			}
			cacheConfigs.add(cc);
			subtract += 0.1F;
			maxSize = (int)((float)maxSize * (subtract * 2));
		}
	}
}
