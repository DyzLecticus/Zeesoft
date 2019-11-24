package nl.zeesoft.zdk.htm.grid;

import nl.zeesoft.zdk.htm.util.SDR;

public abstract class ZGridColumnEncoder {
	public abstract int length();
	
	protected abstract SDR encodeRequestValue(int columnIndex,ZGridRequest request);
	
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
}
