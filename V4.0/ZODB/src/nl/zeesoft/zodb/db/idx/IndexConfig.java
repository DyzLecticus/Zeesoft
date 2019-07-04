package nl.zeesoft.zodb.db.idx;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsAble;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.LockedCode;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zodb.db.IndexElement;

public class IndexConfig extends Locker implements JsAble {
	protected static final String	PFX_OBJ			= "@OBJECT";
	
	public static final String		IDX_NAME		= PFX_OBJ + ":name";
	public static final String		IDX_MODIFIED	= PFX_OBJ + ":modified";
	
	private boolean					rebuild			= false;
	private List<SearchIndex>		indexes 		= new ArrayList<SearchIndex>();
	private List<SearchIndex>		objectIndexes	= new ArrayList<SearchIndex>();
	
	public IndexConfig(Messenger msgr) {
		super(msgr);
	}

	public void initialize() {
		LockedCode code = new LockedCode() {
			@Override
			public Object doLocked() {
				if (getIndexNoLock(IDX_NAME)==null) {
					addIndexNoLock(PFX_OBJ,"name",false,true);
				}
				if (getIndexNoLock(IDX_MODIFIED)==null) {
					addIndexNoLock(PFX_OBJ,"modified",true,false);
				}
				objectIndexes = new ArrayList<SearchIndex>(indexes);
				for (SearchIndex index: objectIndexes) {
					index.initialize(getMessenger());
				}
				return null;
			}
		};
		doLocked(this,code);
	}

	public void clear() {
		for (SearchIndex index: objectIndexes) {
			index.clear();
		}
	}

	public void destroy() {
		for (SearchIndex index: objectIndexes) {
			index.destroy();
		}
	}
	
	public boolean hasObject(String name,IndexElement element) {
		boolean r = false;
		for (SearchIndex index: objectIndexes) {
			if (index.getName().equals(name)) {
				r = index.hasObject(element);
				break;
			}
		}
		return r;
	}

	public void addObjects(List<IndexElement> elements) {
		for (SearchIndex index: objectIndexes) {
			index.addObjects(elements);
		}
	}

	public void addObject(IndexElement element) {
		for (SearchIndex index: getIndexesForObjectName(element.name)) {
			index.addObject(element);
		}
	}
	
	public void setObject(IndexElement element) {
		for (SearchIndex index: getIndexesForObjectName(element.name)) {
			index.setObject(element);
		}
	}
	
	public void setObjects(List<IndexElement> elements) {
		for (IndexElement element: elements) {
			for (SearchIndex index: getIndexesForObjectName(element.name)) {
				index.setObject(element);
			}
		}
	}
	
	public void removeObject(IndexElement element) {
		for (SearchIndex index: getIndexesForObjectName(element.name)) {
			index.removeObject(element);
		}
	}
	
	public List<IndexElement> listObjects(String name, boolean ascending,boolean invert,String operator,ZStringBuilder indexValue) {
		List<IndexElement> r = new ArrayList<IndexElement>();
		for (SearchIndex index: objectIndexes) {
			if (index.getName().equals(name)) {
				r = index.listObjects(ascending, invert, operator, indexValue);
				break;
			}
		}
		return r;
	}
	
	public boolean isRebuild() {
		boolean r = false;
		lockMe(this);
		r = rebuild;
		unlockMe(this);
		return r;
	}

	public void setRebuild(boolean rebuild) {
		lockMe(this);
		this.rebuild = rebuild;
		unlockMe(this);
	}
	
	public ZStringBuilder addIndex(String objectNamePrefix,String propertyName,boolean numeric,boolean unique) {
		LockedCode code = new LockedCode() {
			@Override
			public Object doLocked() {
				ZStringBuilder err = new ZStringBuilder();
				String name = SearchIndex.getName(objectNamePrefix,propertyName);
				SearchIndex index = getIndexNoLock(name);
				if (index!=null) {
					err.append("Index " + name + " already exists");
				} else {
					index = addIndexNoLock(objectNamePrefix,propertyName,numeric,unique);
					index.added = true;
					rebuild = true;
				}
				return err;
			}
		};
		return (ZStringBuilder) doLocked(this,code);
	}

	public ZStringBuilder removeIndex(String name) {
		LockedCode code = new LockedCode() {
			@Override
			public Object doLocked() {
				ZStringBuilder err = new ZStringBuilder();
				if (name.equals(IDX_NAME) || name.equals(IDX_MODIFIED)) {
					err.append("Index " + name + " is mandatory");
				} else {
					SearchIndex index = getIndexNoLock(name);
					if (index!=null) {
						indexes.remove(index);
						index.destroy();
						rebuild = true;
					} else {
						err.append("Index " + name + " does not exist");
					}
				}
				return err;
			}
		};
		return (ZStringBuilder) doLocked(this,code);
	}

	public boolean objectHasUpdateIndexes(ZStringBuilder name) {
		return getIndexesForObjectName(name).size()>2;
	}
	
	public SortedMap<String,ZStringBuilder> getIndexValuesForObject(ZStringBuilder name,JsFile obj) {
		List<SearchIndex> indexes = getIndexesForObjectName(name);
		SortedMap<String,ZStringBuilder> r = new TreeMap<String,ZStringBuilder>();
		for (SearchIndex index: indexes) {
			if (!index.getName().equals(IndexConfig.IDX_NAME) && !index.getName().equals(IDX_MODIFIED)) {
				ZStringBuilder value = index.getIndexValueForObject(obj);
				if (value!=null) {
					r.put(index.propertyName,value);
				}
			}
		}
		return r;
	}
	
	public List<SearchIndex> getUniqueIndexesForObjectName(ZStringBuilder objectName) {
		List<SearchIndex> r = new ArrayList<SearchIndex>();
		for (SearchIndex index: getIndexesForObjectName(objectName)) {
			if (index.unique) {
				r.add(index);
			}
		}
		return r;
	}
	
	public SearchIndex getIndex(String name) {
		LockedCode code = new LockedCode() {
			@Override
			public Object doLocked() {
				return getIndexNoLock(name);
			}
		};
		return (SearchIndex) doLocked(this,code);
	}
	
	public SearchIndex getListIndex(String name) {
		SearchIndex r = null;
		for (SearchIndex index: objectIndexes) {
			if (index.getName().equals(name)) {
				r = index;
				break;
			}
		}
		return r;
	}
	
	public JsFile toUpdateJson() {
		LockedCode code = new LockedCode() {
			@Override
			public Object doLocked() {
				return toJsonNoLock(getUpdateIndexesNoLock());
			}
		};
		return (JsFile) doLocked(this,code);
	}

	@Override
	public JsFile toJson() {
		LockedCode code = new LockedCode() {
			@Override
			public Object doLocked() {
				return toJsonNoLock(getListIndexesNoLock());
			}
		};
		return (JsFile) doLocked(this,code);
	}

	@Override
	public void fromJson(JsFile json) {
		LockedCode code = new LockedCode() {
			@Override
			public Object doLocked() {
				if (json.rootElement!=null) {
					indexes.clear();
					rebuild = json.rootElement.getChildBoolean("rebuild",rebuild);
					JsElem idxsElem = json.rootElement.getChildByName("indexes");
					if (idxsElem!=null) {
						for (JsElem idxElem: idxsElem.children) {
							SearchIndex index = new SearchIndex();
							JsFile js = new JsFile();
							js.rootElement = idxElem;
							index.fromJson(js);
							addIndexNoLock(index.objectNamePrefix,index.propertyName,index.numeric,index.unique);
						}
					}
				}
				return null;
			}
		};
		doLocked(this,code);
	}

	private SearchIndex addIndexNoLock(String objectNamePrefix,String propertyName,boolean numeric,boolean unique) {
		String name = SearchIndex.getName(objectNamePrefix,propertyName);
		SearchIndex index = getIndexNoLock(name);
		if (index==null) {
			index = new SearchIndex();
			index.objectNamePrefix = objectNamePrefix;
			index.propertyName = propertyName;
			index.numeric = numeric;
			index.unique = unique;
			indexes.add(index);
		}
		return index;
	}

	private JsFile toJsonNoLock(List<SearchIndex> indexes) {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("rebuild","" + rebuild));
		JsElem idxsElem = new JsElem("indexes",true);
		json.rootElement.children.add(idxsElem);
		for (SearchIndex index: indexes) {
			idxsElem.children.add(index.toJson().rootElement);
		}
		return json;
	}

	private List<SearchIndex> getUpdateIndexesNoLock() {
		List<SearchIndex> r = new ArrayList<SearchIndex>();
		for (SearchIndex index: indexes) {
			if (!index.getName().equals(IDX_NAME) && !index.getName().equals(IDX_MODIFIED)) {
				r.add(index);
			}
		}
		return r;
	}

	private List<SearchIndex> getListIndexesNoLock() {
		List<SearchIndex> r = new ArrayList<SearchIndex>();
		for (SearchIndex index: indexes) {
			if (!index.added) {
				r.add(index);
			}
		}
		return r;
	}


	private SearchIndex getIndexNoLock(String name) {
		SearchIndex r = null;
		for (SearchIndex index: indexes) {
			if (index.getName().equals(name)) {
				r = index;
				break;
			}
		}
		return r;
	}
	
	private List<SearchIndex> getIndexesForObjectName(ZStringBuilder objectName) {
		List<SearchIndex> r = new ArrayList<SearchIndex>();
		for (SearchIndex index: objectIndexes) {
			if (!index.added && (index.objectNamePrefix.equals(PFX_OBJ) || objectName.startsWith(index.objectNamePrefix))) {
				r.add(index);
			}
		}
		return r;
	}
}
