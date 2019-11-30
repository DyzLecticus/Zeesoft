package nl.zeesoft.zdk.htm.grid.enc;

import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.htm.enc.CombinedEncoder;
import nl.zeesoft.zdk.htm.enc.GridDimensionScaledEncoder;
import nl.zeesoft.zdk.htm.enc.ScalarEncoder;
import nl.zeesoft.zdk.htm.grid.ZGridColumnEncoder;
import nl.zeesoft.zdk.htm.grid.ZGridResult;
import nl.zeesoft.zdk.htm.util.DateTimeSDR;
import nl.zeesoft.zdk.htm.util.SDR;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class ZGridEncoderValue extends ZGridColumnEncoder {
	private String			valueKey		= DateTimeSDR.VALUE_KEY;
	private int				length			= 256;
	private int				bits			= 8;
	private float			resolution		= 1;
	private float			minValue		= 0;
	private float			maxValue		= 255 - bits;
	private boolean			periodic		= false;
	private boolean			scaled			= false;

	public ZGridEncoderValue() {
		rebuildEncoder();
	}
	
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

	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("length","" + length));
		json.rootElement.children.add(new JsElem("bits","" + bits));
		json.rootElement.children.add(new JsElem("resolution","" + resolution));
		json.rootElement.children.add(new JsElem("minValue","" + minValue));
		json.rootElement.children.add(new JsElem("maxValue","" + maxValue));
		json.rootElement.children.add(new JsElem("periodic","" + periodic));
		json.rootElement.children.add(new JsElem("scaled","" + scaled));
		json.rootElement.children.add(new JsElem("valueKey",valueKey,true));
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			length = json.rootElement.getChildInt("length",length);
			bits = json.rootElement.getChildInt("bits",bits);
			resolution = json.rootElement.getChildFloat("resolution",resolution);
			minValue = json.rootElement.getChildFloat("minValue",minValue);
			maxValue = json.rootElement.getChildFloat("maxValue",maxValue);
			periodic = json.rootElement.getChildBoolean("periodic",periodic);
			scaled = json.rootElement.getChildBoolean("scaled",scaled);
			valueKey = json.rootElement.getChildString("valueKey",valueKey);
			rebuildEncoder();
		}
	}
	
	@Override
	protected DateTimeSDR encodeRequestValue(int columnIndex,ZGridResult result) {
		float value = getInputValueAsFloat(columnIndex, result);
		DateTimeSDR r = new DateTimeSDR(getSDRForValue(value));
		r.dateTime = result.getRequest().dateTime;
		r.keyValues.put(valueKey,value);
		if (result.getRequest().inputLabels.length>columnIndex &&
			result.getRequest().inputLabels[columnIndex]!=null &&
			result.getRequest().inputLabels[columnIndex].length()>0
			) {
			r.keyValues.put(DateTimeSDR.LABEL_KEY,result.getRequest().inputLabels[columnIndex]);
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
