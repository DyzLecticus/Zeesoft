package nl.zeesoft.zdk.htm.grid;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.enc.CombinedEncoder;
import nl.zeesoft.zdk.htm.util.DateTimeSDR;
import nl.zeesoft.zdk.json.JsAble;

/**
 * ZGridColumnEncoder instances are used to translate ZGridRequest input values into SDRs for further grid processing.  
 */
public abstract class ZGridColumnEncoder implements JsAble {
	protected CombinedEncoder encoder	=	null;

	/**
	 * Returns a copy of this encoder.
	 * 
	 * @return A copy of this encoder
	 */
	public abstract ZGridColumnEncoder copy();

	/**
	 * Returns the value key used to store the original value that created the DateTimeSDR in its keyValues property.
	 * 
	 * @return The value key
	 */
	public abstract String getValueKey();
	
	/**
	 * Returns the length of the SDRs this encoder generates.
	 * 
	 * @return The length
	 */
	public int length() {
		return encoder.length();
	}
	
	/**
	 * Returns the description of this encoder.
	 * @return The description
	 */
	public ZStringBuilder getDescription() {
		return encoder.getDescription();
	}
	
	/**
	 * Returns an error message if the scalar encoders in the encoder do not overlap.
	 * 
	 * @return An empty string builder or a string builder containing an error message
	 */
	public ZStringBuilder testScalarOverlap() {
		return encoder.testScalarOverlap();
	}
	
	/**
	 * Returns a string builder representation the state of StateEncoderObject instances in this encoder.
	 * 
	 * @return The string builder
	 */
	public ZStringBuilder toStringBuilder() {
		return encoder.toStringBuilder();
	}
	
	/**
	 * Initializes the state of StateEncoderObject instances in this encoder using a string builder.
	 * 
	 * @param str The string builder
	 */
	public void fromStringBuilder(ZStringBuilder str) {
		encoder.fromStringBuilder(str);
	}
	
	protected abstract DateTimeSDR encodeRequestValue(int columnIndex,ZGridResult result);
	
	protected DateTimeSDR getNewDateTimeSDR() {
		return new DateTimeSDR(encoder.length());
	}
	
	protected boolean hasInputValue(int columnIndex,ZGridResult result) {
		boolean r = false;
		if (result.getRequest().inputValues.length>columnIndex &&
			result.getRequest().inputValues[columnIndex]!=null
			) {
			r = true;
		}
		return r;
	}
	
	protected long getInputValueAsLong(int columnIndex,ZGridResult result) {
		long r = 0;
		if (hasInputValue(columnIndex,result) &&
			result.getRequest().inputValues[columnIndex] instanceof Long
			) {
			r = (Long) result.getRequest().inputValues[columnIndex];
		}
		return r;
	}
	
	protected float getInputValueAsFloat(int columnIndex,ZGridResult result) {
		float r = 0;
		if (hasInputValue(columnIndex,result)) {
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
	
	protected String getInputLabel(int columnIndex,ZGridResult result) {
		String r = "";
		if (result.getRequest().inputLabels.length>columnIndex &&
			result.getRequest().inputLabels[columnIndex]!=null &&
			result.getRequest().inputLabels[columnIndex].length()>0
			) {
			r = result.getRequest().inputLabels[columnIndex];
		}
		return r;
	}
}
