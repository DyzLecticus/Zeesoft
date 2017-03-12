package nl.zeesoft.zjmo.orchestra;

import nl.zeesoft.zdk.ZDate;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zjmo.orchestra.members.WorkClient;

public class OrchestraMember extends Locker {
	private Position	position				= null;
	private int			positionBackupNumber	= 0;
	private String 		ipAddressOrHostName		= "";
	private int			controlPort				= 5433;
	private int			workPort				= 5432;

	private MemberState	state					= null;
	private int			workLoad				= 0;
	private long		memoryUsage				= 0;
	private ZDate		errorDate				= null;
	private String		errorMessage			= "";

	public OrchestraMember() {
		super(null);
	}

	public OrchestraMember(Messenger msgr) {
		super(msgr);
	}

	public String getId() {
		String r = "";
		if (position!=null) {
			r = position.getName() + "/" + positionBackupNumber; 
		}
		return r;
	}
	
	public String getIpAddressOrHostName() {
		return ipAddressOrHostName;
	}

	public void setIpAddressOrHostName(String ipAddressOrHostName) {
		this.ipAddressOrHostName = ipAddressOrHostName;
	}

	public int getControlPort() {
		return controlPort;
	}

	public void setControlPort(int controlPort) {
		this.controlPort = controlPort;
	}

	public int getWorkPort() {
		return workPort;
	}

	public void setWorkPort(int workPort) {
		this.workPort = workPort;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public int getPositionBackupNumber() {
		return positionBackupNumber;
	}

	public void setPositionBackupNumber(int positionBackupNumber) {
		this.positionBackupNumber = positionBackupNumber;
	}

	public MemberState getState() {
		return state;
	}

	public void setState(MemberState state) {
		this.state = state;
	}

	public int getWorkLoad() {
		return workLoad;
	}

	public void setWorkLoad(int workLoad) {
		this.workLoad = workLoad;
	}

	public long getMemoryUsage() {
		return memoryUsage;
	}

	public void setMemoryUsage(long memoryUsage) {
		this.memoryUsage = memoryUsage;
	}

	public ZDate getErrorDate() {
		return errorDate;
	}

	public void setErrorDate(ZDate errorDate) {
		this.errorDate = errorDate;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		if (errorMessage.length()>0) {
			errorDate = new ZDate();
		} else {
			errorDate = null;
		}
		this.errorMessage = errorMessage;
	}
	
	public JsElem toJsonElem(boolean includeState) {
		JsElem mem = new JsElem();
		mem.children.add(new JsElem("positionName",getPosition().getName(),true));
		mem.children.add(new JsElem("positionBackupNumber","" + getPositionBackupNumber()));
		mem.children.add(new JsElem("ipAddressOrHostName",getIpAddressOrHostName(),true));
		mem.children.add(new JsElem("controlPort","" + getControlPort()));
		mem.children.add(new JsElem("workPort","" + getWorkPort()));
		if (includeState) {
			mem.children.add(new JsElem("state",getState().getCode(),true));
			if (!getState().getCode().equals(MemberState.UNKNOWN)) {
				mem.children.add(new JsElem("workLoad","" + getWorkLoad()));
				mem.children.add(new JsElem("memoryUsage","" + getMemoryUsage()));
			}
			if (getErrorDate()!=null && getErrorMessage().length()>0) {
				mem.children.add(new JsElem("errorTime","" + getErrorDate().getDate().getTime()));
				mem.children.add(new JsElem("errorMessage",getErrorMessage(),true));
			}
		}
		return mem;
	}
	
	public MemberClient getNewControlClient(Messenger messenger) {
		return new MemberClient(messenger,getIpAddressOrHostName(),getControlPort());
	}

	public WorkClient getNewWorkClient(Messenger messenger) {
		return new WorkClient(messenger,getId(),getIpAddressOrHostName(),getWorkPort());
	}
}
