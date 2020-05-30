package nl.zeesoft.zdk.database;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.collection.PartitionableCollection;
import nl.zeesoft.zdk.collection.PersistableCollection;
import nl.zeesoft.zdk.collection.Query;
import nl.zeesoft.zdk.thread.CodeRunner;
import nl.zeesoft.zdk.thread.RunCode;
import nl.zeesoft.zdk.thread.Waiter;

public class DatabaseCollection extends PersistableCollection {
	protected DatabaseConfiguration					configuration		= null;
	
	protected List<String>							addedClassNames		= new ArrayList<String>();
	
	protected SortedMap<Str,IndexElement>			elementsById		= new TreeMap<Str,IndexElement>();
	protected int									currentBlockNum		= 0;
	
	protected boolean								changedIndex		= false;
	protected boolean								loadedIndex			= false;
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
			resetCurrentBlockNum();
			if (object instanceof DatabaseObject) {
				r = super.put(id, ((DatabaseObject)object).copy());
			} else {
				Str error = new Str();
				error.sb().append("Database object ");
				error.sb().append(object.getClass().getName());
				error.sb().append(" must extend " + DatabaseObject.class.getName());
				configuration.error(this, error);
			}
		}
		return r;
	}
	
	public List<String> getAddedClassNames() {
		lock.lock(this);
		List<String> r = new ArrayList<String>(addedClassNames);
		lock.unlock(this);
		return r;
	}
	
	@Override
	public List<Str> getObjectIds() {
		lock.lock(this);
		List<Str> r = new ArrayList<Str>(elementsById.keySet());
		lock.unlock(this);
		return r;
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
			IndexElement element = elementsById.get(dbObj.getId());
			if (element!=null) {
				blocks[element.blockNum].add(element);
			}
		}
		return r;
	}
	
	protected void resetCurrentBlockNum() {
		lock.lock(this);
		currentBlockNum = this.getSmallestBlockNoLock();
		lock.unlock(this);
	}
	
	@Override
	protected void addedObjectNoLock(Str id, Object object) {
		super.addedObjectNoLock(id, object);
		if (object instanceof DatabaseObject) {
			DatabaseObject dbObj = (DatabaseObject) object;
			if (dbObj.getId().length()==0) {
				dbObj.setId(id);
				addObjectToIndexNoLock(dbObj);
			}
		}
	}
	
	@Override
	protected void updatedObjectNoLock(Str id, Object object) {
		super.updatedObjectNoLock(id, object);
		if (object instanceof DatabaseObject) {
			DatabaseObject dbObj = (DatabaseObject) object;
			IndexElement element = elementsById.get(dbObj.getId());
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
				removeObjectFromIndexNoLock(dbObj);
			}
		}
		return r;
	}
	
	@Override
	protected void clearNoLock() {
		super.clearNoLock();
		addedClassNames.clear();
		elementsById.clear();
		changedIndex = true;
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
		IndexElement element = elementsById.get(id);
		if (element!=null) {
			loadBlockNoLock(element.blockNum);
			r = super.getInternalObjectNoLock(id);
		}
		return r;
	}
	
	protected Str saveIndex(boolean force) {
		Str error = new Str();
		lock.lock(this);
		boolean changed = changedIndex;
		lock.unlock(this);
		if (changed || force) {
			lock.lock(this);
			SortedMap<Str,IndexElement> elemsById = new TreeMap<Str,IndexElement>(elementsById);
			List<String> classNames = new ArrayList<String>(addedClassNames);
			lock.unlock(this);
			PartitionableCollection index = new PartitionableCollection();
			index.setPartitionSize(configuration.getIndexPartitionSize());
			index.setTimeoutMs(configuration.getSaveIndexTimeoutMs());
			List<Object> objects = new ArrayList<Object>(elemsById.values());
			index.putAll(objects);
			index.addClassNames(classNames);
			error = index.toPath(configuration.getIndexPath());
		}
		return error;
	}

	protected boolean loadedIndex() {
		lock.lock(this);
		boolean r = loadedIndex;
		lock.unlock(this);
		return r;
	}
	
	protected Str loadIndex() {
		Str error = new Str();
		if (!loadedIndex()) {
			configuration.debug(this,new Str("Loading index ..."));
			PartitionableCollection index = new PartitionableCollection();
			index.setPartitionSize(configuration.getIndexPartitionSize());
			index.setTimeoutMs(configuration.getLoadIndexTimeoutMs());
			error = index.fromPath(configuration.getIndexPath());
			if (error.length()==0) {
				List<IndexElement> elements = new ArrayList<IndexElement>();
				List<Object> objects = new ArrayList<Object>(index.getObjects().values());
				for (Object object: objects) {
					if (object instanceof IndexElement) {
						elements.add((IndexElement) object);
					}
				}
				lock.lock(this);
				clearNoLock();
				for (IndexElement element: elements) {
					String className = element.id.split(ID_CONCATENATOR).get(0).toString();
					if (!addedClassNames.contains(className)) {
						addedClassNames.add(className);
					}
					elementsById.put(element.id, element);
					blocks[element.blockNum].add(element);
				}
				for (DatabaseBlock block: blocks) {
					block.setSaved();
				}
				changedIndex = false;
				loadedIndex = true;
				lock.unlock(this);
				Str msg = new Str("Loaded index: ");
				msg.sb().append(elements.size());
				configuration.debug(this,msg);
			} else {
				error.sb().insert(0, "Failed to load index: ");
				configuration.error(this,error);
			}
		}
		return error;
	}
	
	protected CodeRunner triggerLoadIndex() {
		CodeRunner r = null;
		if (!loadedIndex()) {
			RunCode code = new RunCode(this) {
				@Override
				protected boolean run() {
					DatabaseCollection collection = (DatabaseCollection) params[0];
					collection.loadIndex();
					return true;
				}
				
			};
			r = CodeRunner.startNewCodeRunner(code);
		}
		return r;
	}
	
	protected Str saveBlock(int blockNum, boolean force) {
		Str error = new Str();
		lock.lock(this);
		SortedMap<Str,Object> dbObjs = new TreeMap<Str,Object>();
		boolean save = force || blocks[blockNum].isChanged();
		if (save) {
			List<IndexElement> elements = blocks[blockNum].getElements();
			for (IndexElement element: elements) {
				Object object = getInternalObjectNoLock(element.id);
				if (object instanceof DatabaseObject) {
					DatabaseObject dbObj = (DatabaseObject) object;
					dbObjs.put(element.id,dbObj.copy());
				}
			}
		}
		lock.unlock(this);
		if (save) {
			PersistableCollection block = new PersistableCollection();
			block.putAll(dbObjs);
			error = block.toPath(configuration.getDataBlockFilePath(blockNum));
			blocks[blockNum].setSaved();
		}
		return error;
	}
	
	protected Str saveAllBlocks(boolean force) {
		Str error = new Str();
		configuration.debug(this,new Str("Saving all blocks ..."));
		for (int i = 0; i < configuration.getNumberOfDataBlocks(); i++) {
			error = saveBlock(i,force);
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
			CodeRunner runner = blocks[i].triggerLoadBlock(this);
			if (runner!=null) {
				r.add(runner);
			}
		}
		return r;
	}
	
	protected void loadBlockNoLock(int blockNum) {
		DatabaseBlock block = blocks[blockNum];
		List<DatabaseObject> dbObjs = block.loadBlock();
		for (DatabaseObject dbObj: dbObjs) {
			putNoLock(dbObj.getId(),dbObj);
		}
	}
	
	protected void addObjectToIndexNoLock(DatabaseObject dbObj) {
		if (!addedClassNames.contains(dbObj.getClass().getName())) {
			addedClassNames.add(dbObj.getClass().getName());
		}
		IndexElement element = new IndexElement();
		element.id = dbObj.getId();
		element.blockNum = currentBlockNum;
		elementsById.put(dbObj.getId(), element);
		changedIndex = true;
	}
	
	protected void removeObjectFromIndexNoLock(DatabaseObject dbObj) {
		IndexElement element = elementsById.remove(dbObj.getId());
		if (element!=null) {
			blocks[element.blockNum].remove(element);
			changedIndex = true;
		}
	}
	
	private int getSmallestBlockNoLock() {
		int r = -1;
		int min = Integer.MAX_VALUE;
		for (DatabaseBlock block: blocks) {
			int size = block.size();
			if (r==-1 || size<min) {
				r = block.getBlockNum();
				min = size;
			}
		}
		if (r==-1) {
			r = 0;
		}
		return r;
	}
	
	private void initializeDatabaseCollection() {
		logger = configuration.getLogger();
		blocks = new DatabaseBlock[configuration.getNumberOfDataBlocks()];
		for (int i = 0; i < configuration.getNumberOfDataBlocks(); i++) {
			blocks[i] = new DatabaseBlock(configuration, i);
		}
	}
}
