package nl.zeesoft.zdk.htm.enc;

import java.util.SortedMap;

import nl.zeesoft.zdk.htm.sdr.SDR;

public class DateTimeValueEncoder extends DateTimeEncoder {
	private static final String		VALUE1		= "VALUE1";
	private static final String		VALUE2		= "VALUE2";
	
	public SDR getSDRForValue(long dateTime,int value1,int value2) {
		SortedMap<String,Float> values = getValuesForDateTime(dateTime);
		values.put(VALUE1,(float)value1);
		values.put(VALUE2,(float)value2);
		return getSDRForValues(values);
	}

	@Override
	protected void initialize() {
		super.initialize();
		addEncoder(VALUE1,getNewValueEncoder());
		addEncoder(VALUE2,getNewValueEncoder());
	}
	
	protected EncoderObject getNewValueEncoder() {
		return new RDScalarEncoder(40,8);
	}
}
