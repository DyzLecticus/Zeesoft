package nl.zeesoft.zjmo.orchestra;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zjmo.orchestra.client.ConductorStateConnector;
import nl.zeesoft.zjmo.orchestra.protocol.ProtocolControl;
import nl.zeesoft.zjmo.orchestra.protocol.ProtocolWork;

/**
 * Abstract member object that implements the control and work protocols.
 */
public abstract class MemberObject extends OrchestraMember {
	private boolean					debug						= false;
	private Messenger				messenger					= null;
	private WorkerUnion				union						= null;
	private boolean					startAndStopMessenger		= false;
	
	private Orchestra				orchestra					= null;
	private MemberControlWorker		controlWorker				= null;
	private MemberWorkWorker		workWorker					= null;
	private ServerSocket 			controlSocket				= null;
	private ServerSocket 			workSocket					= null;
	private List<MemberWorker>		workers						= new ArrayList<MemberWorker>();
	
	private ConductorStateConnector stateConnector				= null;
	
	public MemberObject(Messenger msgr,Orchestra orchestra,String positionName, int positionBackupNumber) {
		this.orchestra = orchestra;
		setPosition(orchestra.getPosition(positionName));
		setPositionBackupNumber(positionBackupNumber);
		super.setState(MemberState.getState(MemberState.UNKNOWN));
		getConfigurationFromOrchestraPosition();
		
		if (msgr!=null) {
			messenger = msgr;
		} else {
			messenger = getNewMessenger();
			startAndStopMessenger = true;
		}
		union = new WorkerUnion(messenger);
		stateConnector = new ConductorStateConnector(messenger,union,true,getId());
		stateConnector.initialize(orchestra,getId());
	}
	
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public Messenger getMessenger() {
		return messenger;
	}

	public WorkerUnion getUnion() {
		return union;
	}
	
	public Orchestra getOrchestra() {
		return orchestra;
	}
	
	public boolean isWorking() {
		boolean r = false;
		lockMe(this);
		if (controlWorker!=null && controlWorker.isWorking()) {
			r = true;
		}
		unlockMe(this);
		return r;
	}

	public boolean start() {
		boolean started = true;
		if (isWorking()) {
			return false;
		}
		
		if (startAndStopMessenger) {
			getMessenger().setPrintDebugMessages(debug);
			getMessenger().start();
		}

		lockMe(this);
		if (controlSocket==null) {
			try {
				controlSocket = new ServerSocket(getControlPort());
			} catch (IOException e) {
				controlSocket = null;
				started = false;
				if (messenger!=null) {
					messenger.error(this,"Failed to open control socket",e);
				} else {
					e.printStackTrace();
				}
			}
		}
		if (workSocket==null) {
			try {
				workSocket = new ServerSocket(getWorkPort());
			} catch (IOException e) {
				workSocket = null;
				started = false;
				if (messenger!=null) {
					messenger.error(this,"Failed to open work socket",e);
				} else {
					e.printStackTrace();
				}
			}
		}
		if (controlSocket!=null && workSocket!=null) {
			if (controlWorker==null) {
				controlWorker = new MemberControlWorker(getMessenger(),union,this);
			}
			controlWorker.start();
			if (workWorker==null) {
				workWorker = new MemberWorkWorker(getMessenger(),union,this);
			}
			workWorker.start();
			stateConnector.open();
			super.setState(MemberState.getState(MemberState.ONLINE));
		} else {
			if (controlSocket!=null) {
				try {
					controlSocket.close();
				} catch (IOException e) {
					if (messenger!=null) {
						messenger.error(this,"Failed to close control socket",e);
					} else {
						e.printStackTrace();
					}
				}
			}
		}
		unlockMe(this);
		return started;
	}
	
	public final void stop() {
		stop(null);
	}
	
	public void stop(Worker ignoreWorker) {
		lockMe(this);
		stateConnector.close();
		if (controlWorker!=null && controlWorker.isWorking()) {
			controlWorker.stop();
		}
		if (workWorker!=null && workWorker.isWorking()) {
			workWorker.stop();
		}
		if (controlSocket!=null) {
			try {
				controlSocket.close();
			} catch (IOException e) {
				if (messenger!=null) {
					messenger.error(this,"Failed to close control socket",e);
				} else {
					e.printStackTrace();
				}
			}
		}
		if (workSocket!=null) {
			try {
				workSocket.close();
			} catch (IOException e) {
				if (messenger!=null) {
					messenger.error(this,"Failed to close work socket",e);
				} else {
					e.printStackTrace();
				}
			}
		}
		controlSocket = null;
		workSocket = null;
		for (MemberWorker worker: workers) {
			if (worker.isWorking()) {
				worker.stop();
			}
		}
		workers.clear();
		super.setState(MemberState.getState(MemberState.OFFLINE));
		unlockMe(this);

		if (startAndStopMessenger) {
			getMessenger().stop();
		}
		union.stopWorkers(ignoreWorker);
		union = new WorkerUnion(messenger);
		if (startAndStopMessenger) {
			getMessenger().whileWorking();
		}
	}

	public boolean goToStateIfState(String goToState,String ifState1) {
		return goToStateIfState(goToState,ifState1,null);
	}
	
	public boolean goToStateIfState(String goToState,String ifState1,String ifState2) {
		boolean r = false;
		lockMe(this);
		if (super.getState()!=null && super.getState().getCode().equals(ifState1) ||
			(ifState2!=null && ifState2.length()>0 && super.getState()!=null && super.getState().getCode().equals(ifState2))
			) {
			super.setState(MemberState.getState(goToState));
			r = true;
		}
		unlockMe(this);
		return r;
	}

	public boolean takeOffLine() {
		boolean r = goToStateIfState(MemberState.GOING_OFFLINE,MemberState.ONLINE,MemberState.DRAINING_OFFLINE);
		if (r) {
			lockMe(this);
			workWorker.stop();
			if (workSocket!=null) {
				try {
					workSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			workSocket = null;
			List<MemberWorker> wrkrs = new ArrayList<MemberWorker>(workers);
			for (MemberWorker wrkr: wrkrs) {
				if (!wrkr.isControl()) {
					stopWorkerNoLock(wrkr);
				}
			}
			super.setState(MemberState.getState(MemberState.OFFLINE));
			unlockMe(this);
		}
		return r;
	}

	public boolean drainOffLine() {
		boolean r = goToStateIfState(MemberState.DRAINING_OFFLINE,MemberState.ONLINE);
		if (r) {
			lockMe(this);
			workWorker.stop();
			unlockMe(this);
		}
		return r;
	}

	public boolean bringOnLine() {
		boolean r = goToStateIfState(MemberState.COMING_ONLINE,MemberState.OFFLINE);
		if (r) {
			lockMe(this);
			super.setState(MemberState.getState(MemberState.COMING_ONLINE));
			boolean opened = true;
			if (workSocket==null) {
				try {
					workSocket = new ServerSocket(getWorkPort());
				} catch (IOException e) {
					workSocket = null;
					opened = false;
					if (messenger!=null) {
						messenger.error(this,"Failed to open work socket",e);
					} else {
						e.printStackTrace();
					}
				}
			}
			if (opened) {
				workWorker.start();
				super.setState(MemberState.getState(MemberState.ONLINE));
			} else {
				super.setState(MemberState.getState(MemberState.OFFLINE));
				r = false;
			}
			unlockMe(this);
		}
		return r;
	}
	
	public ZStringBuilder getStateJson() {
		JsFile f = new JsFile();
		int workLoad = 0;
		lockMe(this);
		Runtime rt = Runtime.getRuntime();
		for (MemberWorker worker: workers) {
			if (!worker.isControl()) {
				workLoad++;
			}
		}
		f.rootElement = new JsElem();
		if (super.getState()!=null) {
			f.rootElement.children.add(new JsElem("state",super.getState().getCode(),true));
		} else {
			f.rootElement.children.add(new JsElem("state",MemberState.UNKNOWN,true));
		}
		f.rootElement.children.add(new JsElem("workLoad","" + workLoad));
		f.rootElement.children.add(new JsElem("memoryUsage","" + (rt.totalMemory() - rt.freeMemory())));
		f.rootElement.children.add(new JsElem("restartRequired","" + isRestartRequired()));
		unlockMe(this);
		return f.toStringBuilder();
	}

	public void updateOrchestra(Orchestra newOrchestra) {
		JsFile oriJson = new JsFile();
		String err = oriJson.fromFile("orchestra.json");
		if (err.length()==0) {
			ZStringBuilder oriZ = oriJson.toStringBuilder();
			ZStringBuilder newZ = newOrchestra.toJson(false).toStringBuilder();
			if (!newZ.equals(oriZ)) {
				newZ.toFile("orchestra.json");
				updatedOrchestra();
			}
		}
	}

	protected void updatedOrchestra() {
		lockMe(this);
		setRestartRequired(true);
		unlockMe(this);
	}
	
	protected Messenger getNewMessenger() {
		return new Messenger(null);
	}
	
	protected void stopProgram(Worker ignoreWorker) {
		stop(ignoreWorker);
		int status = 0;
		if (getMessenger().isError()) {
			status = 1;
		}
		System.exit(status);
	}

	protected void restartProgram(Worker ignoreWorker) {
		stop(ignoreWorker);
		union = new WorkerUnion(messenger);
		JsFile oriJson = new JsFile();
		String err = oriJson.fromFile("orchestra.json");
		if (err.length()==0) {
			orchestra.fromJson(oriJson);
		}
		lockMe(this);
		setRestartRequired(false);
		unlockMe(this);
		start();
	}

	protected void stopWorker(MemberWorker worker) {
		lockMe(this);
		List<MemberWorker> wrkrs = new ArrayList<MemberWorker>(workers);
		for (MemberWorker wrkr: wrkrs) {
			if (wrkr==worker) {
				stopWorkerNoLock(wrkr);
			}
		}
		unlockMe(this);
	}

	protected ProtocolControl getNewControlProtocol() {
		return new ProtocolControl();
	}

	protected ProtocolWork getNewWorkProtocol() {
		return new ProtocolWork();
	}

	protected void acceptControl() {
		Socket socket = null;
		try {
			socket = controlSocket.accept();
		} catch (IOException e) {
			socket = null;
		}
		if (socket!=null) {
			lockMe(this);
			SocketHandler sh = new SocketHandler();
			sh.setSocket(socket);
			MemberWorker worker = new MemberWorker(getMessenger(),union,this,sh,getNewControlProtocol());
			workers.add(worker);
			unlockMe(this);
			worker.start();
		}
	}

	protected void acceptWork() {
		Socket socket = null;
		try {
			socket = workSocket.accept();
		} catch (IOException e) {
			socket = null;
		}
		if (socket!=null) {
			lockMe(this);
			SocketHandler sh = new SocketHandler();
			sh.setSocket(socket);
			try {
				socket.setSoTimeout(1000);
			} catch (SocketException e) {
				// Ignore
			}
			MemberWorker worker = new MemberWorker(getMessenger(),union,this,sh,getNewWorkProtocol());
			workers.add(worker);
			unlockMe(this);
			worker.start();
		}
	}
	
	private void stopWorkerNoLock(MemberWorker worker) {
		if (worker.isWorking()) {
			worker.stop();
		}
		workers.remove(worker);
	}

	private void getConfigurationFromOrchestraPosition() {
		OrchestraMember member = orchestra.getMemberById(getId());
		if (member!=null) {
			setIpAddressOrHostName(member.getIpAddressOrHostName());
			setControlPort(member.getControlPort());
			setWorkPort(member.getWorkPort());
		}
	}
}
