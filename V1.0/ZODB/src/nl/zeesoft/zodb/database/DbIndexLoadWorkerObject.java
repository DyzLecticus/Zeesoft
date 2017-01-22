package nl.zeesoft.zodb.database;

import nl.zeesoft.zodb.Worker;

public abstract class DbIndexLoadWorkerObject extends Worker {
	private boolean done = false;
	
	protected DbIndexLoadWorkerObject() {
		setSleep(1);
	}

	/**
	 * @return the done
	 */
	protected boolean isDone() {
		return done;
	}

	/**
	 * @param done the done to set
	 */
	protected void setDone(boolean done) {
		this.done = done;
	}

}	
