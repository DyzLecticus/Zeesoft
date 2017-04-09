package nl.zeesoft.zjmo.orchestra;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZDate;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zjmo.orchestra.members.WorkClient;

/**
 * Orchestra member data object.
 */
public class OrchestraMember extends Locker {
	private Position		position				= null;
	private int				positionBackupNumber	= 0;
	private String 			ipAddressOrHostName		= "";
	private int				controlPort				= 5433;
	private int				workPort				= 5432;
	private int				workRequestTimeout		= 500;
	private boolean			workRequestTimeoutDrain	= false;
	private List<Channel>	channels				= new ArrayList<Channel>();

	private MemberState		state					= null;
	private int				workLoad				= 0;
	private long			memoryUsage				= 0;
	private ZDate			errorDate				= null;
	private String			errorMessage			= "";
	private boolean			restartRequired			= false;

	public OrchestraMember() {
		super(null);
	}

	public OrchestraMember(Messenger msgr) {
		super(msgr);
	}
	
	public OrchestraMember getCopy() {
		OrchestraMember copy = new OrchestraMember();
		copy.setPosition(getPosition());
		copy.setPositionBackupNumber(getPositionBackupNumber());
		copy.setIpAddressOrHostName(getIpAddressOrHostName());
		copy.setControlPort(getControlPort());
		copy.setWorkPort(getWorkPort());
		copy.setWorkRequestTimeout(getWorkRequestTimeout());
		copy.setWorkRequestTimeoutDrain(isWorkRequestTimeoutDrain());
		for (Channel chan: channels) {
			copy.getChannels().add(chan);
		}
		copy.setState(getState());
		copy.setWorkLoad(getWorkLoad());
		copy.setMemoryUsage(getMemoryUsage());
		copy.setErrorDate(getErrorDate());
		copy.setErrorMessage(getErrorMessage());
		copy.setRestartRequired(isRestartRequired());
		return copy;
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

	public int getWorkRequestTimeout() {
		return workRequestTimeout;
	}

	public void setWorkRequestTimeout(int workRequestTimeout) {
		this.workRequestTimeout = workRequestTimeout;
	}

	public boolean isWorkRequestTimeoutDrain() {
		return workRequestTimeoutDrain;
	}

	public void setWorkRequestTimeoutDrain(boolean workRequestTimeoutDrain) {
		this.workRequestTimeoutDrain = workRequestTimeoutDrain;
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

	public List<Channel> getChannels() {
		return channels;
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

	public boolean isRestartRequired() {
		return restartRequired;
	}

	public void setRestartRequired(boolean restartRequired) {
		this.restartRequired = restartRequired;
	}
	
	public JsElem toJsonElem(boolean includeState) {
		JsElem mem = new JsElem();
		mem.children.add(new JsElem("positionName",getPosition().getName(),true));
		mem.children.add(new JsElem("positionBackupNumber","" + getPositionBackupNumber()));
		mem.children.add(new JsElem("ipAddressOrHostName",getIpAddressOrHostName(),true));
		mem.children.add(new JsElem("controlPort","" + getControlPort()));
		mem.children.add(new JsElem("workPort","" + getWorkPort()));
		mem.children.add(new JsElem("workRequestTimeout","" + getWorkRequestTimeout()));
		mem.children.add(new JsElem("workRequestTimeoutDrain","" + isWorkRequestTimeoutDrain()));
		if (channels.size()>0) {
			JsElem chans = new JsElem("channels");
			int i = 0;
			for (Channel chan: channels) {
				chans.children.add(new JsElem("" + i,chan.getName(),true));
				i++;
			}
			mem.children.add(chans);
		}
		if (includeState) {
			if (getState()==null || getState().getCode().equals(MemberState.UNKNOWN)) {
				mem.children.add(new JsElem("state",MemberState.UNKNOWN,true));
			} else {
				mem.children.add(new JsElem("state",getState().getCode(),true));
				mem.children.add(new JsElem("workLoad","" + getWorkLoad()));
				mem.children.add(new JsElem("memoryUsage","" + getMemoryUsage()));
			}
			if (getErrorDate()!=null && getErrorMessage().length()>0) {
				mem.children.add(new JsElem("errorTime","" + getErrorDate().getDate().getTime()));
				mem.children.add(new JsElem("errorMessage",getErrorMessage(),true));
			}
			if (isRestartRequired()) {
				mem.children.add(new JsElem("restartRequired","" + isRestartRequired()));
			}
		}
		return mem;
	}
	
	public MemberClient getNewControlClient(Messenger messenger,WorkerUnion union) {
		return new MemberClient(messenger,union,getIpAddressOrHostName(),getControlPort());
	}

	public WorkClient getNewWorkClient(Messenger messenger,WorkerUnion union) {
		return new WorkClient(messenger,union,getId(),getIpAddressOrHostName(),getWorkPort(),workRequestTimeout);
	}
}
