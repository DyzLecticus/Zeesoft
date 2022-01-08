package nl.zeesoft.zdk.dai;

public class ObjMapTransformer {
	public float	minValue	= 0.0000000001F;
	
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
			float f = getMinValue((Float)fromVal);
			perc = (Float)toVal / f;
		} else if (fromVal instanceof Integer) {
			float f = getMinValue(((Integer)fromVal).floatValue());
			perc = ((Integer)toVal).floatValue() / f;
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
			float f = getMinValue((Float)fromVal);
			toVal = f * perc;
		} else if (fromVal instanceof Integer) {
			float f = getMinValue(((Integer)fromVal).floatValue());
			toVal = Math.round(f * perc);
		}
		return toVal;
	}
	
	public float getMinValue(float value) {
		float r = value;
		boolean reverse = false;
		if (r < 0F) {
			reverse = true;
			r = r * -1F;
		}
		if (r < minValue) {
			r = minValue;
		}
		if (reverse) {
			r = r * -1F;
		}
		return r;
	}
}
