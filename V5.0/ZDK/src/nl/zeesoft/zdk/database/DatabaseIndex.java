package nl.zeesoft.zdk.database;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.RunCode;

public class DatabaseIndex extends DatabaseStateObject {
	private long							loadedNextId		= 0;
	
	private int								currentBlockNum		= 0;
	private int								blockPageCounter	= 0;
	
	private List<String>					addedClassNames		= new ArrayList<String>();
	private SortedMap<Str,IndexElement>		elementsById		= new TreeMap<Str,IndexElement>();

	protected DatabaseIndex(DatabaseConfiguration configuration) {
		super(configuration);
		blockPageCounter = configuration.getBlockPageSize();
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
		blockPageCounter++;
		if (blockPageCounter>=configuration.getBlockPageSize()) {
			currentBlockNum = getSmallestBlockNoLock(blocks);
			blockPageCounter = 0;
		}
		IndexElement r = new IndexElement();
		r.id = dbObj.getId();
		r.blockNum = currentBlockNum;
		elementsById.put(dbObj.getId(), r);
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
		List<Str> r = new ArrayList<Str>();
		lock.lock(this);
		List<Str> ids = new ArrayList<Str>(elementsById.keySet());
		for (Str id: ids) {
			r.add(new Str(id));
		}
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
	
	protected CodeRunnerChain getCodeRunnerChainForSave(boolean force, int minDiffMs, long nextId) {
		CodeRunnerChain r = null;
		if (force || isChanged(minDiffMs)) {
			lock.lock(this);
			List<Object> list = new ArrayList<Object>(elementsById.values());
			List<String> classNames = new ArrayList<String>(addedClassNames);
			SortedMap<Str,Object> objects = new TreeMap<Str,Object>();
			long uid = 0;
			for (Object object: list) {
				objects.put(DatabaseIndexCollection.getIdForObject(object,uid),object);
				uid++;
			}
			setSavedNoLock();
			lock.unlock(this);
			DatabaseIndexCollection index = new DatabaseIndexCollection(configuration.getLogger());
			index.setPartitionSize(configuration.getIndexPartitionSize());
			index.putAll(objects);
			index.addClassNames(classNames);
			index.setNextId(nextId);
			r = index.getCodeRunnerChainForSave(configuration.getIndexPath());
		}
		return r;
	}
	
	protected CodeRunnerChain getCodeRunnerChainForLoad() {
		CodeRunnerChain r = null;
		lock.lock(this);
		boolean load = isNotLoadedAndNotLoadingNoLock();
		if (load) {
			setLoadingNoLock(true);
		}
		lock.unlock(this);
		if (load) {
			DatabaseIndexCollection index = new DatabaseIndexCollection(configuration.getLogger());
			index.setPartitionSize(configuration.getIndexPartitionSize());
			r = index.getCodeRunnerChainForLoad(configuration.getIndexPath());
			RunCode code = new RunCode() {
				@Override
				protected boolean run() {
					loadedIndex(index);
					return true;
				}
			};
			r.add(code);
		}
		return r;
	}
	
	protected void loadedIndex(DatabaseIndexCollection index) {
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
			String className = element.id.split(DatabaseCollection.ID_CONCATENATOR).get(0).toString();
			if (!addedClassNames.contains(className)) {
				addedClassNames.add(className);
			}
			elementsById.put(element.id, element);
		}
		setLoadedNoLock(true);
		setLoadingNoLock(false);
		loadedNextId = index.getNextId();
		lock.unlock(this);
	}

	protected long getLoadedNextId() {
		lock.lock(this);
		long r = loadedNextId;
		lock.unlock(this);
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
