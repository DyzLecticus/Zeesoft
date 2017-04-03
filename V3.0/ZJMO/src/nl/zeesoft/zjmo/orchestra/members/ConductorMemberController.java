package nl.zeesoft.zjmo.orchestra.members;

import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zjmo.orchestra.MemberState;
import nl.zeesoft.zjmo.orchestra.Orchestra;
import nl.zeesoft.zjmo.orchestra.OrchestraMember;
import nl.zeesoft.zjmo.orchestra.client.ActiveClient;
import nl.zeesoft.zjmo.orchestra.protocol.ProtocolControl;

public class ConductorMemberController extends Locker {
	private Orchestra 							orchestra				= null;
	private	ConductorMemberControllerWorker		worker					= null;
	private ConductorMemberConnector			connector				= null;
	private WorkClientPool						workClientPool			= null;
	private WorkClientPoolWorker				workClientPoolWorker	= null;

	protected ConductorMemberController(Messenger msgr,WorkerUnion uni,Orchestra orchestra) {
		super(msgr);
		this.orchestra = orchestra;
		worker = new ConductorMemberControllerWorker(msgr,uni,this);
		connector = new ConductorMemberConnector(msgr,uni,true,this);
		connector.initialize(orchestra,null);
		workClientPool = new WorkClientPool(msgr,uni,orchestra);
		workClientPoolWorker = new WorkClientPoolWorker(msgr,uni,workClientPool,orchestra.closeUnusedWorkClientsMilliseconds());
	}

	protected void open() {
		close();
		connector.open();
		workClientPoolWorker.start();
		worker.start();
	}

	protected void close() {
		worker.stop();
		connector.close();
		workClientPoolWorker.stop();
		workClientPool.closeAllClients();
	}

	protected JsFile getOrchestraState() {
		lockMe(this);
		JsFile f = orchestra.toJson(true);
		unlockMe(this);
		return f;
	}

	protected JsFile getMemberState(String id) {
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
	
	protected void getState(String memberId,boolean connect) {
		for (OrchestraMember member: orchestra.getMembers()) {
			if (memberId==null || memberId.length()==0 || member.getId().equals(memberId)) {
				ActiveClient ac = connector.getClient(member.getId());
				if (!ac.isOpen()) {
					if (connect) {
						ac.connectClient();
						if (!ac.isOpen()) {
							setMemberStateUnknown(member,"Failed to connect");
						}
					} else {
						lockMe(this);
						setMemberStateUnknown(member,"Not connected");
						unlockMe(this);
					}
				}
				
				if (ac.isOpen()) {
					ZStringBuilder state = ac.getClient().sendCommand(ProtocolControl.GET_STATE);
					lockMe(this);
					member.setErrorMessage("");
					if (state==null) {
						setMemberStateUnknown(member,"Failed to get member state");
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
					unlockMe(this);
				}
			}
		}
	}

	protected boolean workRequestTimedOut(WorkClient client) {
		boolean drain = false;
		lockMe(this);
		for (OrchestraMember mem: orchestra.getMembers()) {
			if (mem.getId().equals(client.getMemberId()) && mem.getState()!=null && !mem.getState().getCode().equals(MemberState.UNKNOWN)) {
				setMemberStateUnknown(mem,"Work request timed out");
				drain = mem.isWorkRequestTimeoutDrain();
				break;
			}
		}
		unlockMe(this);
		return drain;
	}
	
	protected void setStateUnknown(String memberId,String errorMessage) {
		lockMe(this);
		setMemberStateUnknown(orchestra.getMemberById(memberId),errorMessage);
		unlockMe(this);
	}

	protected WorkClient getClient(Object source,String positionName) {
		WorkClient r = null;
		lockMe(source);
		List<OrchestraMember> mems = orchestra.getMembersForPosition(positionName);
		for (OrchestraMember mem: mems) {
			if (mem.getState()!=null && mem.getState().getCode().equals(MemberState.ONLINE)) {
				r = workClientPool.getClient(source,mem.getId());
				if (r!=null) {
					break;
				}
			}
		}
		unlockMe(source);
		if (r==null) {
			getMessenger().error(this,"No players online for: " + positionName + " (members: " + mems.size() + ")");
		}
		return r;
	}

	protected void returnClient(WorkClient client) {
		workClientPool.returnClient(client);
	}
	
	protected void closeUnusedClients(String memberId,long unusedMs) {
		workClientPool.closeUnusedClients(memberId, unusedMs);
	}
	
	private void setMemberStateUnknown(OrchestraMember member,String errorMessage) {
		member.setErrorMessage(errorMessage);
		member.setState(MemberState.getState(MemberState.UNKNOWN));
		member.setWorkLoad(0);
		member.setMemoryUsage(0);
	}
	
	private ZStringBuilder sendMemberCommand(String id,String command) {
		ZStringBuilder response = null;
		ActiveClient ac = connector.getClient(id);
		if (ac.isOpen()) {
			response = ac.getClient().sendCommand(command);
		}
		return response;
	}
}
