package nl.zeesoft.zdk.database;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.collection.PersistableCollection;
import nl.zeesoft.zdk.collection.Query;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.RunCode;
import nl.zeesoft.zdk.thread.Waiter;

public class DatabaseCollection extends PersistableCollection {
	protected DatabaseConfiguration		configuration	= null;
	
	protected DatabaseIndex				index			= null;
	protected DatabaseBlock[]			blocks			= null;
	
	public DatabaseCollection(DatabaseConfiguration configuration) {
		super(configuration.getLogger());
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
		CodeRunnerChain runnerChain = getLoadAllBlocksChain();
		Waiter.startAndWaitTillDone(runnerChain, configuration.getLoadAllBlocksTimeoutMs());
		return super.getObjects();
	}
	
	@Override
	public Query query(Query query) {
		CodeRunnerChain runnerChain = getLoadAllBlocksChain();
		Waiter.startAndWaitTillDone(runnerChain, configuration.getLoadAllBlocksTimeoutMs());
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
	
	protected CodeRunnerChain getSaveIndexChain(boolean force, int minDiffMs) {
		lock.lock(this);
		CodeRunnerChain r = index.getCodeRunnerChainForSave(force, minDiffMs, nextId);
		lock.unlock(this);
		return r;
	}
	
	protected CodeRunnerChain getLoadIndexChain() {
		CodeRunnerChain r = index.getCodeRunnerChainForLoad();
		if (r!=null) {
			clearNoLock();
			RunCode code = new RunCode() {
				@Override
				protected boolean run() {
					loadedIndex();
					return true;
				}
			};
			r.add(code);
		}
		return r;
	}
	
	protected void loadedIndex() {
		lock.lock(this);
		List<IndexElement> elements = index.getElements();
		for (IndexElement element: elements) {
			blocks[element.blockNum].add(element);
		}
		for (DatabaseBlock block: blocks) {
			block.setSaved();
		}
		nextId = index.getLoadedNextId();
		lock.unlock(this);
	}
	
	protected Str saveBlock(int blockNum, boolean force, int minDiffMs) {
		Str error = new Str();
		SortedMap<Str,Object> dbObjs = new TreeMap<Str,Object>();
		boolean save = force || blocks[blockNum].isChanged(minDiffMs);
		if (save) {
			lock.lock(this);
			List<IndexElement> elements = blocks[blockNum].getElements();
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
			if (dbObjs.size()>0) {
				PersistableCollection block = new PersistableCollection(configuration.getLogger());
				block.putAll(dbObjs);
				error = block.toPath(configuration.getDataBlockFilePath(blockNum));
			} else {
				File file = new File(configuration.getDataBlockFilePath(blockNum));
				if (file.exists()) {
					file.delete();
				}
			}
		}
		return error;
	}

	protected CodeRunnerChain getSaveAllBlocksChain(boolean force, int minDiffMs) {
		CodeRunnerChain r = new CodeRunnerChain();
		List<RunCode> codes = new ArrayList<RunCode>();
		for (int i = 0; i < configuration.getNumberOfDataBlocks(); i++) {
			RunCode code = new RunCode(i) {
				@Override
				protected boolean run() {
					int blockNum = (int) params[0];
					Str error = saveBlock(blockNum,force,minDiffMs);
					if (error.length()>0) {
						configuration.error(blocks[blockNum], error);
					}
					return true;
				}
			};
			codes.add(code);
		}
		r.addAll(codes);
		return r;
	}

	protected CodeRunnerChain getLoadAllBlocksChain() {
		CodeRunnerChain r = new CodeRunnerChain();
		List<RunCode> codes = new ArrayList<RunCode>();
		for (int i = 0; i < configuration.getNumberOfDataBlocks(); i++) {
			RunCode code = new RunCode(i) {
				@Override
				protected boolean run() {
					int blockNum = (int) params[0];
					List<DatabaseObject> dbObjs = blocks[blockNum].load();
					for (DatabaseObject dbObj: dbObjs) {
						put(dbObj.getId(),dbObj);
					}
					return true;
				}
			};
			codes.add(code);
		}
		r.addAll(codes);
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
