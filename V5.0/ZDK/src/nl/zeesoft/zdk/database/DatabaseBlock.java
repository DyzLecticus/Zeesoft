package nl.zeesoft.zdk.database;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.collection.PersistableCollection;
import nl.zeesoft.zdk.thread.CodeRunner;
import nl.zeesoft.zdk.thread.Lock;
import nl.zeesoft.zdk.thread.RunCode;

public class DatabaseBlock {
	private Lock					lock			= new Lock();
	
	private DatabaseConfiguration	configuration	= null;
	private int						blockNum		= 0;
	
	private List<IndexElement>		elements		= new ArrayList<IndexElement>();
	
	private boolean					loaded			= false;
	private boolean					loading 		= false;
	
	private long					saved 			= 0L;
	private long					changed 		= 0L;
	
	protected DatabaseBlock(DatabaseConfiguration configuration, int blockNum) {
		this.configuration = configuration;
		this.blockNum = blockNum;
	}
	
	protected int getBlockNum() {
		return blockNum;
	}
	
	public void add(IndexElement element) {
		lock.lock(this);
		elements.add(element);
		changed = System.currentTimeMillis();
		lock.unlock(this);
	}
	
	public void remove(IndexElement element) {
		lock.lock(this);
		elements.remove(element);
		changed = System.currentTimeMillis();
		lock.unlock(this);
	}
	
	public int size() {
		lock.lock(this);
		int r = elements.size();
		lock.unlock(this);
		return r;
	}
	
	public void clear() {
		lock.lock(this);
		elements.clear();
		changed = System.currentTimeMillis();
		lock.unlock(this);
	}

	public List<IndexElement> getElements() {
		lock.lock(this);
		List<IndexElement> r = new ArrayList<IndexElement>(elements);
		lock.unlock(this);
		return r;
	}
	
	protected boolean isLoaded() {
		lock.lock(this);
		boolean r = loaded;
		lock.unlock(this);
		return r;
	}

	protected void setLoaded(boolean loaded) {
		lock.lock(this);
		this.loaded = loaded;
		lock.unlock(this);
	}
	
	protected boolean isLoading() {
		lock.lock(this);
		boolean r = loading;
		lock.unlock(this);
		return r;
	}

	protected void setLoading(boolean loading) {
		lock.lock(this);
		this.loading = loading;
		lock.unlock(this);
	}

	protected long getSaved() {
		lock.lock(this);
		long r = saved;
		lock.unlock(this);
		return r;
	}

	protected void setSaved() {
		lock.lock(this);
		this.saved = System.currentTimeMillis();
		lock.unlock(this);
	}

	protected boolean isChanged() {
		lock.lock(this);
		boolean r = changed > saved;
		lock.unlock(this);
		return r;
	}

	protected void setChanged() {
		lock.lock(this);
		this.changed = System.currentTimeMillis();
		lock.unlock(this);
	}
	
	protected CodeRunner triggerLoadBlock(DatabaseCollection collection) {
		CodeRunner r = null;
		lock.lock(this);
		boolean trigger = (!loaded && !loading);
		lock.unlock(this);
		if (trigger) {
			RunCode code = new RunCode(this,collection) {
				@Override
				protected boolean run() {
					DatabaseBlock block = (DatabaseBlock) params[0];
					DatabaseCollection collection = (DatabaseCollection) params[1];
					List<DatabaseObject> dbObjs = block.loadBlock();
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
	
	protected List<DatabaseObject> loadBlock() {
		List<DatabaseObject> r = new ArrayList<DatabaseObject>();
		lock.lock(this);
		String fileName = configuration.getDataBlockFilePath(blockNum);
		File file = new File(fileName);
		boolean load = !loaded && !loading && file.exists();
		if (load) {
			loading = true;
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
			loaded = error.length() == 0;
			loading = false;
			lock.unlock(this);
		}
		return r;
	}
}
