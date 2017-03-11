package nl.zeesoft.zjmo.orchestra.protocol;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zjmo.orchestra.MemberObject;
import nl.zeesoft.zjmo.orchestra.ProtocolObject;

public class ProtocolWork extends ProtocolObject {
	@Override
	protected ZStringBuilder handleInput(MemberObject member,ZStringBuilder input) {
		ZStringBuilder output = super.handleInput(member, input);
		if (output==null) {
			//System.out.println(this + ": Handle work: " + input);
			output = input;
		}
		return output;
	}
}
