package nl.zeesoft.zdk.thread;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.messenger.Messenger;

public abstract class StateWorker extends Worker {
	public static final String			STATE_STOPPED	= "STOPPED";
	public static final String			STATE_STARTING	= "STARTING";
	public static final String			STATE_STARTED	= "STARTED";
	public static final String			STATE_STOPPING	= "STOPPING";
	
	private String						state			= STATE_STOPPED;
	
	private List<StateWorkerListener>	listeners		= new ArrayList<StateWorkerListener>();
	
	public StateWorker(Messenger msgr, WorkerUnion union) {
		super(msgr, union);
	}
	
	/**
	 * Returns the state.
	 * 
	 * @return The state
	 */
	public String getState() {
		String r = "";
		lockMe(this);
		r = state;
		unlockMe(this);
		return r;
	}
	
	/**
	 * Indicates the grid state does not equal STATE_STOPPED.
	 * 
	 * @return True if the grid state does not equal STATE_STOPPED
	 */
	public boolean isActive() {
		boolean r = false;
		lockMe(this);
		r = !state.equals(STATE_STOPPED);
		unlockMe(this);
		return r;
	}

	
	@Override
	public void start() {
		boolean r = false;
		lockMe(this);
		r = state.equals(STATE_STOPPED);
		if (r) {
			state = STATE_STARTING;
		}
		unlockMe(this);
		if (r) {
			onStart();
			super.start();
			changedState();
		}
	}

	@Override
	public void stop() {
		boolean r = false;
		lockMe(this);
		r = state.equals(STATE_STARTED);
		if (r) {
			state = STATE_STOPPING;
		}
		unlockMe(this);
		if (r) {
			onStop();
			super.stop();
			changedState();
		}
	}

	/**
	 * Pauses the calling thread while the grid is inactive.
	 */
	public void whileInactive() {
		whileActive(false);
	}
	
	/**
	 * Pauses the calling thread while the grid is active.
	 */
	public void whileActive() {
		whileActive(true);
	}

	protected void onStart() {
		// Override to implement
	}

	protected void onStop() {
		// Override to implement
	}
	
	@Override
	protected void startedWorking() {
		lockMe(this);
		state = STATE_STARTED;
		unlockMe(this);
		changedState();
	}
	
	@Override
	protected void stoppedWorking() {
		lockMe(this);
		state = STATE_STOPPED;
		unlockMe(this);
		changedState();
	}

	protected String getStateNoLock() {
		return state;
	}

	protected void whileActive(boolean active) {
		while(isActive()==active) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				if (getMessenger()!=null) {
					getMessenger().error(this,"Waiting for state change was interrupted",e);
				} else {
					e.printStackTrace();
				}
			}
		}
	}
	
	protected void changedState() {
		lockMe(this);
		List<StateWorkerListener> list = new ArrayList<StateWorkerListener>(listeners);
		String stat = state;
		unlockMe(this);
		if (list.size()>0) {
			for (StateWorkerListener listener: list) {
				try {
					listener.changedState(this,stat);
				} catch(Exception e) {
					if (getMessenger()!=null) {
						getMessenger().error(this,"State worker listener threw an exception",e);
					} else {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
