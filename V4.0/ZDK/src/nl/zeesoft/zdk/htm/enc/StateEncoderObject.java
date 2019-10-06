package nl.zeesoft.zdk.htm.enc;

import nl.zeesoft.zdk.ZStringBuilder;

/**
 * Abstract encoder object with state information.
 */
public abstract class StateEncoderObject extends EncoderObject {
	public StateEncoderObject(int length,int bits) {
		super(length,bits);
	}

	/**
	 * Returns a string builder representation of the state of this encoder.
	 * 
	 * @return The string builder
	 */
	public abstract ZStringBuilder toStringBuilder();
	
	/**
	 * Initializes the state of this encoder from a string builder representation.
	 * 
	 * @param str The string builder
	 */
	public abstract void fromStringBuilder(ZStringBuilder str);
}
