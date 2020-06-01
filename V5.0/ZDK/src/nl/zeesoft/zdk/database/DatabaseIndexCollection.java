package nl.zeesoft.zdk.database;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.collection.PartitionableCollection;
import nl.zeesoft.zdk.thread.CodeRunnerChain;

public class DatabaseIndexCollection extends PartitionableCollection {
	public DatabaseIndexCollection() {
		
	}
	
	public DatabaseIndexCollection(Logger logger) {
		this.logger = logger;
	}
	
	protected void setNextId(long nextId) {
		lock.lock(this);
		this.nextId = nextId;
		lock.unlock(this);
	}
	
	protected long getNextId() {
		lock.lock(this);
		long r = nextId;
		lock.unlock(this);
		return r;
	}

	@Override
	protected CodeRunnerChain getCodeRunnerChainForSave(String path) {
		return super.getCodeRunnerChainForSave(path);
	}

	@Override
	protected CodeRunnerChain getCodeRunnerChainForLoad(String path) {
		return super.getCodeRunnerChainForLoad(path);
	}
}
