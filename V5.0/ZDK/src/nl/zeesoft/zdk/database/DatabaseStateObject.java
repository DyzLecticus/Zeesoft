package nl.zeesoft.zdk.database;

import nl.zeesoft.zdk.thread.Lock;

public class DatabaseStateObject {
	protected final Lock			lock			= new Lock();
	
	private boolean					loaded			= false;
	private boolean					loading 		= false;
	private long					saved 			= 0L;
	private long					changed 		= 0L;

	protected void setSaved() {
		lock.lock(this);
		setSavedNoLock();
		lock.unlock(this);
	}
	
	protected boolean isChanged(int minDiffMs) {
		lock.lock(this);
		boolean r = changed > saved && 
			(minDiffMs==0 || System.currentTimeMillis() - changed > minDiffMs);
		lock.unlock(this);
		return r;
	}

	protected void setChanged() {
		lock.lock(this);
		setChangedNoLock();
		lock.unlock(this);
	}

	protected boolean isNotLoadedAndNotLoadingNoLock() {
		return !loaded && !loading;
	}

	protected void setLoadingNoLock(boolean loading) {
		this.loading = loading;
	}

	protected void setLoadedNoLock(boolean loaded) {
		this.loaded = loaded;
	}
	
	protected void setChangedNoLock() {
		this.changed = System.currentTimeMillis();
	}

	protected void setSavedNoLock() {
		this.saved = System.currentTimeMillis();
	}
}
