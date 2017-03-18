package nl.zeesoft.zso.orchestra.members;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zjmo.orchestra.MemberObject;
import nl.zeesoft.zjmo.orchestra.protocol.ProtocolWork;

public class SamplePlayerProtocol extends ProtocolWork {

	@Override
	protected ZStringBuilder handleJson(MemberObject member,JsFile json) {
		ZStringBuilder output = null;
		if (member instanceof SamplePlayer) {
			SamplePlayer player = (SamplePlayer) member;
			boolean play = false;
			long startMs = 0;
			long durationMs = 0;
			for (JsElem cElem: json.rootElement.children) {
				if (cElem.name.equals("startMs")) {
					startMs = Long.parseLong(cElem.value.toString());
					play = true;
				} else if (cElem.name.equals("durationMs")) {
					durationMs = Long.parseLong(cElem.value.toString());
					play = true;
				}
			}
			if (play) {
				player.playClip(startMs, durationMs);
			}
		}
		return output;
	}
	
}
