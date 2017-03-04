package nl.zeesoft.zjmo.orchestra;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zjmo.json.JsElem;
import nl.zeesoft.zjmo.json.JsFile;

public abstract class MemberObject extends OrchestraMember {
	private Orchestra			orchestra			= null;
	private MemberControlWorker	controlWorker		= null;
	private MemberWorkWorker	workWorker			= null;
	private ServerSocket 		controlSocket		= null;
	private ServerSocket 		workSocket			= null;
	private List<MemberWorker>	workers				= new ArrayList<MemberWorker>();
	
	public MemberObject(Orchestra orchestra,String positionName, int positionBackupNumber) {
		this.orchestra = orchestra;
		setPosition(orchestra.getPosition(positionName));
		setPositionBackupNumber(positionBackupNumber);
		super.setState(MemberState.getState(MemberState.UNKNOWN));
		getConfigurationFromOrchestraPosition();
	}
	
	public Orchestra getOrchestra() {
		return orchestra;
	}
	
	public boolean isWorking() {
		boolean r = false;
		lockMe(this);
		if (controlWorker!=null && controlWorker.isWorking() && 
			workWorker!=null && workWorker.isWorking()
			) {
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
		lockMe(this);
		if (controlSocket==null) {
			try {
				controlSocket = new ServerSocket(getControlPort());
			} catch (IOException e) {
				controlSocket = null;
				started = false;
				e.printStackTrace();
			}
		}
		if (workSocket==null) {
			try {
				workSocket = new ServerSocket(getWorkPort());
			} catch (IOException e) {
				workSocket = null;
				started = false;
				e.printStackTrace();
			}
		}
		if (controlSocket!=null && workSocket!=null) {
			if (controlWorker==null) {
				controlWorker = new MemberControlWorker(this);
			}
			controlWorker.start();
			if (workWorker==null) {
				workWorker = new MemberWorkWorker(this);
			}
			workWorker.start();
			super.setState(MemberState.getState(MemberState.ONLINE));
		} else {
			if (controlSocket!=null) {
				try {
					controlSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		unlockMe(this);
		return started;
	}
	
	public void stop() {
		lockMe(this);
		if (controlSocket!=null) {
			try {
				//System.out.println("Closing control socket ...");
				controlSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (workSocket!=null) {
			try {
				//System.out.println("Closing work socket ...");
				workSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (controlWorker!=null) {
			//System.out.println("Stopping control worker ...");
			controlWorker.stop();
		}
		if (workWorker!=null) {
			//System.out.println("Stopping work worker ...");
			workWorker.stop();
		}
		//System.out.println("Stopping member workers ...");
		for (MemberWorker worker: workers) {
			if (worker.isWorking()) {
				//System.out.println("Stopping member worker ...");
				worker.stop();
			} else {
				//System.out.println("Member worker already stopped");
			}
		}
		workers.clear();
		//System.out.println("Stopped member workers");
		super.setState(MemberState.getState(MemberState.OFFLINE));
		unlockMe(this);
	}

	public boolean goToStateIfState(String goToState,String ifState1) {
		return goToStateIfState(goToState,ifState1,null);
	}
	
	public boolean goToStateIfState(String goToState,String ifState1,String ifState2) {
		boolean r = false;
		lockMe(this);
		if (super.getState().getCode().equals(ifState1) ||
			(ifState2!=null && ifState2.length()>0 && super.getState().getCode().equals(ifState2))
			) {
			super.setState(MemberState.getState(goToState));
			r = true;
		}
		unlockMe(this);
		return r;
	}

	public boolean takeOffLine() {
		boolean r = goToStateIfState(MemberState.GOING_OFFLINE,MemberState.ONLINE);
		if (r) {
			lockMe(this);
			super.setState(MemberState.getState(MemberState.GOING_OFFLINE));
			List<MemberWorker> wrkrs = new ArrayList<MemberWorker>(workers);
			for (MemberWorker wrkr: wrkrs) {
				if (!wrkr.isControl()) {
					stopWorkerNoLock(wrkr);
				}
			}
			workWorker.stop();
			super.setState(MemberState.getState(MemberState.OFFLINE));
			unlockMe(this);
		}
		return r;
	}

	public boolean drainOffLine() {
		boolean r = goToStateIfState(MemberState.DRAINING_OFFLINE,MemberState.ONLINE);
		if (r) {
			lockMe(this);
			super.setState(MemberState.getState(MemberState.DRAINING_OFFLINE));
			workWorker.stop();
			super.setState(MemberState.getState(MemberState.OFFLINE));
			unlockMe(this);
		}
		return r;
	}

	public boolean bringOnLine() {
		boolean r = goToStateIfState(MemberState.COMING_ONLINE,MemberState.OFFLINE);
		if (r) {
			lockMe(this);
			super.setState(MemberState.getState(MemberState.COMING_ONLINE));
			workWorker.stop();
			super.setState(MemberState.getState(MemberState.ONLINE));
			unlockMe(this);
		}
		return r;
	}

	protected void stopProgram() {
		stop();
		System.exit(0);
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
			MemberWorker worker = new MemberWorker(null,null,this,sh,getNewControlProtocol());
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
			MemberWorker worker = new MemberWorker(null,null,this,sh,getNewControlProtocol());
			workers.add(worker);
			unlockMe(this);
			worker.start();
		}
	}
	
	protected ZStringBuilder getStateJson() {
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
		f.rootElement.children.add(new JsElem("state",super.getState().getCode(),true));
		f.rootElement.children.add(new JsElem("workLoad","" + workLoad));
		f.rootElement.children.add(new JsElem("memoryUsage","" + (rt.totalMemory() - rt.freeMemory())));
		unlockMe(this);
		return f.toStringBuilder();
	}
	
	private void stopWorkerNoLock(MemberWorker worker) {
		if (worker.isWorking()) {
			worker.stop();
		}
		workers.remove(worker);
	}

	private void getConfigurationFromOrchestraPosition() {
		OrchestraMember member = orchestra.getMemberForPosition(getPosition().getName(),getPositionBackupNumber());
		if (member!=null) {
			setIpAddressOrHostName(member.getIpAddressOrHostName());
			setControlPort(member.getControlPort());
			setWorkPort(member.getWorkPort());
		}
	}
}
