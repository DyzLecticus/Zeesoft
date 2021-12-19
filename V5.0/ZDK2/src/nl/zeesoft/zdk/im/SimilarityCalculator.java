package nl.zeesoft.zdk.im;

public class SimilarityCalculator {
	public float calculateSimilarity(ObjectArrayList a, ObjectArrayList b) {
		float perc = 1;
		int max = a.list.size();
		if (max < b.list.size()) {
			max = b.list.size();
		}
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
	
	public float calculateSimilarity(ObjectArray a, ObjectArray b) {
		float perc = 1;
		int max = a.objects.length;
		if (max < b.objects.length) {
			max = b.objects.length;
		}
		if (max > 0) {
			perc = 0;
			for (int i = 0; i < max; i++) {
				if (a.objects.length > i && b.objects.length > i) {
					perc += calculateSimilarity(a.objects[i], b.objects[i]);
				}
			}
			perc = perc / (float) max;
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
		float lowest = a;
		if (b < a) {
			lowest = b;
		}
		if (lowest < 0) {
			a += (lowest * -2);
			b += (lowest * -2);
		}
		if (a > b) {
			perc = (a - b) / ((a + b) / 2);
		} else {
			perc = (b - a) / ((a + b) / 2);
		}
		perc = 1F - (perc / 2F);
		return perc;
	}
}
