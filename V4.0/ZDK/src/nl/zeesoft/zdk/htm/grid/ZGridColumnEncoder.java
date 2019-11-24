package nl.zeesoft.zdk.htm.grid;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.enc.CombinedEncoder;
import nl.zeesoft.zdk.htm.util.DateTimeSDR;

public abstract class ZGridColumnEncoder {
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
	
	protected abstract DateTimeSDR encodeRequestValue(int columnIndex,ZGridRequest request);
	
	protected long getInputValueAsLong(int columnIndex,ZGridRequest request) {
		long r = 0;
		if (request.inputValues.length>columnIndex &&
			request.inputValues[columnIndex]!=null &&
			request.inputValues[columnIndex] instanceof Long
			) {
			r = (Long) request.inputValues[columnIndex];
		}
		return r;
	}
	
	protected float getInputValueAsFloat(int columnIndex,ZGridRequest request) {
		float r = 0;
		if (request.inputValues.length>columnIndex &&
			request.inputValues[columnIndex]!=null
			) {
			if (request.inputValues[columnIndex] instanceof Float) {
				r = (float) request.inputValues[columnIndex];
			} else if (request.inputValues[columnIndex] instanceof Long) {
				r = (float) (long) request.inputValues[columnIndex];
			} else if (request.inputValues[columnIndex] instanceof Integer) {
				r = (float) (int) request.inputValues[columnIndex];
			}
		}
		return r;
	}
}
