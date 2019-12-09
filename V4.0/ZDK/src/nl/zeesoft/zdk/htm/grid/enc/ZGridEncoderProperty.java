package nl.zeesoft.zdk.htm.grid.enc;

import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.htm.enc.CombinedEncoder;
import nl.zeesoft.zdk.htm.enc.PropertyEncoder;
import nl.zeesoft.zdk.htm.grid.ZGridColumnEncoder;
import nl.zeesoft.zdk.htm.grid.ZGridResult;
import nl.zeesoft.zdk.htm.util.DateTimeSDR;
import nl.zeesoft.zdk.htm.util.SDR;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class ZGridEncoderProperty extends ZGridColumnEncoder {
	private String			valueKey		= "PROPERTY";
	private int				length			= 256;
	private int				bits			= 8;
	
	private PropertyEncoder	propertyEncoder	= null;

	public ZGridEncoderProperty() {
		rebuildEncoder();
	}
	
	public ZGridEncoderProperty(int length) {
		this.length = length;
		rebuildEncoder();
	}
	
	public ZGridEncoderProperty(int length,String valueKey) {
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
	
	public boolean addProperty(String property) {
		return propertyEncoder.getValueForProperty(property) > 0;
	}
	
	public SDR getSDRForValue(float value) {
		SDR r = null;
		SortedMap<String,Float> values = new TreeMap<String,Float>();
		values.put(valueKey,value);
		r = encoder.getSDRForValues(values);
		return r;
	}
	
	public SDR getSDRForProperty(String property) {
		return propertyEncoder.getSDRForProperty(property);
	}

	public float getValueForProperty(String property) {
		return propertyEncoder.getValueForProperty(property);
	}

	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("length","" + length));
		json.rootElement.children.add(new JsElem("bits","" + bits));
		json.rootElement.children.add(new JsElem("valueKey",valueKey,true));
		return json;
	}
	
	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			length = json.rootElement.getChildInt("length",length);
			bits = json.rootElement.getChildInt("bits",bits);
			valueKey = json.rootElement.getChildString("valueKey",valueKey);
			rebuildEncoder();
		}
	}
	
	@Override
	protected DateTimeSDR encodeRequestValue(int columnIndex,ZGridResult result) {
		DateTimeSDR r = null;
		String property = "";
		String label = getInputLabel(columnIndex,result);
		if (hasInputValue(columnIndex,result) &&
			result.getRequest().inputValues[columnIndex] instanceof String
			) {
			property = (String) result.getRequest().inputValues[columnIndex];
		} else {
			property = label;
			label = "";
		}
		if (property.length()>0) {
			float value = propertyEncoder.getValueForProperty(property);
			r = new DateTimeSDR(getSDRForValue(value));
			r.keyValues.put(valueKey,value);
		} else {
			r = getNewDateTimeSDR();
		}
		r.dateTime = result.getRequest().dateTime;
		if (label.length()>0) {
			r.keyValues.put(DateTimeSDR.LABEL_KEY,label);
		}
		return r;
	}
	
	protected void rebuildEncoder() {
		encoder = new CombinedEncoder();
		propertyEncoder = new PropertyEncoder(length,bits);
		encoder.addEncoder(valueKey,propertyEncoder);
	}
}
