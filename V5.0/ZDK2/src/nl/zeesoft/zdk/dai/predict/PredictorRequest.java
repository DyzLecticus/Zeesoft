package nl.zeesoft.zdk.dai.predict;

import java.util.ArrayList;
import java.util.List;

public class PredictorRequest {
	public int							maxTimeMs			= 0;
	public int							minCacheIndex		= 0;
	public int							maxCacheIndex		= Integer.MAX_VALUE;
	
	public List<PredictorCacheResult>	cacheResults		= new ArrayList<PredictorCacheResult>();
}
