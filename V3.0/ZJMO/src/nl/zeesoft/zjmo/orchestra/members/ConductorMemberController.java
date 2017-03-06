package nl.zeesoft.zjmo.orchestra.members;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zjmo.json.JsElem;
import nl.zeesoft.zjmo.json.JsFile;
import nl.zeesoft.zjmo.orchestra.MemberClient;
import nl.zeesoft.zjmo.orchestra.MemberState;
import nl.zeesoft.zjmo.orchestra.Orchestra;
import nl.zeesoft.zjmo.orchestra.OrchestraMember;
import nl.zeesoft.zjmo.orchestra.protocol.ProtocolControl;

public class ConductorMemberController extends Locker {
	private WorkerUnion			union		= null;
	private Orchestra 			orchestra	= null;
	private List<MemberClient>	clients		= new ArrayList<MemberClient>();

	protected ConductorMemberController(Messenger msgr,WorkerUnion uni,Orchestra orchestra) {
		super(msgr);
		this.orchestra = orchestra;
		this.union = uni;
	}

	protected void open() {
		lockMe(this);
		for (MemberClient client: clients) {
			client.close();
		}
		clients.clear();
		for (OrchestraMember member: orchestra.getMembers()) {
			clients.add(member.getNewControlClient());
		}
		unlockMe(this);
	}

	protected void close() {
		lockMe(this);
		for (MemberClient client: clients) {
			client.close();
		}
		unlockMe(this);
	}

	protected void setPlayersOffLine() {
		for (OrchestraMember mem: orchestra.getMembers()) {
			if (!mem.getPosition().equals(Orchestra.CONDUCTOR)) {
				takeOffline(mem.getId());
			}
		}
	}

	public JsFile getOrchestraState() {
		lockMe(this);
		JsFile f = orchestra.toJson(true);
		unlockMe(this);
		return f;
	}

	public JsFile getMemberState(String id) {
		lockMe(this);
		JsFile f = new JsFile();
		f.rootElement = orchestra.getMemberById(id).toJsonElem(true);
		unlockMe(this);
		return f;
	}

	protected int getMemberWorkLoad(String id) {
		int r = 0;
		lockMe(this);
		r = orchestra.getMemberById(id).getWorkLoad();
		unlockMe(this);
		return r;
	}
	
	protected ZStringBuilder takeOffline(String id) {
		return sendMemberCommand(id,ProtocolControl.TAKE_OFFLINE);
	}

	protected ZStringBuilder drainOffline(String id) {
		return sendMemberCommand(id,ProtocolControl.DRAIN_OFFLINE);
	}

	protected ZStringBuilder bringOnline(String id) {
		return sendMemberCommand(id,ProtocolControl.BRING_ONLINE);
	}
	
	protected void getState(String memberId) {
		lockMe(this);
		for (MemberClient client: clients) {
			OrchestraMember member = getMemberForClient(client);
			if (memberId==null || memberId.length()==0 || member.getId().equals(memberId)) {
				
				if (!client.isOpen()) {
					client.open();
					if (client.isOpen()) {
						MemberClient stateClient = member.getNewControlClient();
						ConductorMemberStateWorker stateWorker = new ConductorMemberStateWorker(getMessenger(),union,this,stateClient,member.getId());
						stateWorker.start();
					}
				}
				
				if (!client.isOpen()) {
					setMemberStateUnknown(member,"Failed to open control client");
				} else {
					member.setErrorMessage("");
					ZStringBuilder state = client.sendCommand(ProtocolControl.GET_STATE);
					if (state==null) {
						setMemberStateUnknown(member,"Control client has been closed");
					} else {
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
		unlockMe(this);
	}

	protected void setStateUnknown(String memberId,String errorMessage) {
		lockMe(this);
		for (OrchestraMember mem: orchestra.getMembers()) {
			if (mem.getId().equals(memberId)) {
				MemberClient client = getClientForMember(mem);
				client.close();
				setMemberStateUnknown(mem,errorMessage);
				break;
			}
		}
		unlockMe(this);
	}

	private void setMemberStateUnknown(OrchestraMember member,String errorMessage) {
		member.setErrorMessage(errorMessage);
		member.setState(MemberState.getState(MemberState.UNKNOWN));
		member.setWorkLoad(0);
		member.setMemoryUsage(0);
	}
	
	private ZStringBuilder sendMemberCommand(String id,String command) {
		ZStringBuilder response = null;
		OrchestraMember member = orchestra.getMemberById(id);
		if (member!=null) {
			MemberClient client = getClientForMember(member);
			if (client.isOpen()) {
				response = client.sendCommand(command);
			}
		}
		return response;
	}
	
	private OrchestraMember getMemberForClient(MemberClient client) {
		OrchestraMember r = null;
		int i = 0;
		for (MemberClient cl: clients) {
			OrchestraMember member = orchestra.getMembers().get(i);
			i++;
			if (cl==client) {
				r = member;
				break;
			}
		}
		return r;
	}

	private MemberClient getClientForMember(OrchestraMember member) {
		MemberClient r = null;
		int i = 0;
		for (OrchestraMember mem: orchestra.getMembers()) {
			MemberClient client = clients.get(i);
			i++;
			if (mem==member) {
				r = client;
				break;
			}
		}
		return r;
	}
}
