package nl.zeesoft.zdk.dai.ompredict;

import java.util.List;

import nl.zeesoft.zdk.dai.ObjMapComparator;
import nl.zeesoft.zdk.dai.cache.CacheIndexesGenerator;
import nl.zeesoft.zdk.dai.omcache.OMCacheConfig;

public class OMPredictorConfig {
	public ObjMapComparator		comparator			= new ObjMapComparator();
	public int					maxHistorySize		= 1000;
	public int					maxMsLoggerSize		= 1000;

	public List<Integer>		cacheIndexes		= CacheIndexesGenerator.generate();
	public OMCacheConfig		cacheConfig			= new OMCacheConfig();
	
	public OMPredictorConfig() {
		cacheConfig.initializeDefault();
		cacheConfig.setComparator(comparator);
	}
	
	public void setComparator(ObjMapComparator comparator) {
		this.comparator = comparator;
		cacheConfig.setComparator(comparator);
	}
}
