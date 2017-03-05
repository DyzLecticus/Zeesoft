package nl.zeesoft.zjmo.orchestra;

import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;

public class MemberClient extends Locker {
	private ProtocolControl		protocol			= null;
	private String				ipAddressOrHostName	= "localhost";
	private int					port				= 5432;
	private SocketHandler		socket				= null;
	private boolean				open				= false;

	public MemberClient(String ipAddressOrHostName, int port) {
		super(null);
		this.ipAddressOrHostName = ipAddressOrHostName;
		this.port = port;
		this.protocol = getNewControlProtocol();
	}

	public MemberClient(Messenger msgr,String ipAddressOrHostName, int port) {
		super(msgr);
		this.ipAddressOrHostName = ipAddressOrHostName;
		this.port = port;
		this.protocol = getNewControlProtocol();
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
		r = isOpenNoLock();
		unlockMe(this);
		return r;
	}
	
	public void close() {
		lockMe(this);
		if (socket!=null) {
			socket.close();
		}
		open = false;
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
		return writeOutputReadInput(protocol.getCommandJson(command,null));
	}

	public ZStringBuilder sendCommand(String command, String pName, String pValue) {
		SortedMap<String,String> params = new TreeMap<String,String>();
		params.put(pName,pValue);
		return writeOutputReadInput(protocol.getCommandJson(command,params));
	}

	public ZStringBuilder sendCommand(String command, String pName1, String pValue1, String pName2, String pValue2) {
		SortedMap<String,String> params = new TreeMap<String,String>();
		params.put(pName1,pValue1);
		params.put(pName2,pValue2);
		return writeOutputReadInput(protocol.getCommandJson(command,params));
	}

	public ZStringBuilder sendCommand(String command,SortedMap<String,String> parameters) {
		return writeOutputReadInput(protocol.getCommandJson(command,parameters));
	}

	protected ProtocolControl getNewControlProtocol() {
		return new ProtocolControl();
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
		if (!isOpenNoLock()) {
			if (socket==null) {
				socket = new SocketHandler();
				//System.out.println("Initializing client socket to: " + ipAddressOrHostName + ":" + port);
				open = socket.open(ipAddressOrHostName,port,false);
			} else {
				//System.out.println("Opening client socket to: " + ipAddressOrHostName + ":" + port);
				open = socket.open(ipAddressOrHostName,port,false);
			}
		}
	}
	
	private boolean isOpenNoLock() {
		if (socket!=null && open) {
			open = socket.isOpen();
		} else if (socket==null) {
			open = false;
		}
		return open;
	}
}
