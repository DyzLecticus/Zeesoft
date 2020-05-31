package nl.zeesoft.zdk.database;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.collection.PersistableCollection;
import nl.zeesoft.zdk.collection.Query;
import nl.zeesoft.zdk.thread.CodeRunner;
import nl.zeesoft.zdk.thread.Waiter;

public class DatabaseCollection extends PersistableCollection {
	protected DatabaseConfiguration					configuration		= null;
	
	protected DatabaseIndex							index				= null;
	protected DatabaseBlock[]						blocks				= null;
	
	public DatabaseCollection() {
		this.configuration = new DatabaseConfiguration();
		initializeDatabaseCollection();
	}
	
	public DatabaseCollection(DatabaseConfiguration configuration) {
		this.configuration = configuration.copy();
		initializeDatabaseCollection();
	}

	@Override
	public Str put(Str id,Object object) {
		Str r = null;
		if (object!=null) {
			if (object instanceof DatabaseObject) {
				r = super.put(id, ((DatabaseObject)object).copy());
			} else {
				Str error = new Str();
				error.sb().append("Database collection objects ");
				error.sb().append(object.getClass().getName());
				error.sb().append(" must extend " + DatabaseObject.class.getName());
				configuration.error(this, error);
			}
		}
		return r;
	}
	
	public List<String> getAddedClassNames() {
		return index.getAddedClassNames();
	}
	
	@Override
	public List<Str> getObjectIds() {
		return index.getIds();
	}
	
	@Override
	public SortedMap<Str,Object> getObjects() {
		List<CodeRunner> runners = triggerLoadAllBlocks();
		Waiter.waitTillDone(runners, configuration.getLoadAllBlocksTimeoutMs());
		return super.getObjects();
	}
	
	@Override
	public Query query(Query query) {
		List<CodeRunner> runners = triggerLoadAllBlocks();
		Waiter.waitTillDone(runners, configuration.getLoadAllBlocksTimeoutMs());
		return super.query(query);
	}
	
	@Override
	public Str putNoLock(Str id, Object object) {
		Str r = super.putNoLock(id, object);
		if (r!=null && object instanceof DatabaseObject) {
			DatabaseObject dbObj = (DatabaseObject) object;
			IndexElement element = index.get(dbObj.getId());
			if (element!=null) {
				blocks[element.blockNum].add(element);
			}
		}
		return r;
	}
	
	@Override
	protected void addedObjectNoLock(Str id, Object object) {
		super.addedObjectNoLock(id, object);
		if (object instanceof DatabaseObject) {
			DatabaseObject dbObj = (DatabaseObject) object;
			if (dbObj.getId().length()==0) {
				dbObj.setId(id);
				index.add(dbObj,blocks);
			}
		}
	}
	
	@Override
	protected void updatedObjectNoLock(Str id, Object object) {
		super.updatedObjectNoLock(id, object);
		if (object instanceof DatabaseObject) {
			DatabaseObject dbObj = (DatabaseObject) object;
			IndexElement element = index.get(dbObj.getId());
			if (element!=null) {
				blocks[element.blockNum].setChanged();
			}
		}
	}

	@Override
	protected Object removeNoLock(Str id) {
		Object r = super.removeNoLock(id);
		if (r!=null && r instanceof DatabaseObject) {
			DatabaseObject dbObj = (DatabaseObject) r;
			if (dbObj.getId().length()>0) {
				index.remove(dbObj);
			}
		}
		return r;
	}
	
	@Override
	protected void clearNoLock() {
		super.clearNoLock();
		index.clear();
		for (DatabaseBlock block: blocks) {
			block.clear();
		}
	}

	@Override
	protected Object getExternalObjectNoLock(Str id) {
		Object r = super.getExternalObjectNoLock(id);
		if (r!=null && r instanceof DatabaseObject) {
			r = ((DatabaseObject)r).copy();
		}
		return r;
	}

	@Override
	protected Object getInternalObjectNoLock(Str id) {
		Object r = null;
		IndexElement element = index.get(id);
		if (element!=null) {
			loadBlockNoLock(element.blockNum);
			r = super.getInternalObjectNoLock(id);
		}
		return r;
	}
	
	protected Str saveIndex(boolean force, int minDiffMs) {
		return index.save(force, minDiffMs);
	}
	
	protected void loadIndex() {
		lock.lock(this);
		clearNoLock();
		List<IndexElement> elements = index.load();
		for (IndexElement element: elements) {
			blocks[element.blockNum].add(element);
		}
		for (DatabaseBlock block: blocks) {
			block.setSaved();
		}
		lock.unlock(this);
	}
	
	protected CodeRunner triggerLoadIndex() {
		return index.triggerLoad(this);
	}
	
	protected Str saveBlock(int blockNum, boolean force, int minDiffMs) {
		Str error = new Str();
		SortedMap<Str,Object> dbObjs = new TreeMap<Str,Object>();
		boolean save = force || blocks[blockNum].isChanged(minDiffMs);
		if (save) {
			List<IndexElement> elements = blocks[blockNum].getElements();
			lock.lock(this);
			for (IndexElement element: elements) {
				Object object = getExternalObjectNoLock(element.id);
				if (object instanceof DatabaseObject) {
					DatabaseObject dbObj = (DatabaseObject) object;
					dbObjs.put(element.id,dbObj.copy());
				}
			}
			blocks[blockNum].setSaved();
			lock.unlock(this);
		}
		if (save) {
			PersistableCollection block = new PersistableCollection();
			block.putAll(dbObjs);
			error = block.toPath(configuration.getDataBlockFilePath(blockNum));
		}
		return error;
	}
	
	protected Str saveAllBlocks(boolean force, int minDiffMs) {
		Str error = new Str();
		configuration.debug(this,new Str("Saving all blocks ..."));
		for (int i = 0; i < configuration.getNumberOfDataBlocks(); i++) {
			error = saveBlock(i,force,minDiffMs);
			if (error.length()>0) {
				break;
			}
		}
		if (error.length()==0) {
			configuration.debug(this,new Str("Saved all blocks"));
		} else {
			error.sb().insert(0, "Failed to save all blocks: ");
			configuration.error(this,error);
		}
		return error;
	}
	
	protected List<CodeRunner> triggerLoadAllBlocks() {
		List<CodeRunner> r = new ArrayList<CodeRunner>();
		for (int i = 0; i < configuration.getNumberOfDataBlocks(); i++) {
			CodeRunner runner = blocks[i].triggerLoad(this);
			if (runner!=null) {
				r.add(runner);
			}
		}
		return r;
	}
	
	protected void loadBlockNoLock(int blockNum) {
		List<DatabaseObject> dbObjs = blocks[blockNum].load();
		for (DatabaseObject dbObj: dbObjs) {
			putNoLock(dbObj.getId(),dbObj);
		}
	}
	
	private void initializeDatabaseCollection() {
		logger = configuration.getLogger();
		index = new DatabaseIndex(configuration);
		blocks = new DatabaseBlock[configuration.getNumberOfDataBlocks()];
		for (int i = 0; i < configuration.getNumberOfDataBlocks(); i++) {
			blocks[i] = new DatabaseBlock(configuration, i);
		}
	}
}
