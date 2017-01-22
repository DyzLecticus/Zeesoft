package nl.zeesoft.zodb.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.file.FileObject;
import nl.zeesoft.zodb.model.datatypes.DtObject;

public class MdlCollectionUniqueConstraint {
	private	MdlCollection					modelClass		= null;
	private List<MdlCollectionProperty>		properties		= new ArrayList<MdlCollectionProperty>();

	private SortedMap<String,Long>			keyIdMap		= new TreeMap<String,Long>();

	private String							fullFileName	= "";
		
	private boolean							isChanged		= false;
	
	protected MdlCollectionUniqueConstraint() {
		
	}

	public String getCrcText() {
		String text = modelClass.getName();
		for (MdlCollectionProperty prop: properties) {
			text = text + Generic.SEP_STR + prop.getName();
		}
		return text;
	}

	public String getIndexKeyForObject(MdlDataObject obj) {
		StringBuffer r = new StringBuffer();
		for (MdlCollectionProperty mtd: properties) {
			DtObject value = obj.getPropertyValue(mtd.getName());
			if (r.length()>0) {
				r.append(Generic.SEP_STR);
			}
			r.append(Generic.getSerializableStringValue(value.toStringBuffer().toString()));
		}
		return r.toString();
	}

	public void addObject(MdlDataObject obj) {
		String key = getIndexKeyForObject(obj);
		if (keyIdMap.get(key)==null) {
			keyIdMap.put(key, obj.getId().getValue());
			isChanged = true;
		}
	}
	public void removeObject(MdlDataObject obj) {
		String key = getIndexKeyForObject(obj);
		Long removed = keyIdMap.remove(key);
		if (removed!=null) {
			isChanged = true;
		}
	}
	
	public Long getIdForKey(String key) {
		return keyIdMap.get(key);
	}

	public StringBuffer getStringBufferIfChanged() {
		StringBuffer sb = null;
		if (isChanged) {
			sb = toStringBuffer();
			isChanged = false;
		}
		return sb;
	}

	public void serialize() {
		FileObject f = new FileObject();
		f.writeFile(getFullFileName(), toStringBuffer());
	}

	private StringBuffer toStringBuffer() {
		StringBuffer sb = new StringBuffer();
		for (Entry<String,Long> e: keyIdMap.entrySet()) {
			sb.append(e.getKey());
			sb.append(Generic.SEP_OBJ);
			sb.append(e.getValue().toString());
			sb.append("\n");
		}
		return sb;
	}

	public void unserialize() {
		if (FileObject.fileExists(getFullFileName())) {
			FileObject f = new FileObject();
			String[] ls = f.readFile(getFullFileName()).toString().split("\n");
			for (String l: ls) {
				if ((l.length()>0) && (!l.trim().equals(""))) {
					List<String> objs = Generic.getObjectsFromString(l.toString());
					if (objs.size()==2) {
						keyIdMap.put(objs.get(0),Long.parseLong(objs.get(1)));
					}
				}
			}
			isChanged = false;
		}
	}

	public String getFullFileName() {
		if (fullFileName.length()==0) {
			String dir = Generic.dirName(DbConfig.getInstance().getFullDataDir() + modelClass.getName());
			String name = "UIDX";
			for (MdlCollectionProperty mtd: properties) {
				name = name + "_" + mtd.getName();
			}
			fullFileName = dir + name + ".txt"; 
		}
		return fullFileName;
	}
	
	/**
	 * @param modelClass the modelClass to set
	 */
	public void setModelClass(MdlCollection modelClass) {
		this.modelClass = modelClass;
	}

	/**
	 * @return the properties
	 */
	public List<MdlCollectionProperty> getProperties() {
		return properties;
	}
}
