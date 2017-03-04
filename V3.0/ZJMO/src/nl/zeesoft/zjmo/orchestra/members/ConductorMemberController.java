package nl.zeesoft.zjmo.orchestra.members;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zjmo.json.JsElem;
import nl.zeesoft.zjmo.json.JsFile;
import nl.zeesoft.zjmo.orchestra.MemberClient;
import nl.zeesoft.zjmo.orchestra.MemberState;
import nl.zeesoft.zjmo.orchestra.Orchestra;
import nl.zeesoft.zjmo.orchestra.OrchestraMember;
import nl.zeesoft.zjmo.orchestra.ProtocolControl;

public class ConductorMemberController extends Locker {
	private Orchestra 			orchestra	= null;
	private List<MemberClient>	clients		= new ArrayList<MemberClient>();

	protected ConductorMemberController(Orchestra orchestra) {
		super(null);
		this.orchestra = orchestra;
	}

	protected ConductorMemberController(Messenger msgr,Orchestra orchestra) {
		super(msgr);
		this.orchestra = orchestra;
	}

	protected void open() {
		lockMe(this);
		for (MemberClient client: clients) {
			client.close();
		}
		clients.clear();
		for (OrchestraMember member: orchestra.getMembers()) {
			MemberClient client = new MemberClient(member.getIpAddressOrHostName(),member.getControlPort());
			clients.add(client);
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
				setPlayerOffLine(mem.getPosition().getName(),mem.getPositionBackupNumber());
			}
		}
	}

	protected void setPlayerOffLine(String positionName,int positionBackupNumber) {
		sendMemberCommand(positionName,positionBackupNumber,ProtocolControl.TAKE_OFFLINE);
	}

	protected void drainPlayerOffLine(String positionName,int positionBackupNumber) {
		sendMemberCommand(positionName,positionBackupNumber,ProtocolControl.DRAIN_OFFLINE);
	}

	protected void getState(String memberId) {
		lockMe(this);
		for (MemberClient client: clients) {
			OrchestraMember member = getMemberForClient(client);
			if (memberId==null || memberId.length()==0 || member.getId().equals(memberId)) {
				
				if (!client.isOpen()) {
					client.open();
					if (client.isOpen()) {
						MemberClient stateClient = new MemberClient(member.getIpAddressOrHostName(),member.getControlPort());
						ConductorMemberStateWorker stateWorker = new ConductorMemberStateWorker(this,stateClient,member.getId());
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
	
	private void sendMemberCommand(String positionName,int positionBackupNumber,String command) {
		OrchestraMember member = orchestra.getMemberForPosition(positionName, positionBackupNumber);
		if (member!=null) {
			MemberClient client = getClientForMember(member);
			if (client.isOpen()) {
				client.sendCommand(command);
			}
		}
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
