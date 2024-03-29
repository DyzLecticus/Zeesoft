package nl.zeesoft.zodb;

import java.util.ArrayList;
import java.util.List;

/**
 * Multiple threading worker union.
 * All workers will join this union by calling the addWorker method.
 * The stopWorkers method will check for working workers and try to stop them.
 */
public final class WorkerUnion extends Locker {
	private static WorkerUnion			union					= null;
	private	List<Worker>				workers					= new ArrayList<Worker>();
	private boolean						done					= false;
	
	private WorkerUnion() {
		// Singleton
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
	    throw new CloneNotSupportedException(); 
	}

	public static WorkerUnion getInstance() {
		if (union==null) {
			union = new WorkerUnion();
		}
		return union;
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

	public void stopWorkers() {
		stopWorkers(null);
	}

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
							Messenger.getInstance().warn(this, "Stopping worker: " + w.getClass().getName());
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
							Messenger.getInstance().error(this, "Failed to stop worker: " + w.getClass().getName());
						}
					}
				} else {
					Messenger.getInstance().debug(this, "All workers have been stopped");
				}
			}
			done = true;
		}
		unlockMe(this);
	}
}	
