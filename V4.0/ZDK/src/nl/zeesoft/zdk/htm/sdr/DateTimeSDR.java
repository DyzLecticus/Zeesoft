package nl.zeesoft.zdk.htm.sdr;

import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

public class DateTimeSDR extends SDR {
	public	long						dateTime	= System.currentTimeMillis();
	public	SortedMap<String,Object>	keyValues	= new TreeMap<String,Object>();
	
	public DateTimeSDR(int length) {
		super(length);
	}
	
	public DateTimeSDR(SDR sdr) {
		super(sdr.length());
		for (Integer onBit: sdr.getOnBits()) {
			setBit(onBit,true);
		}
	}
	
	@Override
	public SDR copy() {
		DateTimeSDR r = new DateTimeSDR(super.copy());
		r.dateTime = new Long(dateTime);
		for (Entry<String,Object> entry: keyValues.entrySet()) {
			r.keyValues.put(entry.getKey(),entry.getValue());
		}
		return r;
	}
	
	public static Float getValueFromSDR(DateTimeSDR sdr,String valueKey) {
		Float r = null;
		Object val = sdr.keyValues.get(valueKey);
		if (val!=null) {
			r = objectToFloat(val);
		}
		return r;
	}
	
	public static float objectToFloat(Object val) {
		float r = 0;
		if (val!=null) {
			if (val instanceof Float) {
				r = (float) val;
			} else if (val instanceof Integer) {
				r = (float) (Integer) val;
			} else if (val instanceof Long) {
				r = (float) (Long) val;
			}
		}
		return r;
	}
}
