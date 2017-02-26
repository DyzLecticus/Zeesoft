package nl.zeesoft.zjmo.orchestra;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zjmo.json.JsElem;
import nl.zeesoft.zjmo.json.JsFile;

public abstract class MemberObject extends OrchestraMember {
	private MemberControlWorker	controlWorker		= null;
	private MemberWorkWorker	workWorker			= null;
	private ServerSocket 		controlSocket		= null;
	private ServerSocket 		workSocket			= null;
	private List<MemberWorker>	workers				= new ArrayList<MemberWorker>();

	public MemberObject() {
		setState(MemberState.getState(MemberState.UNKNOWN));
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
			try {
				controlSocket.setSoTimeout(1000);
			} catch (SocketException e) {
				//e.printStackTrace();
			}
			try {
				workSocket.setSoTimeout(1000);
			} catch (SocketException e) {
				//e.printStackTrace();
			}
			if (controlWorker==null) {
				controlWorker = new MemberControlWorker(this);
			}
			controlWorker.start();
			if (workWorker==null) {
				workWorker = new MemberWorkWorker(this);
			}
			workWorker.start();
			setState(MemberState.getState(MemberState.ONLINE));
		} else {
			if (controlSocket!=null) {
				try {
					controlSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
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
				controlSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (workSocket!=null) {
			try {
				workSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (controlWorker!=null) {
			controlWorker.stop();
		}
		if (workWorker!=null) {
			workWorker.stop();
		}
		for (MemberWorker worker: workers) {
			worker.stop();
		}
		workers.clear();
		setState(MemberState.getState(MemberState.OFFLINE));
		unlockMe(this);
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
				worker.stop();
				workers.remove(wrkr);
			}
		}
		unlockMe(this);
	}

	protected Protocol getNewProtocol() {
		return new Protocol();
	}
	
	protected void acceptControl() {
		Socket socket = null;
		try {
			socket = controlSocket.accept();
		} catch (IOException e) {
			// Ignore
		}
		if (socket!=null) {
			lockMe(this);
			SocketHandler sh = new SocketHandler();
			sh.setSocket(socket);
			MemberWorker worker = new MemberWorker(null,null,this,sh,getNewProtocol());
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
			MemberWorker worker = new MemberWorker(null,null,this,sh,getNewProtocol());
			workers.add(worker);
			unlockMe(this);
			worker.start();
		}
	}
	
	protected ZStringBuilder getStateJson() {
		JsFile f = new JsFile();
		lockMe(this);
		Runtime rt = Runtime.getRuntime();
		f.rootElement = new JsElem();
		f.rootElement.children.add(new JsElem("state",getState().getCode(),true));
		f.rootElement.children.add(new JsElem("workLoad","" + workers.size()));
		f.rootElement.children.add(new JsElem("memoryUsage","" + (rt.totalMemory() - rt.freeMemory())));
		unlockMe(this);
		return f.toStringBuilder();
	}
}
