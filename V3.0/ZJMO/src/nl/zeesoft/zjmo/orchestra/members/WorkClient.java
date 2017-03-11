package nl.zeesoft.zjmo.orchestra.members;

import java.util.Date;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zjmo.orchestra.MemberClient;
import nl.zeesoft.zjmo.orchestra.ProtocolObject;
import nl.zeesoft.zjmo.orchestra.protocol.ProtocolWork;

public class WorkClient extends MemberClient {
	private String	memberId	= "";
	private Object	inUseBy		= null;
	private Date	lastUsed	= new Date();

	public WorkClient(String memberId,String ipAddressOrHostName, int port) {
		super(null, ipAddressOrHostName, port);
		this.memberId = memberId;
	}

	public WorkClient(Messenger msgr,String memberId, String ipAddressOrHostName, int port) {
		super(msgr, ipAddressOrHostName, port);
		this.memberId = memberId;
	}

	public String getMemberId() {
		return memberId;
	}

	protected void unSetInUseBy() {
		lockMe(this);
		this.inUseBy = null;
		lastUsed = new Date();
		unlockMe(this);
	}

	protected boolean getSetInUseBy(Object inUseBy) {
		boolean r = false;
		lockMe(this);
		if (this.inUseBy==null) {
			this.inUseBy = inUseBy;
			r = true;
		}
		unlockMe(this);
		return r;
	}

	protected boolean isInUse() {
		boolean r = false;
		lockMe(this);
		r = (inUseBy!=null);
		unlockMe(this);
		return r;
	}
	
	protected Date getLastUsed() {
		Date r = null;
		lockMe(this);
		r = lastUsed;
		unlockMe(this);
		return r;
	}

	protected void setLastUsed(Date lastUsed) {
		lockMe(this);
		this.lastUsed = lastUsed;
		unlockMe(this);
	}
	
	@Override
	protected ProtocolObject getNewProtocol() {
		return new ProtocolWork();
	}
}
