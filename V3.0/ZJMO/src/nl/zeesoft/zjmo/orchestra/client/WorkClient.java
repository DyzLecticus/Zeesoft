package nl.zeesoft.zjmo.orchestra.client;

import java.util.Date;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zjmo.orchestra.MemberClient;
import nl.zeesoft.zjmo.orchestra.ProtocolObject;
import nl.zeesoft.zjmo.orchestra.protocol.ProtocolWork;
import nl.zeesoft.zjmo.orchestra.protocol.RequestObject;
import nl.zeesoft.zjmo.orchestra.protocol.WorkRequest;

/**
 * MemberClient extension for WorkRequest and PublishRequest handling
 */
public class WorkClient extends MemberClient {
	private String	memberId	= "";
	private Object	inUseBy		= null;
	private Date	lastUsed	= new Date();

	public WorkClient(String ipAddressOrHostName, int port,int timeout) {
		super(null,null,ipAddressOrHostName,port);
		setTimeout(timeout);
	}
	
	public WorkClient(Messenger msgr,WorkerUnion union,String memberId, String ipAddressOrHostName, int port,int timeout) {
		super(msgr,union,ipAddressOrHostName,port);
		this.memberId = memberId;
		setTimeout(timeout);
	}

	public String getMemberId() {
		return memberId;
	}
	
	public ZStringBuilder sendRequest(RequestObject r) {
		return sendRequest(r,getTimeout());
	}

	public ZStringBuilder sendRequest(RequestObject r,int timeout) {
		return writeOutputReadInput(r.toJson().toStringBuilder(),timeout);
	}

	public ZStringBuilder sendWorkRequestRequest(WorkRequest wr) {
		return sendWorkRequestRequest(wr,getTimeout());
	}

	public ZStringBuilder sendWorkRequestRequest(WorkRequest wr,int timeout) {
		return writeOutputReadInput(wr.getRequest().toStringBuilder(),timeout);
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
