package nl.zeesoft.zdk.htm.grid.enc;

import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.htm.enc.CombinedEncoder;
import nl.zeesoft.zdk.htm.enc.GridDimensionEncoder;
import nl.zeesoft.zdk.htm.enc.GridDimensionScaledEncoder;
import nl.zeesoft.zdk.htm.enc.RDScalarEncoder;
import nl.zeesoft.zdk.htm.enc.ScalarEncoder;
import nl.zeesoft.zdk.htm.grid.ZGridColumnEncoder;
import nl.zeesoft.zdk.htm.grid.ZGridResult;
import nl.zeesoft.zdk.htm.util.DateTimeSDR;
import nl.zeesoft.zdk.htm.util.SDR;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class ZGridEncoderValue extends ZGridColumnEncoder {
	public static final String	TYPE_SCALAR			= "SCALAR";
	public static final String	TYPE_SCALED			= "SCALED";
	public static final String	TYPE_DIMENSIONAL	= "DIMENSIONAL";
	public static final String	TYPE_RANDOM			= "RANDOM";
	
	private String				valueKey			= DateTimeSDR.VALUE_KEY;
	private String				type				= TYPE_SCALAR;
	private int					length				= 256;
	private int					bits				= 8;
	private float				resolution			= 1;
	private float				minValue			= 0;
	private float				maxValue			= 255 - bits;
	private boolean				periodic			= false;

	public ZGridEncoderValue() {
		rebuildEncoder();
	}
	
	public ZGridEncoderValue(int length) {
		this.length = length;
		rebuildEncoder();
	}
	
	public ZGridEncoderValue(int length,String valueKey) {
		this.length = length;
		if (valueKey.length()>0) {
			this.valueKey = valueKey;
		}
		rebuildEncoder();
	}
	
	@Override
	public String getValueKey() {
		return valueKey;
	}

	public void setType(String type) {
		if (type.equals(TYPE_SCALAR) ||
			type.equals(TYPE_SCALED) ||
			type.equals(TYPE_DIMENSIONAL) ||
			type.equals(TYPE_RANDOM)
			) {
			this.type = type;
			rebuildEncoder();
		}
	}

	public void setValueKey(String valueKey) {
		if (valueKey.length()>0) {
			this.valueKey = valueKey;
		}
		rebuildEncoder();
	}

	public void setLength(int length) {
		this.length = length;
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

	public SDR getSDRForFloatValue(float value) {
		SDR r = null;
		SortedMap<String,Float> values = new TreeMap<String,Float>();
		values.put(valueKey,value);
		r = encoder.getSDRForValues(values);
		return r;
	}

	public SDR getSDRForIntegerValue(int value) {
		return getSDRForFloatValue((float) value);
	}

	public SDR getSDRForLongValue(long value) {
		return getSDRForFloatValue((float) value);
	}

	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("type",type,true));
		json.rootElement.children.add(new JsElem("length","" + length));
		json.rootElement.children.add(new JsElem("bits","" + bits));
		json.rootElement.children.add(new JsElem("resolution","" + resolution));
		json.rootElement.children.add(new JsElem("minValue","" + minValue));
		json.rootElement.children.add(new JsElem("maxValue","" + maxValue));
		json.rootElement.children.add(new JsElem("periodic","" + periodic));
		json.rootElement.children.add(new JsElem("valueKey",valueKey,true));
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			type = json.rootElement.getChildString("type",type);
			length = json.rootElement.getChildInt("length",length);
			bits = json.rootElement.getChildInt("bits",bits);
			resolution = json.rootElement.getChildFloat("resolution",resolution);
			minValue = json.rootElement.getChildFloat("minValue",minValue);
			maxValue = json.rootElement.getChildFloat("maxValue",maxValue);
			periodic = json.rootElement.getChildBoolean("periodic",periodic);
			valueKey = json.rootElement.getChildString("valueKey",valueKey);
			rebuildEncoder();
		}
	}
	
	@Override
	protected DateTimeSDR encodeRequestValue(int columnIndex,ZGridResult result) {
		DateTimeSDR r = null;
		Object value = null;
		if (hasInputValue(columnIndex,result)) {
			value = result.getRequest().inputValues[columnIndex];
			if (value instanceof Float) {
				r = new DateTimeSDR(getSDRForFloatValue((float)value));
			} else if (value instanceof Integer) {
				r = new DateTimeSDR(getSDRForIntegerValue((int)value));
			} else if (value instanceof Long) {
				r = new DateTimeSDR(getSDRForLongValue((long)value));
			}
		}
		if (r==null) {
			r = new DateTimeSDR(encoder.length());
		}
		r.dateTime = result.getRequest().dateTime;
		if (value!=null) {
			r.keyValues.put(valueKey,value);
		}
		String label = getInputLabel(columnIndex,result);
		if (label.length()>0) {
			r.keyValues.put(DateTimeSDR.LABEL_KEY,label);
		}
		return r;
	}
	
	protected void rebuildEncoder() {
		encoder = new CombinedEncoder();
		if (type.equals(TYPE_SCALAR)) {
			ScalarEncoder enc = new ScalarEncoder(length,bits,minValue,maxValue);
			if (resolution!=1) {
				enc.setResolution(resolution);
			}
			if (periodic) {
				enc.setPeriodic(periodic);
			}
			encoder.addEncoder(valueKey,enc);
		} else if (type.equals(TYPE_SCALED)) {
			GridDimensionScaledEncoder enc = new GridDimensionScaledEncoder(length);
			if (resolution!=1) {
				enc.setResolution(resolution);
			}
			encoder.addEncoder(valueKey,enc);
		} else if (type.equals(TYPE_DIMENSIONAL)) {
			GridDimensionEncoder enc = new GridDimensionEncoder(length,bits);
			if (resolution!=1) {
				enc.setResolution(resolution);
			}
			encoder.addEncoder(valueKey,enc);
		} else if (type.equals(TYPE_RANDOM)) {
			RDScalarEncoder enc = new RDScalarEncoder(length,bits);
			if (resolution!=1) {
				enc.setResolution(resolution);
			}
			encoder.addEncoder(valueKey,enc);
		}
	}
}
