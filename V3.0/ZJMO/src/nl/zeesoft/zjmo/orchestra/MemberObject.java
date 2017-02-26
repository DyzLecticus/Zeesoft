package nl.zeesoft.zjmo.orchestra;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public abstract class MemberObject extends OrchestraMember {
	private MemberControlWorker	controlWorker		= null;
	private MemberWorkWorker	workWorker			= null;
	private ServerSocket 		controlSocket		= null;
	private ServerSocket 		workSocket			= null;
	private List<MemberWorker>	workers				= new ArrayList<MemberWorker>();

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
				controlSocket.setSoTimeout(1000);
			} catch (IOException e) {
				controlSocket = null;
				started = false;
				e.printStackTrace();
			}
		}
		if (workSocket==null) {
			try {
				workSocket = new ServerSocket(getControlPort());
				workSocket.setSoTimeout(1000);
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
		}
		unlockMe(this);
		return started;
	}
	
	public void stop() {
		lockMe(this);
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
		unlockMe(this);
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
			MemberWorker worker = new MemberWorker(null,null,this,socket,getNewProtocol());
			workers.add(worker);
			worker.start();
			unlockMe(this);
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
			MemberWorker worker = new MemberWorker(null,null,this,socket,getNewProtocol());
			workers.add(worker);
			worker.start();
			unlockMe(this);
		}
	}
}
