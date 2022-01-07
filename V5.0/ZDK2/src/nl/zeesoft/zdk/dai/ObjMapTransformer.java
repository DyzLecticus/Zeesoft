package nl.zeesoft.zdk.dai;

public class ObjMapTransformer {
	public int resolution = 3;
	
	public ObjMap getTransformation(ObjMap from, ObjMap to) {
		ObjMap r = new ObjMap();
		for (String key: to.values.keySet()) {
			if (from.values.containsKey(key)) {
				Object fromVal = from.values.get(key);
				Object toVal = to.values.get(key);
				if (fromVal!=null && toVal!=null && fromVal.getClass()==toVal.getClass()) {
					r.values.put(key, calculateValueTransformation(fromVal, toVal, key));
				}
			}
		}
		return r;
	}
	
	public float calculateValueTransformation(Object fromVal, Object toVal, String key) {
		float perc = 1F;
		if (fromVal instanceof Float) {
			float f = (Float)fromVal;
			if (f!=0F) {
				perc = (Float)toVal / f;
			} else {
				perc = Float.MAX_VALUE;
			}
		} else if (fromVal instanceof Integer) {
			float f = ((Integer)fromVal).floatValue();
			if (f!=0F) {
				perc = ((Integer)toVal).floatValue() / f;
			} else {
				perc = Float.MAX_VALUE;
			}
		}
		return perc;
	}

	public ObjMap applyTransformation(ObjMap from, ObjMap transformation) {
		ObjMap r = new ObjMap();
		applyTransformation(from, transformation, r);
		return r;
	}

	public void applyTransformation(ObjMap from, ObjMap transformation, ObjMap to) {
		for (String key: transformation.values.keySet()) {
			if (from.values.containsKey(key)) {
				Object fromVal = from.values.get(key);
				if (fromVal!=null) {
					float perc = (Float)transformation.values.get(key);
					to.values.put(key, calculateTransformedValue(fromVal, perc, key));
				}
			}
		}
	}
	
	public Object calculateTransformedValue(Object fromVal, float perc, String key) {
		Object toVal = fromVal;
		if (fromVal instanceof Float) {
			toVal = (Float)fromVal * perc;
		} else if (fromVal instanceof Integer) {
			toVal = Math.round(((Integer)fromVal).floatValue() * perc);
		}
		return toVal;
	}
}
