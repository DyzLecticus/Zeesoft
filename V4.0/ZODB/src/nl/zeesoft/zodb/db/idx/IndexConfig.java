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
import nl.zeesoft.zdk.thread.Locker;

public class IndexConfig extends Locker implements JsAble {
	public static final String	PFX_NAME		= "@NAME";
	public static final String	PFX_MODIFIED	= "@MODIFIED";
	
	public static final String	IDX_NAME		= PFX_NAME + ":name";
	public static final String	IDX_MODIFIED	= PFX_MODIFIED + ":modified";
	
	private boolean				rebuild			= false;
	private List<SearchIndex>	indexes 		= new ArrayList<SearchIndex>();
	
	public IndexConfig(Messenger msgr) {
		super(msgr);
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
		lockMe(this);
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
		unlockMe(this);
		return err;
	}

	public ZStringBuilder removeIndex(String name) {
		lockMe(this);
		ZStringBuilder err = new ZStringBuilder();
		SearchIndex index = getIndexNoLock(name);
		if (index!=null) {
			indexes.remove(index);
			rebuild = true;
		} else {
			err.append("Index " + name + " does not exist");
		}
		unlockMe(this);
		return err;
	}

	public boolean objectHasIndexes(String name) {
		boolean r = false;
		lockMe(this);
		r = getIndexesForObjectNameNoLock(name).size()>0;
		unlockMe(this);
		return r;
	}
	
	public SortedMap<String,String> getIndexValuesForObject(String name,JsFile obj) {
		lockMe(this);
		List<SearchIndex> indexes = getIndexesForObjectNameNoLock(name);
		SortedMap<String,String> r = new TreeMap<String,String>();
		for (SearchIndex index: indexes) {
			r.put(index.propertyName,index.getIndexValueForObject(obj));
		}
		unlockMe(this);
		return r;
	}
	
	public List<SearchIndex> getUniqueIndexesForObjectName(String objectName) {
		lockMe(this);
		List<SearchIndex> r = new ArrayList<SearchIndex>();
		for (SearchIndex index: getIndexesForObjectNameNoLock(objectName)) {
			if (index.unique && objectName.startsWith(index.objectNamePrefix)) {
				r.add(index);
			}
		}
		unlockMe(this);
		return r;
	}

	public SearchIndex getIndex(String name) {
		SearchIndex r = null;
		lockMe(this);
		r = getIndexNoLock(name);
		unlockMe(this);
		return r;
	}
	
	public SearchIndex getListIndex(String name) {
		SearchIndex r = null;
		lockMe(this);
		for (SearchIndex index: getListIndexesNoLock()) {
			if (index.getName().equals(name)) {
				r = index;
				break;
			}
		}
		unlockMe(this);
		return r;
	}
	
	public JsFile toSelectJson() {
		JsFile json = new JsFile();
		lockMe(this);
		json = toJsonNoLock(getListIndexesNoLock());
		unlockMe(this);
		return json;
	}

	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		lockMe(this);
		json = toJsonNoLock(indexes);
		unlockMe(this);
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			lockMe(this);
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
			unlockMe(this);
		}
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

	private List<SearchIndex> getListIndexesNoLock() {
		List<SearchIndex> r = new ArrayList<SearchIndex>();
		for (SearchIndex index: indexes) {
			if (!index.added) {
				r.add(index);
			}
		}
		SearchIndex index = new SearchIndex();
		index.objectNamePrefix = PFX_NAME;
		index.propertyName = "name";
		index.numeric = false;
		index.unique = true;
		r.add(index);
		
		index = new SearchIndex();
		index.objectNamePrefix = PFX_MODIFIED;
		index.propertyName = "modified";
		index.numeric = true;
		index.unique = false;
		r.add(index);
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
	
	private List<SearchIndex> getIndexesForObjectNameNoLock(String objectName) {
		List<SearchIndex> r = new ArrayList<SearchIndex>();
		for (SearchIndex index: indexes) {
			if (!index.added && objectName.startsWith(index.objectNamePrefix)) {
				r.add(index);
			}
		}
		return r;
	}
}
