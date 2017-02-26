package nl.zeesoft.zjmo.orchestra;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;

public class OrchestraMember extends Locker {
	private Position	position				= null;
	private int			positionBackupNumber	= 0;
	private String 		ipAddressOrHostName		= "";
	private int			controlPort				= 5433;
	private int			workPort				= 5432;

	public OrchestraMember() {
		super(null);
	}

	public OrchestraMember(Messenger msgr) {
		super(msgr);
	}

	private MemberState	state					= null;

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
}
