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
}
