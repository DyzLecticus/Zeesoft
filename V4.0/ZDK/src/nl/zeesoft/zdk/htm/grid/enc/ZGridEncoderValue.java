package nl.zeesoft.zdk.htm.grid.enc;

import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.htm.enc.CombinedEncoder;
import nl.zeesoft.zdk.htm.enc.GridDimensionScaledEncoder;
import nl.zeesoft.zdk.htm.enc.ScalarEncoder;
import nl.zeesoft.zdk.htm.grid.ZGridColumnEncoder;
import nl.zeesoft.zdk.htm.grid.ZGridRequest;
import nl.zeesoft.zdk.htm.util.DateTimeSDR;
import nl.zeesoft.zdk.htm.util.SDR;

public class ZGridEncoderValue extends ZGridColumnEncoder {
	private int				length			= 256;
	
	private String			valueKey		= DateTimeSDR.VALUE_KEY;
	private int				bits			= 8;
	private float			resolution		= 1;
	private float			minValue		= 0;
	private float			maxValue		= 255 - bits;
	private boolean			periodic		= false;
	private boolean			scaled			= true;

	public ZGridEncoderValue(int length) {
		this.length = length;
		rebuildEncoder();
	}
	
	public ZGridEncoderValue(int length,String valueKey) {
		this.length = length;
		this.valueKey = valueKey;
		rebuildEncoder();
	}
	
	@Override
	public String getValueKey() {
		return valueKey;
	}

	public void setValueKey(String valueKey) {
		this.valueKey = valueKey;
		rebuildEncoder();
	}

	public void setBits(int bits) {
		this.bits = bits;
		rebuildEncoder();
	}

	public void setResolution(float resolution) {
		this.resolution = resolution;
		rebuildEncoder();
	}

	public void setMinValue(float minValue) {
		this.minValue = minValue;
		rebuildEncoder();
	}

	public void setMaxValue(float maxValue) {
		this.maxValue = maxValue;
		rebuildEncoder();
	}

	public void setPeriodic(boolean periodic) {
		this.periodic = periodic;
		rebuildEncoder();
	}
	
	public void setScaled(boolean scaled) {
		this.scaled = scaled;
		rebuildEncoder();
	}
	
	public SDR getSDRForValue(float value) {
		SDR r = null;
		SortedMap<String,Float> values = new TreeMap<String,Float>();
		values.put(valueKey,value);
		r = encoder.getSDRForValues(values);
		return r;
	}
	
	protected DateTimeSDR encodeRequestValue(int columnIndex,ZGridRequest request) {
		float value = getInputValueAsFloat(columnIndex, request);
		DateTimeSDR r = new DateTimeSDR(getSDRForValue(value));
		r.dateTime = request.dateTime;
		r.keyValues.put(valueKey,value);
		if (request.inputLabels.length>columnIndex &&
			request.inputLabels[columnIndex]!=null &&
			request.inputLabels[columnIndex].length()>0
			) {
			r.keyValues.put(DateTimeSDR.LABEL_KEY,request.inputLabels[columnIndex]);
		}
		return r;
	}
	
	protected void rebuildEncoder() {
		encoder = new CombinedEncoder();
		if (scaled) {
			GridDimensionScaledEncoder enc = new GridDimensionScaledEncoder(length);
			if (resolution!=1) {
				enc.setResolution(resolution);
			}
			encoder.addEncoder(valueKey,enc);
		} else {
			ScalarEncoder enc = new ScalarEncoder(length,bits,minValue,maxValue);
			if (resolution!=1) {
				enc.setResolution(resolution);
			}
			if (periodic) {
				enc.setPeriodic(periodic);
			}
			encoder.addEncoder(valueKey,enc);
		}
	}
}
