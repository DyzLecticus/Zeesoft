package nl.zeesoft.zjmo.orchestra.protocol;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zjmo.orchestra.MemberObject;
import nl.zeesoft.zjmo.orchestra.Orchestra;
import nl.zeesoft.zjmo.orchestra.ProtocolObject;

public class ProtocolWork extends ProtocolObject {
	public static final String UPDATE_ORCHESTRA = "UPDATE_ORCHESTRA";
	
	@Override
	protected ZStringBuilder handleInput(MemberObject member,ZStringBuilder input) {
		ZStringBuilder output = super.handleInput(member, input);
		if (output==null) {
			JsFile json = getJsonForInput(input);
			if (json.rootElement!=null) {
				output = handleJson(member,json);
			}
			if (output==null) {
				output = input;
			}
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
	
	protected ZStringBuilder handleJson(MemberObject member,JsFile json) {
		ZStringBuilder output = null;
		JsElem type = json.rootElement.getChildByName("requestType");
		if (type!=null && type.value!=null && type.value.length()>0) {
			if (type.value.toString().equals(UPDATE_ORCHESTRA)) {
				Orchestra orch = member.getOrchestra().getCopy(false);
				orch.fromJson(json);
				member.updateOrchestra(orch);
			}
		} else {
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
		return output;
	}
}
