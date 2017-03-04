package nl.zeesoft.zjmo.orchestra;

import nl.zeesoft.zdk.ZDate;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;

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
}
