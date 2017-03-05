package nl.zeesoft.zjmo.orchestra.protocol;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zjmo.json.JsFile;
import nl.zeesoft.zjmo.orchestra.MemberObject;

public class ProtocolWorkConductor extends ProtocolWork {
	@Override
	protected ZStringBuilder handleInput(MemberObject member,ZStringBuilder input) {
		ZStringBuilder output = null;
		input.trim();
		if (input.startsWith("{")) {
			JsFile json = new JsFile();
			json.fromStringBuilder(input);
			WorkRequest wr = new WorkRequest();
			wr.fromJson(json);
			if (wr.getPositionName().length()==0) {
				wr.setError("Work request requires a position name");
				output = wr.toJson().toStringBuilder();
			} else if (wr.getRequest()==null) {
				wr.setError("Work request is empty");
				output = wr.toJson().toStringBuilder();
			}
			if (output==null && member.getOrchestra().getPosition(wr.getPositionName())==null) {
				wr.setError("Work request position does not exist: " + wr.getPositionName());
				output = wr.toJson().toStringBuilder();
			}
			// TODO: Get online position member
			if (output==null) {
				// TODO: Process request
			}
		} else {
			output = getErrorJson("Unrecognized input");
		}
		return output;
	}
}
