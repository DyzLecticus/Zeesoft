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
import nl.zeesoft.zjmo.Orchestrator;
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

	private boolean					stopOnRestart				= false;
			
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
	}
	
	/**
	 * Sets the debug indicator.
	 * Used to set the messenger debug logging to true when the member is started.
	 * 
	 * @param debug Indicates debugging
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	/**
	 * Returns the messenger.
	 * 
	 * @return The messenger
	 */
	public Messenger getMessenger() {
		return messenger;
	}

	/**
	 * Returns the worker union.
	 * 
	 * @return The worker union
	 */
	public WorkerUnion getUnion() {
		return union;
	}
	
	/**
	 * Returns the orchestra.
	 * 
	 * @return The orchestra
	 */
	public Orchestra getOrchestra() {
		return orchestra;
	}
	
	/**
	 * Returns true if the member is working.
	 * 
	 * @return True if the member is working
	 */
	public boolean isWorking() {
		boolean r = false;
		lockMe(this);
		if (controlWorker!=null && controlWorker.isWorking()) {
			r = true;
		}
		unlockMe(this);
		return r;
	}

	/**
	 * Starts the member.
	 * 
	 * @return True if the member has started.
	 */
	public boolean start() {
		boolean started = true;
		if (isWorking()) {
			return false;
		}
		
		stateConnector.initialize(orchestra,getId());
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
	
	/**
	 * Stops the member.
	 * Uses the worker union to enforce all workers are stopped.
	 */
	public final void stop() {
		stop(null);
	}
	
	/**
	 * Stops the member.
	 * Uses the worker union to enforce all workers are stopped.
	 * 
	 * @param ignoreWorker The worker to ignore
	 */
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
	
	/**
	 * Transfers the member state to a certain state if it is in a certain state.
	 * 
	 * @param goToState The state to transfer to
	 * @param ifState1 The conditional state
	 * @return True if the state transition is successful
	 */
	public boolean goToStateIfState(String goToState,String ifState1) {
		return goToStateIfState(goToState,ifState1,null);
	}
	
	/**
	 * Transfers the member state to a certain state if it is in one or another state.
	 * 
	 * @param goToState The state to transfer to
	 * @param ifState1 The first conditional state
	 * @param ifState2 The second conditional state
	 * @return True if the state transition is successful
	 */
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

	/**
	 * Takes the member offline.
	 * Closes the work port.
	 * 
	 * @return True if the member is offline 
	 */
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

	/**
	 * Drains the member offline.
	 * Stops the work port from accepting new connections.
	 * 
	 * @return True if the member is draining offline 
	 */
	public boolean drainOffLine() {
		boolean r = goToStateIfState(MemberState.DRAINING_OFFLINE,MemberState.ONLINE);
		if (r) {
			lockMe(this);
			workWorker.stop();
			unlockMe(this);
		}
		return r;
	}

	/**
	 * Brings the member online.
	 * 
	 * @return True if the member is online
	 */
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
	
	/**
	 * Returns the member state JSON.
	 * 
	 * @return The member state JSON.
	 */
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
		f.rootElement.children.add(new JsElem("restartRequired","" + super.isRestartRequired()));
		unlockMe(this);
		return f.toStringBuilder();
	}

	/**
	 * Updates the orchestra.
	 * Overwrites the orchestra.json file used to configure the instance.
	 * 
	 * @param newOrchestra The new orchestra configuration
	 * @return An error message if applicable
	 */
	public String updateOrchestra(Orchestra newOrchestra) {
		JsFile oriJson = new JsFile();
		String err = oriJson.fromFile(Orchestrator.ORCHESTRA_JSON);
		if (err.length()==0) {
			ZStringBuilder oriZ = oriJson.toStringBuilder();
			ZStringBuilder newZ = newOrchestra.toJson(false).toStringBuilderReadFormat();
			if (!newZ.equals(oriZ)) {
				if (newOrchestra.getMemberById(getId())!=null) {
					err = newZ.toFile(Orchestrator.ORCHESTRA_JSON);
				} else {
					lockMe(this);
					stopOnRestart = true;
					unlockMe(this);
				}
				if (checkRestartRequired(newOrchestra)) {
					setRestartRequired(true);
				}
			}
		}
		return err;
	}
	
	/**
	 * Used to indicate a restart is required due to configuration changes.
	 * 
	 * @param restartRequired Indicates a restart is required
	 */
	@Override
	public void setRestartRequired(boolean restartRequired) {
		lockMe(this);
		super.setRestartRequired(restartRequired);
		unlockMe(this);
	}
	
	/**
	 * Returns true if a restart is required due to configuration changes.
	 * 
	 * @return True if a restart is required due to configuration changes
	 */
	@Override
	public boolean isRestartRequired() {
		boolean r = false;
		lockMe(this);
		r = super.isRestartRequired();
		unlockMe(this);
		return r;
	}

	/**
	 * Creates and returns a new messenger.
	 * 
	 * @return A new messenger
	 */
	protected Messenger getNewMessenger() {
		return new Messenger(null);
	}

	/**
	 * Returns true if a restart is required due to changes in the orchestra configuration.
	 * Designed to be overridden by extending classes.
	 * 
	 * @param newOrchestra The new orchestra configuration.
	 * @return True if a restart is required due to changes in the orchestra configuration
	 */
	protected boolean checkRestartRequired(Orchestra newOrchestra) {
		return false;
	}
	
	/**
	 * Stops the program.
	 * Calls System.exit
	 * 
	 * @param ignoreWorker The worker to ignore
	 */
	protected void stopProgram(Worker ignoreWorker) {
		stop(ignoreWorker);
		int status = 0;
		if (getMessenger().isError()) {
			status = 1;
		}
		System.exit(status);
	}

	/**
	 * Restarts the program.
	 * Reloads the configuration and reinitializes the member while the member is not working.
	 * 
	 * @param ignoreWorker The worker to ignore
	 */
	protected void restartProgram(Worker ignoreWorker) {
		lockMe(this);
		boolean stopProgram = stopOnRestart;
		unlockMe(this);
		if (stopProgram) {
			stopProgram(ignoreWorker);
		} else {
			stop(ignoreWorker);
			JsFile json = new JsFile();
			String err = json.fromFile(Orchestrator.ORCHESTRA_JSON);
			if (err.length()==0) {
				orchestra.fromJson(json);
				getConfigurationFromOrchestraPosition();
				setRestartRequired(false);
			}
			start();
		}
	}
	
	/**
	 * Stops a certain member worker.
	 * 
	 * @param worker The member worker to stop.
	 */
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

	/**
	 * Creates and returns a new control protocol instance.
	 * 
	 * @return A new control protocol instance
	 */
	protected ProtocolControl getNewControlProtocol() {
		return new ProtocolControl();
	}

	/**
	 * Creates and returns a new work protocol instance.
	 * 
	 * @return A new work protocol instance
	 */
	protected ProtocolWork getNewWorkProtocol() {
		return new ProtocolWork();
	}

	/**
	 * Accepts control sessions on the control socket.
	 * Creates member workers for each session.
	 */
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

	/**
	 * Accepts work sessions on the work socket.
	 * Creates member workers for each session.
	 */
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
