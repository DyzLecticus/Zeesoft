package nl.zeesoft.zodb;

import java.util.ArrayList;
import java.util.List;

/**
 * Multiple threading worker union.
 * All workers will join this union by calling the addWorker method.
 * The stopWorkers method will check for working workers and try to stop them.
 */
public final class WorkerUnion {
	private static WorkerUnion			union					= null;
	private	List<Worker>				workers					= new ArrayList<Worker>();
	private Object						unionIsLockedBy			= null;
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
		lockUnion(this);
		workers.add(w);
		unlockUnion(this);
	}

	public void stopWorkers() {
		stopWorkers(null);
	}

	public void stopWorkers(Worker ignoreWorker) {
		lockUnion(this);
		if (!done) {
			if (workers.size()>0) {
				int maxSleep = 1;
	
				// Workers may have already been requested to stop.
				for (Worker w: workers) {
					if ((ignoreWorker==null) || (w!=ignoreWorker)) {
						if (w.isWorking()) {
							if (w.getSleep()>maxSleep) {
								maxSleep = (w.getSleep() + 1);
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
		unlockUnion(this);
	}
	
	/**************************** PRIVATE METHODS **************************/
	private synchronized void lockUnion(Object source) {
		int attempt = 0;
		while (unionIsLocked()) {
		    try {
                wait();
            } catch (InterruptedException e) { 
            	
            }
            attempt ++;
			if (attempt>=1000) {
				Messenger.getInstance().error(this,"Lock failed after " + attempt + " attempts. Source:" + source);
				attempt = 0;
			}
		}
		unionIsLockedBy = source;
	}

	private synchronized void unlockUnion(Object source) {
		if (unionIsLockedBy==source) {
			unionIsLockedBy=null;
			notifyAll();
		}
	}
	
	private synchronized boolean unionIsLocked() {
		return (unionIsLockedBy!=null);
	}
}	
