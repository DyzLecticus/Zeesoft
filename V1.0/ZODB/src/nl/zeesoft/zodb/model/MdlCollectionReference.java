package nl.zeesoft.zodb.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.file.FileObject;
import nl.zeesoft.zodb.model.datatypes.DtIdRef;
import nl.zeesoft.zodb.model.datatypes.DtIdRefList;
import nl.zeesoft.zodb.model.datatypes.DtObject;

public class MdlCollectionReference extends MdlCollectionProperty {
	private SortedMap<Long,List<Long>>	idReferenceMap	= new TreeMap<Long,List<Long>>();
	
	private MdlCollection				reference 		= null;
	private boolean						entity 			= false;
	private String 						entityLabel		= "";
	private boolean						removeMe		= false;
	
	private boolean						isChanged		= false;

	private String						fullFileName	= "";
	
	protected MdlCollectionReference() {
		
	}

	public void addParentObject(MdlObjectRef obj) {
		// No need to save parent without children
	}
	public void removeParentObject(MdlObjectRef obj) {
		List<Long> removed = idReferenceMap.remove(obj.getId().getValue());
		if (removed!=null) {
			isChanged = true;
		}
	}
	
	public void addChildObject(MdlDataObject obj) {
		DtObject valueObj = obj.getPropertyValue(getName());
		if (valueObj instanceof DtIdRef) {
			Long value = ((DtIdRef) valueObj).getValue();
			List<Long> idList = idReferenceMap.get(value);
			if (idList==null) {
				idList = new ArrayList<Long>();
				idList.add(obj.getId().getValue());
				idReferenceMap.put(value, idList);
				isChanged = true;
			} else if (!idList.contains(obj.getId().getValue())) {
				idList.add(obj.getId().getValue());
				isChanged = true;
			}
		} else if (valueObj instanceof DtIdRefList) {
			List<Long> values = ((DtIdRefList) valueObj).getValue();
			// Create zero value list to be able to fetch empty references
			if (values.size()==0) {
				values = new ArrayList<Long>();
				values.add(0L);
			}
			for (long value: values) {
				List<Long> idList = idReferenceMap.get(value);
				if (idList==null) {
					idList = new ArrayList<Long>();
					idList.add(obj.getId().getValue());
					idReferenceMap.put(value, idList);
					isChanged = true;
				} else if (!idList.contains(obj.getId().getValue())) {
					idList.add(obj.getId().getValue());
					isChanged = true;
				}
			}
		}
	}

	public void removeChildObject(MdlDataObject obj) {
		DtObject valueObj = obj.getPropertyValue(getName());
		if (valueObj instanceof DtIdRef) {
			Long value = ((DtIdRef) valueObj).getValue();
			List<Long> idList = idReferenceMap.get(value);
			if (idList!=null) {
				boolean removed = idList.remove(obj.getId().getValue());
				if (removed) {
					if (idList.size()<=0) {
						 idReferenceMap.remove(value);
					}
					isChanged = true;
				}
			}
		} else if (valueObj instanceof DtIdRefList) {
			List<Long> values = ((DtIdRefList) valueObj).getValue();
			// Remove zero value list
			if (values.size()==0) {
				values = new ArrayList<Long>();
				values.add(0L);
			}
			for (long value: values) {
				List<Long> idList = idReferenceMap.get(value);
				if (idList!=null) {
					boolean removed = idList.remove(obj.getId().getValue());
					if (removed) {
						if (idList.size()<=0) {
							 idReferenceMap.remove(value);
						}
						isChanged = true;
					}
				}
			}
		}
	}
	
	public List<Long> getChildIdListForParentId(long id) {
		return idReferenceMap.get(id);
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
		for (Entry<Long,List<Long>> e: idReferenceMap.entrySet()) {
			sb.append(e.getKey().toString());
			sb.append(Generic.SEP_OBJ);
			boolean first = true;
			for (long id: e.getValue()) {
				if (!first) {
					sb.append(Generic.SEP_STR);
				}
				sb.append(id);
				first = false;
			}
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
					List<String> objs = Generic.getObjectsFromString(l);
					if (objs.size()==2) {
						String id = objs.get(0);
						String[] list = Generic.getValuesFromString(objs.get(1));
						List<Long> idList = new ArrayList<Long>();
						for (int i = 0; i < list.length; i++) {
							idList.add(Long.parseLong(list[i]));
						}
						idReferenceMap.put(Long.parseLong(id), idList);
					}
				}
			}
			isChanged = false;
		}
	}

	public String getFullFileName() {
		if (fullFileName.length()==0) {
			String dir = Generic.dirName(DbConfig.getInstance().getFullDataDir() + getModelCollection().getName());
			fullFileName = dir + "RIDX_" + getName() + ".txt";
		}
		return fullFileName;
	}
	
	@Override
	public String toString() {
		String r = super.toString() + Generic.SEP_STR + reference.getName() + Generic.SEP_STR + entity + Generic.SEP_STR + removeMe  + Generic.SEP_STR + entityLabel;
		return r;
	}
	/**
	 * @return the reference
	 */
	public MdlCollection getReference() {
		return reference;
	}
	/**
	 * @param reference the reference to set
	 */
	protected void setReference(MdlCollection reference) {
		this.reference = reference;
	}
	/**
	 * @return the entity
	 */
	public boolean isEntity() {
		return entity;
	}
	/**
	 * @param entity the entity to set
	 */
	protected void setEntity(boolean entity) {
		this.entity = entity;
	}

	/**
	 * @return the removeMe
	 */
	public boolean isRemoveMe() {
		return removeMe;
	}

	/**
	 * @param removeMe the deleteMe to set
	 */
	public void setRemoveMe(boolean removeMe) {
		this.removeMe = removeMe;
	}

	/**
	 * @return the entityLabel
	 */
	public String getEntityLabel() {
		return entityLabel;
	}

	/**
	 * @param entityLabel the entityLabel to set
	 */
	public void setEntityLabel(String entityLabel) {
		this.entityLabel = entityLabel;
	}
}
