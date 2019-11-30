package nl.zeesoft.zdk.htm.grid;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.enc.CombinedEncoder;
import nl.zeesoft.zdk.htm.util.DateTimeSDR;
import nl.zeesoft.zdk.json.JsAble;

public abstract class ZGridColumnEncoder implements JsAble {
	protected CombinedEncoder encoder	=	null;
	
	public abstract String getValueKey();
	
	public int length() {
		return encoder.length();
	}
	
	public ZStringBuilder getDescription() {
		return encoder.getDescription();
	}
	
	public ZStringBuilder testScalarOverlap() {
		return encoder.testScalarOverlap();
	}
	
	protected abstract DateTimeSDR encodeRequestValue(int columnIndex,ZGridResult result);
	
	protected long getInputValueAsLong(int columnIndex,ZGridResult result) {
		long r = 0;
		if (result.getRequest().inputValues.length>columnIndex &&
			result.getRequest().inputValues[columnIndex]!=null &&
			result.getRequest().inputValues[columnIndex] instanceof Long
			) {
			r = (Long) result.getRequest().inputValues[columnIndex];
		}
		return r;
	}
	
	protected float getInputValueAsFloat(int columnIndex,ZGridResult result) {
		float r = 0;
		if (result.getRequest().inputValues.length>columnIndex &&
			result.getRequest().inputValues[columnIndex]!=null
			) {
			if (result.getRequest().inputValues[columnIndex] instanceof Float) {
				r = (float) result.getRequest().inputValues[columnIndex];
			} else if (result.getRequest().inputValues[columnIndex] instanceof Long) {
				r = (float) (long) result.getRequest().inputValues[columnIndex];
			} else if (result.getRequest().inputValues[columnIndex] instanceof Integer) {
				r = (float) (int) result.getRequest().inputValues[columnIndex];
			}
		}
		return r;
	}
}
