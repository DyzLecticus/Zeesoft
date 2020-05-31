package nl.zeesoft.zdk.database;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.collection.PartitionableCollection;
import nl.zeesoft.zdk.thread.CodeRunner;
import nl.zeesoft.zdk.thread.RunCode;

public class DatabaseIndex extends DatabaseStateObject {
	private DatabaseConfiguration			configuration		= null;
	
	private int								currentBlockNum		= 0;
	private int								blockPageCounter	= 0;
	private List<String>					addedClassNames		= new ArrayList<String>();
	private SortedMap<Str,IndexElement>		elementsById		= new TreeMap<Str,IndexElement>();

	protected DatabaseIndex(DatabaseConfiguration configuration) {
		this.configuration = configuration;
	}
	
	protected List<String> getAddedClassNames() {
		lock.lock(this);
		List<String> r = new ArrayList<String>(addedClassNames);
		lock.unlock(this);
		return r;
	}
	
	protected IndexElement add(DatabaseObject dbObj, DatabaseBlock[] blocks) {
		lock.lock(this);
		if (!addedClassNames.contains(dbObj.getClass().getName())) {
			addedClassNames.add(dbObj.getClass().getName());
		}
		IndexElement r = new IndexElement();
		r.id = dbObj.getId();
		r.blockNum = currentBlockNum;
		elementsById.put(dbObj.getId(), r);
		blockPageCounter++;
		if (blockPageCounter>=configuration.getBlockPageSize()) {
			currentBlockNum = getSmallestBlockNoLock(blocks);
			blockPageCounter = 0;
		}
		setChangedNoLock();
		lock.unlock(this);
		return r;
	}
	
	protected IndexElement remove(DatabaseObject dbObj) {
		lock.lock(this);
		IndexElement r = elementsById.remove(dbObj.getId());
		setChangedNoLock();
		lock.unlock(this);
		return r;
	}
	
	protected int size() {
		lock.lock(this);
		int r = elementsById.size();
		lock.unlock(this);
		return r;
	}
	
	protected void clear() {
		lock.lock(this);
		clearNoLock();
		lock.unlock(this);
	}

	protected List<Str> getIds() {
		lock.lock(this);
		List<Str> r = new ArrayList<Str>(elementsById.keySet());
		lock.unlock(this);
		return r;
	}

	protected IndexElement get(Str id) {
		lock.lock(this);
		IndexElement r = elementsById.get(id);
		lock.unlock(this);
		return r;
	}

	protected List<IndexElement> getElements() {
		lock.lock(this);
		List<IndexElement> r = new ArrayList<IndexElement>(elementsById.values());
		lock.unlock(this);
		return r;
	}
		
	protected Str save(boolean force, int minDiffMs) {
		Str error = new Str();
		if (force || isChanged(minDiffMs)) {
			lock.lock(this);
			List<Object> objects = new ArrayList<Object>(elementsById.values());
			List<String> classNames = new ArrayList<String>(addedClassNames);
			setSavedNoLock();
			lock.unlock(this);
			PartitionableCollection index = new PartitionableCollection();
			index.setPartitionSize(configuration.getIndexPartitionSize());
			index.setTimeoutMs(configuration.getSaveIndexTimeoutMs());
			index.putAll(objects);
			index.addClassNames(classNames);
			error = index.toPath(configuration.getIndexPath());
		}
		return error;
	}
	
	protected CodeRunner triggerLoad(DatabaseCollection collection) {
		CodeRunner r = null;
		lock.lock(this);
		boolean load = isNotLoadedAndNotLoadingNoLock();
		lock.unlock(this);
		if (load) {
			RunCode code = new RunCode(collection) {
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

	protected List<IndexElement> load() {
		List<IndexElement> r = new ArrayList<IndexElement>(); 
		lock.lock(this);
		boolean load = isNotLoadedAndNotLoadingNoLock();
		if (load) {
			setLoadingNoLock(true);
		}
		lock.unlock(this);
		if (load) {
			configuration.debug(this,new Str("Loading index ..."));
			PartitionableCollection index = new PartitionableCollection();
			index.setPartitionSize(configuration.getIndexPartitionSize());
			index.setTimeoutMs(configuration.getLoadIndexTimeoutMs());
			Str error = index.fromPath(configuration.getIndexPath());
			if (error.length()==0) {
				List<Object> objects = new ArrayList<Object>(index.getObjects().values());
				for (Object object: objects) {
					if (object instanceof IndexElement) {
						r.add((IndexElement) object);
					}
				}
				lock.lock(this);
				clearNoLock();
				for (IndexElement element: r) {
					String className = element.id.split(DatabaseCollection.ID_CONCATENATOR).get(0).toString();
					if (!addedClassNames.contains(className)) {
						addedClassNames.add(className);
					}
					elementsById.put(element.id, element);
				}
				setLoadedNoLock(true);
				setLoadingNoLock(false);
				lock.unlock(this);
				Str msg = new Str("Loaded index: ");
				msg.sb().append(r.size());
				configuration.debug(this,msg);
			} else {
				error.sb().insert(0, "Failed to load index: ");
				configuration.error(this,error);
			}
		}
		return r;
	}
	
	protected void clearNoLock() {
		elementsById.clear();
		addedClassNames.clear();
		setChangedNoLock();
	}
	
	protected int getSmallestBlockNoLock(DatabaseBlock[] blocks) {
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
}
