package nl.zeesoft.zodb;

import java.util.Date;

/**
 * Multiple threading worker. 
 */
public abstract class Worker extends Locker implements Runnable {
	private int				sleep				= 100; // ms
	private Thread 			worker 				= null;
	private boolean 		working 			= false;
	private boolean			stopOnException		= true;
	private Exception		caughtException		= null;
	
	public void start() {
		WorkerUnion.getInstance().addWorker(this);
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

    public void stop() {    	
		lockMe(this);
        worker = null;
		unlockMe(this);
    }

    protected final void waitForStop(int timeOutSeconds,boolean silent) {
		int tries = 0;
		int maxTries = (timeOutSeconds * 100);
		while (isWorking()) {
			tries++;
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				Messenger.getInstance().error(this,"Stopping worker was interrupted");
			}
			if (tries>maxTries) {
				break;
			}
		}
		if (tries>maxTries && !silent) {
			Messenger.getInstance().error(this,"Failed to stop worker within " + timeOutSeconds + " seconds");
		}
    }
    
	@Override
	public final void run() {
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
					if (!(this instanceof Messenger)) {
						Messenger.getInstance().warn(this,"Worker has stopped on exception");
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
		WorkerUnion.getInstance().removeWorker(this);
	}
	
	protected static final long getMaxSleepForSleep(long sleep) {
		if (sleep>=1000) {
			sleep = 100;
		} else if (sleep>=100) {
			sleep = 10;
		}
		return sleep;
	}
	
	protected void sleep(long sleep) {
		try {
			Thread.sleep(sleep);
		} catch (InterruptedException e) {
			// ...
		}
	}
	
	/**
	 * Override this method to implement
	 */
	public abstract void whileWorking();

	/**
	 * @return the sleep
	 */
	public final int getSleep() {
		int r = 100;
		lockMe(this);
		r = sleep;
		unlockMe(this);
		return r;
	}

	/**
	 * @param sleep the sleep to set
	 */
	public final void setSleep(int sleep) {
		lockMe(this);
		this.sleep = sleep;
		unlockMe(this);
	}

	/**
	 * @param stopOnException the stopOnException to set
	 */
	public final void setStopOnException(boolean stopOnException) {
		lockMe(this);
		this.stopOnException = stopOnException;
		unlockMe(this);
	}

	/**
	 * @return the working
	 */
	public final boolean isWorking() {
		boolean r = false;
		lockMe(this);
		r = working;
		unlockMe(this);
		return r;
	}

	/**
	 * @return the caughtException
	 */
	public Exception getCaughtException() {
		Exception r = null;
		lockMe(this);
		r = caughtException;
		unlockMe(this);
		return r;
	}

	/**
	 * @param caughtException the caughtException to set
	 */
	protected void setCaughtException(Exception caughtException) {
		if (!(this instanceof Messenger)) {
			Messenger.getInstance().error(this,"Error while working: " + caughtException + "\n" + Generic.getCallStackString(caughtException.getStackTrace(),""));
		}
		lockMe(this);
		this.caughtException = caughtException;
		unlockMe(this);
	}

	/**
	 * @return the worker
	 */
	private Thread getWorker() {
		Thread wrkr;
		lockMe(this);
		wrkr = worker;
		unlockMe(this);
		return wrkr;
	}
}	
