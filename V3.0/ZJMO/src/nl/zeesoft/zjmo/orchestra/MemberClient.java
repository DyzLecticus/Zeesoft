package nl.zeesoft.zjmo.orchestra;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;

public class MemberClient extends Locker {
	private Protocol			protocol			= null;
	private String				ipAddressOrHostName	= "localhost";
	private int					port				= 5432;
	private SocketHandler		socket				= null;
	private boolean				open				= false;

	public MemberClient(String ipAddressOrHostName, int port) {
		super(null);
		this.ipAddressOrHostName = ipAddressOrHostName;
		this.port = port;
		this.protocol = getNewProtocol();
	}

	public MemberClient(Messenger msgr,String ipAddressOrHostName, int port) {
		super(msgr);
		this.ipAddressOrHostName = ipAddressOrHostName;
		this.port = port;
		this.protocol = getNewProtocol();
	}

	public boolean open() {
		lockMe(this);
		initializeConnectionNoLock();
		unlockMe(this);
		return open;
	}
	
	public boolean isOpen() {
		boolean r = false;
		lockMe(this);
		r = open;
		unlockMe(this);
		return r;
	}
	
	public void close() {
		lockMe(this);
		socket.close();
		open = false;
		unlockMe(this);
	}
	
	public void writeOutput(ZStringBuilder output) {
		lockMe(this);
		writeOutputNoLock(output);
		unlockMe(this);
	}

	public ZStringBuilder writeOutputReadInput(ZStringBuilder output) {
		ZStringBuilder input = null;
		lockMe(this);
		writeOutputNoLock(output);
		input = readInputNoLock();
		unlockMe(this);
		return input;
	}

	public ZStringBuilder readInput() {
		ZStringBuilder input = null;
		lockMe(this);
		input = readInputNoLock();
		unlockMe(this);
		return input;
	}
	
	public ZStringBuilder sendCommand(String command) {
		return writeOutputReadInput(protocol.getCommandJson(command));
	}

	protected Protocol getNewProtocol() {
		return new Protocol();
	}
	
	private void writeOutputNoLock(ZStringBuilder output) {
		initializeConnectionNoLock();
		socket.writeOutput(output);
	}

	private ZStringBuilder readInputNoLock() {
		initializeConnectionNoLock();
		return socket.readInput();
	}

	private void initializeConnectionNoLock() {
		if (!open) {
			initializeSocketHandlerNoLock();
			open = socket.open();
		}
	}
	
	private void initializeSocketHandlerNoLock() {
		if (socket==null) {
			socket = new SocketHandler();
			socket.open(ipAddressOrHostName,port,false);
		}
	}
}
