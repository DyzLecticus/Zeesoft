package nl.zeesoft.zdk.thread;

public class Busy {
	private Lock		lock	= new Lock();
	private Object		owner	= null;
	private boolean		busy	= false;
	
	public Busy(Object owner) {
		this.owner = owner;
		if (owner==null) {
			this.owner = this;
		}
	}

	public boolean isBusy() {
		lock.lock(owner);
		boolean r = busy;
		lock.unlock(owner);
		return r;
	}
	
	public void setBusy(boolean busy) {
		lock.lock(owner);
		this.busy = busy;
		lock.unlock(owner);
	}
}
