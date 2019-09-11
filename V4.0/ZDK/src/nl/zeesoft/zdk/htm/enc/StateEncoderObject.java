package nl.zeesoft.zdk.htm.enc;

import nl.zeesoft.zdk.ZStringBuilder;

public abstract class StateEncoderObject extends EncoderObject {
	public StateEncoderObject(int size,int bits) {
		super(size,bits);
	}
	
	public abstract ZStringBuilder toStringBuilder();
	
	public abstract void fromStringBuilder(ZStringBuilder str);
}
