package nl.zeesoft.zdk.htm.enc;

import nl.zeesoft.zdk.ZStringBuilder;

public abstract class StateEncoderObject extends EncoderObject {
	public StateEncoderObject(int length,int bits) {
		super(length,bits);
	}
	
	public abstract ZStringBuilder toStringBuilder();
	
	public abstract void fromStringBuilder(ZStringBuilder str);
}
