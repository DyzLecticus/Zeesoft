package nl.zeesoft.zdk.htm.util;

import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * A DateTimeSDR is used by streams to propagate the actual input along with the SDR so classifiers can learn to classify and predict real values. 
 */
public class DateTimeSDR extends SDR {
	public static final String			VALUE_KEY	= "VALUE";
	public static final String			LABEL_KEY	= "LABEL";
	
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
}
