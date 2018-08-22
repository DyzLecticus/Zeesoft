package nl.zeesoft.zdk.thread;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.messenger.Messenger;

/**
 * Multiple threading worker union.
 * All workers will join and leave this union by calling the addWorker and removeWorker methods respectively.
 * The stopWorkers method will check for working workers and try to stop them.
 */
public final class WorkerUnion extends Locker {
	private	List<Worker>				workers					= new ArrayList<Worker>();
	private boolean						done					= false;

	public WorkerUnion() {
		super(null);
	}

	public WorkerUnion(Messenger msgr) {
		super(msgr);
	}

	protected void addWorker(Worker w) {
		lockMe(this);
		workers.add(w);
		unlockMe(this);
	}

	protected void removeWorker(Worker w) {
		lockMe(this);
		workers.remove(w);
		unlockMe(this);
	}

	/**
	 * Call this method when stopping the application in order to ensure all workers are stopped.
	 */
	public void stopWorkers() {
		stopWorkers(null);
	}

	/**
	 * Call this method when stopping the application in order to ensure all workers are stopped.
	 * 
	 * @param ignoreWorker The optional worker to ignore
	 */
	public void stopWorkers(Worker ignoreWorker) {
		lockMe(this);
		if (!done) {
			if (workers.size()>0) {
				long maxSleep = 1;
	
				// Workers may have already been requested to stop.
				for (Worker w: workers) {
					if ((ignoreWorker==null) || (w!=ignoreWorker)) {
						if (w.isWorking()) {
							if (w.getSleep()>maxSleep) {
								maxSleep = (Worker.getMaxSleepForSleep(w.getSleep()) + 1);
							}
						}
					}
				}
				try {
					Thread.sleep(maxSleep);
				} catch (InterruptedException e) {
					// Ignore
				}
				
				List<Worker> stoppingWorkers = new ArrayList<Worker>();
				for (Worker w: workers) {
					if ((ignoreWorker==null) || (w!=ignoreWorker)) {
						if (w.isWorking()) {
							if (getMessenger()!=null) {
								getMessenger().warn(this, "Stopping worker: " + w.getClass().getName());
							}
							stoppingWorkers.add(w);
							w.stop();
							if (w.getSleep()>maxSleep) {
								maxSleep = (w.getSleep() + 1);
							}
						}
					}
				}
				workers.clear();
				if (stoppingWorkers.size()>0) {
					// Wait for workers to stop
					try {
						Thread.sleep(maxSleep);
					} catch (InterruptedException e) {
						// Ignore
					}
					for (Worker w: stoppingWorkers) {
						if (w.isWorking()) {
							workers.add(w);
							if (getMessenger()!=null) {
								getMessenger().error(this, "Failed to stop worker: " + w.getClass().getName());
							}
						}
					}
				} else {
					if (getMessenger()!=null) {
						getMessenger().debug(this, "All workers have been stopped");
					}
				}
			}
			done = true;
		}
		unlockMe(this);
	}
}	
