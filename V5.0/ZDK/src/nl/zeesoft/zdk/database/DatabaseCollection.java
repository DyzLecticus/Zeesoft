package nl.zeesoft.zdk.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.collection.PersistableCollection;
import nl.zeesoft.zdk.thread.CodeRunner;
import nl.zeesoft.zdk.thread.RunCode;

public class DatabaseCollection extends PersistableCollection {
	protected DatabaseConfiguration					configuration		= null;
	
	protected List<String>							addedClassNames		= new ArrayList<String>();
	
	protected SortedMap<Str,IndexElement>			elementsById		= new TreeMap<Str,IndexElement>();
	protected SortedMap<Integer,List<IndexElement>>	elementsByBlockNum	= new TreeMap<Integer,List<IndexElement>>();
	
	protected boolean								changedIndex		= false;
	protected boolean								loadedIndex			= false;
	protected boolean[]								loadedBlocks		= null;
	protected List<Integer>							changedBlockNums	= new ArrayList<Integer>();
	
	public DatabaseCollection() {
		this.configuration = new DatabaseConfiguration();
		initialize();
	}
	
	public DatabaseCollection(DatabaseConfiguration configuration) {
		this.configuration = configuration.copy();
		initialize();
	}

	@Override
	public Str put(Str id,Object object) {
		Str r = null;
		if (object instanceof DatabaseObject) {
			r = super.put(id, ((DatabaseObject)object).copy());
		}
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
	public Object get(Str id) {
		Object r = null;
		lock.lock(this);
		IndexElement element = elementsById.get(id);
		if (element!=null) {
			if (!loadedBlock(element.blockNum)) {
				triggerLoadBlock(element.blockNum,true,configuration.getLoadBlockTimeoutMs());
				if (loadedBlock(element.blockNum)) {
					r = getObjectNoLock(id);
				}
			}
		}
		lock.unlock(this);
		return r;
	}
	
	@Override
	public SortedMap<Str,Object> getObjects() {
		if (!loadedAllBlocks()) {
			loadAllBlocks(true, configuration.getLoadAllBlocksTimeoutMs());
		}
		return super.getObjects();
	}
	
	@Override
	public Str putNoLock(Str id, Object object) {
		Str r = super.putNoLock(id, object);
		if (r!=null && object instanceof DatabaseObject) {
			DatabaseObject dbObj = (DatabaseObject) object;
			IndexElement element = elementsById.get(dbObj.getId());
			if (element!=null) {
				elementsByBlockNum.get(element.blockNum).add(element);
				if (!changedBlockNums.contains(element.blockNum)) {
					changedBlockNums.add(element.blockNum);
				}
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
				addObjectToIndexNoLock(dbObj);
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
		for (List<IndexElement> block: elementsByBlockNum.values()) {
			block.clear();
		}
		changedIndex = true;
		changedBlockNums.clear();
		for (int i = 0; i < configuration.getNumBlocks(); i++) {
			changedBlockNums.add(i);
		}
	}

	@Override
	protected Object getObjectNoLock(Str id) {
		Object r = super.getObjectNoLock(id);
		if (r!=null && r instanceof DatabaseObject) {
			r = ((DatabaseObject)r).copy();
		}
		return r;
	}
	
	protected Str saveIndex(boolean force) {
		Str error = new Str();
		if (changedIndex || force) {
			lock.lock(this);
			SortedMap<Str,IndexElement> elemsById = new TreeMap<Str,IndexElement>(elementsById);
			List<String> classNames = new ArrayList<String>(addedClassNames);
			lock.unlock(this);
			PersistableCollection index = new PersistableCollection();
			List<Object> objects = new ArrayList<Object>(elemsById.values());
			index.putAll(objects);
			index.addClassNames(classNames);
			error = index.toFile(configuration.getIndexFilePath());
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
			PersistableCollection index = new PersistableCollection();
			error = index.fromFile(configuration.getIndexFilePath());
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
					elementsById.put(element.id, element);
					elementsByBlockNum.get(element.blockNum).add(element);
				}
				changedIndex = false;
				changedBlockNums.clear();
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
	
	protected boolean triggerLoadIndex(boolean wait, int waitMs) {
		boolean r = false;
		if (!loadedIndex()) {
			r = true;
			RunCode code = new RunCode() {
				@Override
				protected boolean run() {
					DatabaseCollection collection = (DatabaseCollection) params[0];
					collection.loadIndex();
					return true;
				}
				
			};
			code.params[0] = this;
			CodeRunner runner = new CodeRunner(code);
			runner.start();
			if (wait) {
				wait(getWaitWhileIndexNotLoadedCode(), waitMs);
			}
		}
		return r;
	}
	
	protected Str saveBlock(int blockNum, boolean force) {
		Str error = new Str();
		lock.lock(this);
		boolean save = force || changedBlockNums.contains(blockNum);
		SortedMap<Str,Object> dbObjs = new TreeMap<Str,Object>();
		List<IndexElement> elements = elementsByBlockNum.get(blockNum);
		for (IndexElement element: elements) {
			Object object = objects.get(element.id);
			if (object instanceof DatabaseObject && classNames.contains(object.getClass().getName())) {
				DatabaseObject dbObj = (DatabaseObject) object;
				dbObjs.put(element.id,dbObj.copy());
			}
		}
		lock.unlock(this);
		if (save) {
			PersistableCollection block = new PersistableCollection();
			block.putAll(dbObjs);
			error = block.toFile(configuration.getDataBlockFilePath(blockNum));
		}
		return error;
	}
	
	protected Str saveAllBlocks(boolean force) {
		Str error = new Str();
		configuration.debug(this,new Str("Saving all blocks ..."));
		for (int i = 0; i < configuration.getNumBlocks(); i++) {
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

	protected boolean loadedBlock(int blockNum) {
		lock.lock(this);
		boolean r = loadedBlocks[blockNum];
		lock.unlock(this);
		return r;
	}
	
	protected Str loadBlock(int blockNum) {
		Str error = new Str();
		if (!loadedBlock(blockNum)) {
			PersistableCollection block = new PersistableCollection();
			error = block.fromFile(configuration.getDataBlockFilePath(blockNum));
			if (error.length()==0) {
				for (Object object: block.getObjects().values()) {
					if (object instanceof DatabaseObject) {
						DatabaseObject dbObj = (DatabaseObject) object;
						put(dbObj.getId(),object);
					}
				}
				lock.lock(this);
				loadedBlocks[blockNum] = true;
				lock.unlock(this);
			}
		}
		return error;
	}

	protected boolean loadedAllBlocks() {
		boolean r = true;
		for (int i = 0; i < configuration.getNumBlocks(); i++) {
			r = loadedBlock(i);
			if (!r) {
				break;
			}
		}
		return r;
	}
	
	protected void loadAllBlocks(boolean wait, int waitMs) {
		if (!loadedAllBlocks()) {
			configuration.debug(this,new Str("Loading all blocks..."));
			boolean triggered = false;
			for (int i = 0; i < configuration.getNumBlocks(); i++) {
				if (triggerLoadBlock(i, false, 0)) {
					triggered = true;
				}
			}
			if (triggered && wait) {
				wait(getWaitWhileBlocksNotAllLoadedCode(), waitMs);
				configuration.debug(this,new Str("Loaded all blocks"));
			}
		}
	}
	
	protected boolean triggerLoadBlock(int blockNum, boolean wait, int waitMs) {
		boolean r = false;
		if (!loadedBlock(blockNum)) {
			r = true;
			RunCode code = new RunCode() {
				@Override
				protected boolean run() {
					DatabaseCollection collection = (DatabaseCollection) params[0];
					int blockNum = (int) params[1];
					collection.loadBlock(blockNum);
					return true;
				}
				
			};
			code.params = new Object[2];
			code.params[0] = this;
			code.params[1] = blockNum;
			CodeRunner runner = new CodeRunner(code);
			runner.start();
			if (wait) {
				wait(getWaitWhileBlockNotLoadedCode(blockNum), waitMs);
			}
		}
		return r;
	}
	
	protected void addObjectToIndexNoLock(DatabaseObject dbObj) {
		if (!addedClassNames.contains(dbObj.getClass().getName())) {
			addedClassNames.add(dbObj.getClass().getName());
		}
		IndexElement element = new IndexElement();
		element.id = dbObj.getId();
		element.blockNum = getSmallestBlockNoLock();
		elementsById.put(dbObj.getId(), element);
		changedIndex = true;
	}
	
	protected void removeObjectFromIndexNoLock(DatabaseObject dbObj) {
		IndexElement element = elementsById.remove(dbObj.getId());
		if (element!=null) {
			elementsByBlockNum.get(element.blockNum).remove(element);
			if (!changedBlockNums.contains(element.blockNum)) {
				changedBlockNums.add(element.blockNum);
			}
			changedIndex = true;
		}
	}
	
	private int getSmallestBlockNoLock() {
		int r = -1;
		int min = Integer.MAX_VALUE;
		for (Entry<Integer,List<IndexElement>> entry: elementsByBlockNum.entrySet()) {
			if (r==-1 || entry.getValue().size()<min) {
				r = entry.getKey();
				min = entry.getValue().size();
			}
		}
		if (r==-1) {
			r = 0;
		}
		return r;
	}
	
	private void initialize() {
		loadedBlocks = new boolean[configuration.getNumBlocks()];
		for (int i = 0; i < configuration.getNumBlocks(); i++) {
			loadedBlocks[i] = false;
			elementsByBlockNum.put(i, new ArrayList<IndexElement>());
		}
	}
	
	private void wait(RunCode whileCode, int waitMs) {
		int sleepMs = 1;
		int waitedMs = 0;
		while(whileCode.tryRunCatch() && waitedMs < waitMs) {
			try {
				Thread.sleep(sleepMs);
				waitedMs += sleepMs;
				if (waitedMs >= 100) {
					sleepMs = 50;
				} else if (waitedMs >= 10) {
					sleepMs = 10;
				}
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	private RunCode getWaitWhileIndexNotLoadedCode() {
		RunCode r = new RunCode(this) {
			@Override
			protected boolean run() {
				DatabaseCollection collection = (DatabaseCollection) params[0];
				return !collection.loadedIndex();
			}
		};
		return r;
	}
	
	private RunCode getWaitWhileBlockNotLoadedCode(int blockNum) {
		RunCode r = new RunCode(this, blockNum) {
			@Override
			protected boolean run() {
				DatabaseCollection collection = (DatabaseCollection) params[0];
				int blockNum = (int) params[1];
				return !collection.loadedBlock(blockNum);
			}
		};
		return r;
	}
	
	private RunCode getWaitWhileBlocksNotAllLoadedCode() {
		RunCode r = new RunCode(this) {
			@Override
			protected boolean run() {
				DatabaseCollection collection = (DatabaseCollection) params[0];
				return !collection.loadedAllBlocks();
			}
		};
		return r;
	}
}
