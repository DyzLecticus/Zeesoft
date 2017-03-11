package nl.zeesoft.zjmo.orchestra.protocol;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zjmo.json.JsElem;
import nl.zeesoft.zjmo.json.JsFile;
import nl.zeesoft.zjmo.orchestra.MemberObject;
import nl.zeesoft.zjmo.orchestra.ProtocolObject;

public class ProtocolWork extends ProtocolObject {
	@Override
	protected ZStringBuilder handleInput(MemberObject member,ZStringBuilder input) {
		ZStringBuilder output = super.handleInput(member, input);
		if (output==null) {
			JsFile json = getJsonForInput(input);
			if (json.rootElement!=null) {
				JsElem sleep = json.rootElement.getChildByName("sleep");
				if (sleep!=null && sleep.value!=null && sleep.value.length()>0) {
					int slp = 0;
					try {
						slp = Integer.parseInt(sleep.value.toString());
					} catch (NumberFormatException e) {
						output = getErrorJson("The sleep parameter requires a valid integer value");
					}
					if (slp>0) {
						try {
							Thread.sleep(slp);
						} catch (InterruptedException e) {
							// Ignore
						}
					}
				}
			}
			//System.out.println(this + ": Handle work: " + input);
			output = input;
		}
		return output;
	}
	
	protected JsFile getJsonForInput(ZStringBuilder input) {
		JsFile json = new JsFile();
		if (input.trim().length()>0 && input.startsWith("{") && input.endsWith("}")) {
			json.fromStringBuilder(input);
		}
		return json;
	}
}
