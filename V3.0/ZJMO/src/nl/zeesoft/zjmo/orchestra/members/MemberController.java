package nl.zeesoft.zjmo.orchestra.members;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zjmo.json.JsElem;
import nl.zeesoft.zjmo.json.JsFile;
import nl.zeesoft.zjmo.orchestra.MemberClient;
import nl.zeesoft.zjmo.orchestra.MemberState;
import nl.zeesoft.zjmo.orchestra.Orchestra;
import nl.zeesoft.zjmo.orchestra.OrchestraMember;
import nl.zeesoft.zjmo.orchestra.Protocol;

public class MemberController {
	private Orchestra 			orchestra	= null;
	private List<MemberClient>	clients		= new ArrayList<MemberClient>();

	protected MemberController(Orchestra orchestra) {
		this.orchestra = orchestra;
	}

	protected MemberController(Messenger msgr, WorkerUnion union, Orchestra orchestra) {
		this.orchestra = orchestra;
	}

	protected void initialize() {
		close();
		clients.clear();
		for (OrchestraMember member: orchestra.getMembers()) {
			MemberClient client = new MemberClient(member.getIpAddressOrHostName(),member.getControlPort());
			clients.add(client);
		}
		open();
		getState();
	}

	protected void open() {
		for (MemberClient client: clients) {
			client.open();
		}
	}

	protected void close() {
		for (MemberClient client: clients) {
			client.close();
		}
	}

	protected void getState() {
		int i = 0;
		for (MemberClient client: clients) {
			OrchestraMember member = orchestra.getMembers().get(i);
			i++;
			if (!client.isOpen()) {
				client.open();
			}
			if (!client.isOpen()) {
				member.setErrorMessage("Unable to open socket");
				member.setState(MemberState.getState(MemberState.UNKNOWN));
				member.setWorkLoad(0);
				member.setMemoryUsage(0);
			} else {
				member.setErrorMessage("");
				ZStringBuilder state = client.sendCommand(Protocol.GET_STATE);
				JsFile json = new JsFile();
				json.fromStringBuilder(state);
				for (JsElem elem: json.rootElement.children) {
					if (elem.name.equals("state")) {
						member.setState(MemberState.getState(elem.value.toString()));
					} else if (elem.name.equals("workLoad")) {
						member.setWorkLoad(Integer.parseInt(elem.value.toString()));
					} else if (elem.name.equals("memoryUsage")) {
						member.setMemoryUsage(Long.parseLong(elem.value.toString()));
					}
				}
			}
		}
	}
}
