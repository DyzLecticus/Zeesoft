package nl.zeesoft.zdk.thread;

import java.util.Date;

import nl.zeesoft.zdk.messenger.Messenger;

/**
 * Abstract multiple threading worker. 
 */
public abstract class Worker extends Locker implements Runnable {
	private WorkerUnion		union				= null;
	private int				sleep				= 100; // ms
	private int				sleepNs				= 0; // ns
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
		int slpNs = 0;
		long compensatedSleep = slp;
		Date started = null;
		boolean stopOnEx = true;
		
		lockMe(this);
		working = true;
		wrkr = worker;
		slp = sleep;
		slpNs = sleepNs;
		stopOnEx = stopOnException;
		unlockMe(this);
		
		startedWorking();
		
		while ((wrkr!=null) && (!wrkr.isInterrupted())) {
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
			
			wrkr = getWorker();
			if (wrkr==null || wrkr.isInterrupted()) {
				break;
			}
			
			compensatedSleep = slp;
			if (slp>=10) {
				compensatedSleep = (slp - ((new Date()).getTime() - started.getTime()));
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
			if (slpNs>0) {
				sleepNs(slpNs);
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
		
		stoppedWorking();
	}
	
	/**
	 * Called when the worker has stopped.
	 */
	protected void startedWorking() {
		// Override to implement
	}

	/**
	 * Override this method to implement logic in the Worker.
	 */
	protected abstract void whileWorking();
	
	/**
	 * Called when the worker has stopped.
	 */
	protected void stoppedWorking() {
		// Override to implement
	}

	/**
	 * Returns the amount of milliseconds this worker will sleep between whileWorking method calls.
	 *
	 * @return The amount of milliseconds this worker will sleep between whileWorking method calls
	 */
	protected final int getSleep() {
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
	protected final void setSleep(int sleep) {
		lockMe(this);
		this.sleep = sleep;
		unlockMe(this);
	}

	/**
	 * Sets the amount of nanoseconds this worker should sleep between whileWorking method calls (default 0).
	 * 
	 * @param sleepNs The amount of nanoseconds this worker should sleep between whileWorking method calls
	 */
	protected final void setSleepNs(int sleepNs) {
		lockMe(this);
		this.sleepNs = sleepNs;
		unlockMe(this);
	}

	/**
	 * Indicates the worker should stop if an exception is thrown while working (default true).
	 * 
	 * @param stopOnException True if the worker should stop if an exception is thrown while working
	 */
	protected final void setStopOnException(boolean stopOnException) {
		lockMe(this);
		this.stopOnException = stopOnException;
		unlockMe(this);
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

	protected WorkerUnion getUnion() {
		return union;
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
		} else {
			caughtException.printStackTrace();
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
	
	private final void sleepNs(int sleepNs) {
		try {
			Thread.sleep(0,sleepNs);
		} catch (InterruptedException e) {
			// Ignore
		}
	}
}	
