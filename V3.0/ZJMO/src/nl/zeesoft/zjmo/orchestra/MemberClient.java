package nl.zeesoft.zjmo.orchestra;

import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zjmo.orchestra.protocol.ProtocolControl;

/**
 * Used to connect to orchestra MemberObject instances.
 */
public class MemberClient extends Locker {
	private WorkerUnion			union				= null;
	private ProtocolObject		protocol			= null;
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

	public MemberClient(Messenger msgr,WorkerUnion union,String ipAddressOrHostName, int port) {
		super(msgr);
		this.union = union;
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

	public ZStringBuilder writeOutputReadInput(ZStringBuilder output,int timeout) {
		ZStringBuilder input = null;
		lockMe(this);
		writeOutputNoLock(output);
		input = readInputNoLock(timeout);
		unlockMe(this);
		return input;
	}

	public ZStringBuilder readInput(int timeout) {
		ZStringBuilder input = null;
		lockMe(this);
		input = readInputNoLock(timeout);
		unlockMe(this);
		return input;
	}

	public ZStringBuilder getCloseSessionCommand() {
		return protocol.getCommandJson(ProtocolObject.CLOSE_SESSION,null);
	}
	
	public void sendCloseSessionCommand() {
		if (isOpenNoLock()) {
			writeOutputNoLock(getCloseSessionCommand());
		}
	}

	public ZStringBuilder sendCommand(String command) {
		return writeOutputReadInput(protocol.getCommandJson(command,null),100);
	}

	public ZStringBuilder sendCommand(String command, String pName, String pValue) {
		SortedMap<String,String> params = new TreeMap<String,String>();
		params.put(pName,pValue);
		return writeOutputReadInput(protocol.getCommandJson(command,params),100);
	}

	public ZStringBuilder sendCommand(String command, String pName1, String pValue1, String pName2, String pValue2) {
		SortedMap<String,String> params = new TreeMap<String,String>();
		params.put(pName1,pValue1);
		params.put(pName2,pValue2);
		return writeOutputReadInput(protocol.getCommandJson(command,params),100);
	}

	public ZStringBuilder sendCommand(String command,SortedMap<String,String> parameters) {
		return writeOutputReadInput(protocol.getCommandJson(command,parameters),100);
	}

	protected ProtocolObject getNewProtocol() {
		return new ProtocolControl();
	}
	
	private void writeOutputNoLock(ZStringBuilder output) {
		initializeConnectionNoLock();
		socket.writeOutput(output);
	}

	private ZStringBuilder readInputNoLock(int timeout) {
		initializeConnectionNoLock();
		ZStringBuilder input = null;
		if (timeout>0) {
			long started = (new Date()).getTime();
			MemberClientWorker worker = new MemberClientWorker(getMessenger(),union,socket,timeout);
			worker.start();
			int i = 0;
			int s = 1;
			boolean error = false;
			boolean timedOut = false;
			while (worker.isReading()) {
				try {
					if (i>20) {
						s = 5;
					}
					if (i>50) {
						s = 10;
					}
					Thread.sleep(s);
				} catch (InterruptedException e) {
					if (getMessenger()!=null) {
						getMessenger().error(this,"Read input was interrupted");
					}
					error = true;
					break;
				}
				if ((new Date()).getTime() > started + timeout) {
					timedOut = true;
					break;
				}
				i++;
			}
			if (!error) {
				if (timedOut) {
					input = getCloseSessionCommand();
				} else {
					input = worker.getInput();
					if (input==null) {
						input = getCloseSessionCommand();
					}
				}
			} else {
				input = getCloseSessionCommand();
			}
		} else {
			input = socket.readInput();
		}
		if (input!=null && input.equals(getCloseSessionCommand())) {
			socket.writeOutput(getCloseSessionCommand());
			socket.close();
			open = false;
		}
		return input;
	}

	private void initializeConnectionNoLock() {
		if (!isOpenNoLock()) {
			if (socket==null) {
				socket = new SocketHandler();
				open = socket.open(ipAddressOrHostName,port,true);
			} else {
				open = socket.open(ipAddressOrHostName,port,true);
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
