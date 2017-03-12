package nl.zeesoft.zjmo.orchestra.protocol;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zjmo.orchestra.MemberObject;
import nl.zeesoft.zjmo.orchestra.members.Conductor;
import nl.zeesoft.zjmo.orchestra.members.WorkClient;

public class ProtocolWorkConductor extends ProtocolWork {
	@Override
	protected ZStringBuilder handleInput(MemberObject member,ZStringBuilder input) {
		ZStringBuilder output = null;
		if (isCommandJson(input)) {
			output = super.handleInput(member, input);
		} else if (member instanceof Conductor) {
			//System.out.println(this + ": Handle work: " + input);
			Conductor con = (Conductor) member;
			input.trim();
			if (input.startsWith("{")) {
				WorkRequest wr = new WorkRequest();
				wr.fromStringBuilder(input);
				WorkClient client = null;
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
				if (output==null) {
					boolean retry = true;
					while (retry) {
						client = con.getClient(this,wr.getPositionName());
						if (client==null) {
							wr.setError("No players online for position: " + wr.getPositionName());
							output = wr.toJson().toStringBuilder();
							retry = false;
						} else {
							wr.setResponse(null);
							//System.out.println(this + ": Sending request: " + wr.getRequest().toStringBuilder());
							ZStringBuilder response = client.writeOutputReadInput(wr.getRequest().toStringBuilder());
							con.returnClient(client);
							if (client.isOpen()) {
								JsFile resp = new JsFile();
								resp.fromStringBuilder(response);
								if (resp.rootElement==null) {
									wr.setError("Player did not return valid JSON: " + client.getMemberId());
								} else {
									wr.setResponse(resp);
								}
								output = wr.toJson().toStringBuilder();
								retry = false;
							}
						}
					}
				}
			} else {
				output = getErrorJson("Unrecognized input");
			}
		}
		return output;
	}
}
