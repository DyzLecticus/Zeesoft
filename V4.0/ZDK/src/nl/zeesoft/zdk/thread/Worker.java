package nl.zeesoft.zdk.thread;

import java.util.Date;

import nl.zeesoft.zdk.messenger.Messenger;

/**
 * Abstract multiple threading worker. 
 */
public abstract class Worker extends Locker implements Runnable {
	private WorkerUnion		union				= null;
	private int				sleep				= 100; // ms
	private Thread 			worker 				= null;
	private boolean 		working 			= false;
	private boolean			stopOnException		= true;
	private Exception		caughtException		= null;

	public Worker(Messenger msgr, WorkerUnion union) {
		super(msgr);
		this.union = union;
	}
	
	/**
	 * Starts the worker
	 */
	public void start() {
		if (isWorking()) {
			stop();
		}
		lockMe(this);
		if (worker == null) {
			worker = new Thread(this);
			worker.start();
		}
		unlockMe(this);
	}

	/**
	 * Stops the worker
	 * 
	 * Use the waitForStop method to force calling threads to wait for this worker to stop.
	 */
	public void stop() {		
		lockMe(this);
		worker = null;
		unlockMe(this);
	}

	/**
	 * Override this method to implement logic in the Worker.
	 */
	public abstract void whileWorking();

	/**
	 * Returns the amount of milliseconds this worker will sleep between whileWorking method calls.
	 *
	 * @return The amount of milliseconds this worker will sleep between whileWorking method calls
	 */
	public final int getSleep() {
		int r = 100;
		lockMe(this);
		r = sleep;
		unlockMe(this);
		return r;
	}

	/**
	 * Sets the amount of milliseconds this worker should sleep between whileWorking method calls (default 100).
	 * 
	 * @param sleep The amount of milliseconds this worker should sleep between whileWorking method calls
	 */
	public final void setSleep(int sleep) {
		lockMe(this);
		this.sleep = sleep;
		unlockMe(this);
	}

	/**
	 * Indicates the worker should stop if an exception is thrown while working (default true).
	 * 
	 * @param stopOnException True if the worker should stop if an exception is thrown while working
	 */
	public final void setStopOnException(boolean stopOnException) {
		lockMe(this);
		this.stopOnException = stopOnException;
		unlockMe(this);
	}

	/**
	 * Returns true if the worker is working.
	 * 
	 * @return True if the worker is working
	 */
	public final boolean isWorking() {
		boolean r = false;
		lockMe(this);
		r = working;
		unlockMe(this);
		return r;
	}

	/**
	 * Returns the exception that was thrown while working.
	 * 
	 * @return The exception that was thrown while working
	 */
	public Exception getCaughtException() {
		Exception r = null;
		lockMe(this);
		r = caughtException;
		unlockMe(this);
		return r;
	}
	
	/**
	 * Do not call this method.
	 */
	@Override
	public final void run() {
		if (union!=null) {
			union.addWorker(this);
		}

		Thread wrkr = null;
		int slp = 100;
		long compensatedSleep = slp;
		Date started = null;
		boolean stopOnEx = true;
		
		lockMe(this);
		working = true;
		wrkr = worker;
		slp = sleep;
		stopOnEx = stopOnException;
		unlockMe(this);
		
		while ((wrkr!=null) && (!wrkr.isInterrupted())) {
			compensatedSleep = slp;
			started = null;
			if (slp>=10) {
				started = new Date();
			}
			try {
				whileWorking();
			} catch (Exception ex) {
				setCaughtException(ex);
				if (stopOnEx) {
					lockMe(this);
					worker = null;
					unlockMe(this);
					if (getMessenger()!=null) {
						getMessenger().warn(this,"Worker has stopped on exception");
					}
				}
			}
			if (slp>=10) {
				compensatedSleep = (slp - ((new Date()).getTime() - started.getTime()));
			}
			wrkr = getWorker();
			if (wrkr==null || wrkr.isInterrupted()) {
				break;
			}
			if (compensatedSleep>0) {
				if (compensatedSleep>=100) {
					for (int i = 0; i < (compensatedSleep / getMaxSleepForSleep(compensatedSleep)); i++) {
						sleep(getMaxSleepForSleep(compensatedSleep));
						wrkr = getWorker(); 
						if (wrkr==null || wrkr.isInterrupted()) {
							break;
						}
					}
				} else {
					sleep(compensatedSleep);
				}
			}
			if (wrkr==null || wrkr.isInterrupted()) {
				break;
			} else {
				lockMe(this);
				wrkr = worker;
				slp = sleep;
				unlockMe(this);
			}
		}
		
		lockMe(this);
		working = false;
		unlockMe(this);
		
		if (union!=null) {
			union.removeWorker(this);
		}
	}
	
	/**
	 * Forces calling threads to wait for this worker to stop.
	 * 
	 * Designed to be called from an override of the stop method.
	 * 
	 * @param timeOutSeconds The seconds to wait before timing out the stop wait
	 * @param silent Indicates time out should use the Messenger to log an error message
	 */
	protected final void waitForStop(int timeOutSeconds,boolean silent) {
		int tries = 0;
		int maxTries = (timeOutSeconds * 100);
		while (isWorking()) {
			tries++;
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				if (getMessenger()!=null) {
					getMessenger().error(this,"Stopping worker was interrupted");
				}
			}
			if (tries>maxTries) {
				break;
			}
		}
		if (tries>maxTries && !silent && getMessenger()!=null) {
			getMessenger().error(this,"Failed to stop worker within " + timeOutSeconds + " seconds");
		}
	}
	
	protected static final long getMaxSleepForSleep(long sleep) {
		if (sleep>=1000) {
			sleep = 100;
		} else if (sleep>=100) {
			sleep = 10;
		}
		return sleep;
	}

	protected void setCaughtException(Exception caughtException) {
		if (getMessenger()!=null) {
			getMessenger().error(this,"Error while working",caughtException);
		}
		lockMe(this);
		this.caughtException = caughtException;
		unlockMe(this);
	}

	private Thread getWorker() {
		Thread wrkr;
		lockMe(this);
		wrkr = worker;
		unlockMe(this);
		return wrkr;
	}

	private final void sleep(long sleep) {
		try {
			Thread.sleep(sleep);
		} catch (InterruptedException e) {
			// Ignore
		}
	}
}	
