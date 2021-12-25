package nl.zeesoft.zdk.dai;

import java.util.List;

public class ObjMapComparator {
	public float calculateSimilarity(ObjMapList a, ObjMapList b) {
		float perc = 1;
		int max = max(a.list.size(), b.list.size());
		if (max > 0) {
			perc = 0;
			for (int i = 0; i < max; i++) {
				if (a.list.size() > i && b.list.size() > i) {
					perc += calculateSimilarity(a.list.get(i), b.list.get(i));
				}
			}
			perc = perc / (float) max;
		}
		return perc;
	}
	
	public float calculateSimilarity(ObjMap a, ObjMap b) {
		float perc = 1;
		List<String> overlap = a.getOverlappingKeys(b);
		int total = overlap.size();
		List<String> nonOverlap = a.getNonOverlappingKeys(b);
		total += nonOverlap.size();
		if (total>0) {
			perc = 0;
			for (String key: overlap) {
				perc += calculateSimilarity(a.values.get(key), b.values.get(key));
			}
			perc = perc / (float) total;
		}
		return perc;
	}
	
	public float calculateSimilarity(Object a, Object b) {
		float perc = 0;
		if (a==null && b==null) {
			perc = 1;
		} else if (a!=null && b!=null && a.getClass() == b.getClass()) {
			if (a.equals(b)) {
				perc = 1;
			} else {
				perc = calculateValueSimilarity(a, b);
			}
		}
		return perc;
	}
	
	public float calculateValueSimilarity(Object a, Object b) {
		float perc = 0;
		if (a instanceof Float) {
			perc = calculateFloatSimilarity((Float) a, (Float) b);
		}
		return perc;
	}
	
	public float calculateFloatSimilarity(float a, float b) {
		float perc = 0;
		float min = min(a, b);
		if (min < 0) {
			a += (min * -2);
			b += (min * -2);
		}
		if (a > b) {
			perc = (a - b) / ((a + b) / 2);
		} else {
			perc = (b - a) / ((a + b) / 2);
		}
		perc = 1F - (perc / 2F);
		return perc;
	}
	
	private int max(int a, int b) {
		int r = a;
		if (b > r) {
			r = b;
		}
		return r;
	}
	
	private float min(float a, float b) {
		float r = a;
		if (b < r) {
			r = b;
		}
		return r;
	}
}
