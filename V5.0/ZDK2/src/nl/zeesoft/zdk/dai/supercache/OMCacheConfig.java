package nl.zeesoft.zdk.dai.supercache;

import nl.zeesoft.zdk.dai.ObjMapComparator;

public class OMCacheConfig {
	public ObjMapComparator	comparator		= new ObjMapComparator();
	public float			mergeSimilarity	= 1F;
	public int				maxSize			= 1000;
	public OMCacheConfig	subConfig		= null;
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("Merge/Size: ");
		str.append(mergeSimilarity);
		str.append("/");
		str.append(maxSize);
		if (subConfig!=null) {
			str.append(" -> ");
			str.append(subConfig.toString());
		}
		return str.toString();
	}
	
	public void initializeDefault() {
		initialize(0.85F, 0.075F);
	}
	
	public void initialize(float start, float increment) {
		OMCacheConfig cfg = this;
		float sim = start;
		for (int i = 0; i < 5; i++) {
			cfg.mergeSimilarity = sim;
			sim += increment;
			if (sim<=1F) {
				cfg.subConfig = new OMCacheConfig();
				cfg = cfg.subConfig;
			} else {
				break;
			}
		}
	}
}
