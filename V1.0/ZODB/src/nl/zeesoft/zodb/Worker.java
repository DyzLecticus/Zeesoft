package nl.zeesoft.zodb;

/**
 * Multiple threading worker. 
 */
public abstract class Worker implements Runnable {
	private int				sleep		= 100; // ms
	private Thread 			worker 		= null;
	private boolean 		working 	= false;
	
	public Worker() {
		WorkerUnion.getInstance().addWorker(this);
	}
	
	public void start() {
		if (working) {
			stop();
		}
        if (worker == null) {
            worker = new Thread(this);
            worker.start();
        }
    }

    public void stop() {
        worker = null;
    }

	@Override
	public final void run() {
		working = true;
		while ((worker!=null) && (!worker.isInterrupted())) {
			whileWorking();
			try {
				if ((sleep>0) && (worker!=null) && (!worker.isInterrupted())) {
					if (sleep>=100) {
						for (int i = 0; i < (sleep / 10); i++) {
							sleep(10);
							if (worker==null) {
								break;
							}
						}
					} else {
						sleep(sleep);
					}
				}
			} catch (NullPointerException e) {
				// Ignore
			}
		}
		working = false;
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
	public int getSleep() {
		return sleep;
	}

	/**
	 * @param sleep the sleep to set
	 */
	public void setSleep(int sleep) {
		this.sleep = sleep;
	}

	/**
	 * @return the working
	 */
	public boolean isWorking() {
		return working;
	}
}	
