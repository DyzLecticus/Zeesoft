package nl.zeesoft.zdk.database;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.collection.PersistableCollection;
import nl.zeesoft.zdk.thread.CodeRunner;
import nl.zeesoft.zdk.thread.RunCode;

public class DatabaseBlock extends DatabaseStateObject {
	private DatabaseConfiguration	configuration	= null;
	private int						blockNum		= 0;
	
	private List<IndexElement>		elements		= new ArrayList<IndexElement>();
	
	protected DatabaseBlock(DatabaseConfiguration configuration, int blockNum) {
		this.configuration = configuration;
		this.blockNum = blockNum;
	}
	
	protected int getBlockNum() {
		return blockNum;
	}
	
	protected void add(IndexElement element) {
		lock.lock(this);
		elements.add(element);
		setChangedNoLock();
		lock.unlock(this);
	}
	
	protected void remove(IndexElement element) {
		lock.lock(this);
		elements.remove(element);
		setChangedNoLock();
		lock.unlock(this);
	}
	
	protected int size() {
		lock.lock(this);
		int r = elements.size();
		lock.unlock(this);
		return r;
	}
	
	protected void clear() {
		lock.lock(this);
		elements.clear();
		setChangedNoLock();
		lock.unlock(this);
	}

	protected List<IndexElement> getElements() {
		lock.lock(this);
		List<IndexElement> r = new ArrayList<IndexElement>(elements);
		lock.unlock(this);
		return r;
	}
	
	protected CodeRunner triggerLoad(DatabaseCollection collection) {
		CodeRunner r = null;
		lock.lock(this);
		File file = new File(configuration.getDataBlockFilePath(blockNum));
		boolean trigger = isNotLoadedAndNotLoadingNoLock() && file.exists();
		lock.unlock(this);
		if (trigger) {
			RunCode code = new RunCode(this,collection) {
				@Override
				protected boolean run() {
					DatabaseBlock block = (DatabaseBlock) params[0];
					DatabaseCollection collection = (DatabaseCollection) params[1];
					List<DatabaseObject> dbObjs = block.load();
					for (DatabaseObject dbObj: dbObjs) {
						collection.put(dbObj.getId(),dbObj);
					}
					return true;
				}
				
			};
			r = CodeRunner.startNewCodeRunner(code);
		}
		return r;
	}
	
	protected List<DatabaseObject> load() {
		List<DatabaseObject> r = new ArrayList<DatabaseObject>();
		String fileName = configuration.getDataBlockFilePath(blockNum);
		lock.lock(this);
		File file = new File(fileName);
		boolean load = isNotLoadedAndNotLoadingNoLock() && file.exists();
		if (load) {
			setLoadingNoLock(true);
		}
		lock.unlock(this);
		if (load) {
			PersistableCollection block = new PersistableCollection();
			Str error = block.fromPath(fileName);
			if (error.length()==0) {
				for (Object object: block.getObjects().values()) {
					if (object instanceof DatabaseObject) {
						DatabaseObject dbObj = (DatabaseObject) object;
						r.add(dbObj);
					}
				}
			} else {
				configuration.error(this,error);
			}
			lock.lock(this);
			setLoadedNoLock(error.length() == 0);
			setLoadingNoLock(false);
			lock.unlock(this);
		}
		return r;
	}
}
