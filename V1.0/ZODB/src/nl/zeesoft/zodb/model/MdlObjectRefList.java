package nl.zeesoft.zodb.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.model.datatypes.DtBoolean;
import nl.zeesoft.zodb.model.datatypes.DtDateTime;
import nl.zeesoft.zodb.model.datatypes.DtDecimal;
import nl.zeesoft.zodb.model.datatypes.DtFloat;
import nl.zeesoft.zodb.model.datatypes.DtIdRef;
import nl.zeesoft.zodb.model.datatypes.DtIdRefList;
import nl.zeesoft.zodb.model.datatypes.DtInteger;
import nl.zeesoft.zodb.model.datatypes.DtLong;
import nl.zeesoft.zodb.model.datatypes.DtObject;
import nl.zeesoft.zodb.model.datatypes.DtString;
import nl.zeesoft.zodb.model.datatypes.DtStringBuffer;

public class MdlObjectRefList {
	private List<MdlObjectRef> references =  new ArrayList<MdlObjectRef>(); 
	
	public MdlObjectRef getMdlObjectRefById(long id) {
		MdlObjectRef o = null;
		for (MdlObjectRef ref: references) {
			if (ref.getId().getValue()==id) {
				o = ref;
				break;
			}
		}
		return o;
	}

	public MdlObjectRef getMdlObjectRefByName(String name) {
		MdlObjectRef o = null;
		for (MdlObjectRef ref: references) {
			if (ref.getName().getValue().equals(name)) {
				o = ref;
				break;
			}
		}
		return o;
	}

	public List<MdlObjectRef> getMdlObjectRefsByName(String name) {
		List<MdlObjectRef> ol = new ArrayList<MdlObjectRef>();
		for (MdlObjectRef ref: references) {
			if (ref.getName().getValue().equals(name)) {
				ol.add(ref);
			}
		}
		return ol;
	}

	public List<MdlObjectRef> getUnloadedRefs() {
		List<MdlObjectRef> ol = new ArrayList<MdlObjectRef>();
		for (MdlObjectRef ref: references) {
			if (ref.getDataObject()==null) {
				ol.add(ref);
			}
		}
		return ol;
	}
	
	@Override
	public String toString() {
		String s = "";
		for (MdlObjectRef obj: references) {
			if (s.length()>0) {
				s = s + Generic.SEP_OBJ;
			}
			s = s + obj;
		}
		return s;
	}

	public static List<MdlObjectRef> parseDtMdlObjectRefList(String s) {
		List<MdlObjectRef> l = new ArrayList<MdlObjectRef>();
		for (String val: Generic.getObjectsFromString(s)) {
			l.add(MdlObjectRef.parseMdlObjectRef(val));
		}
		return l;
	}
	
	public static MdlObjectRefList copy(MdlObjectRefList original) {
		MdlObjectRefList c = new MdlObjectRefList();
		for (MdlObjectRef ref: original.getReferences()) {
			c.getReferences().add(MdlObjectRef.copy(ref));
 		}
		return c;
	}

	public void sortObjects(String property) {
		sortObjects(property,true);
	}
	
	public void sortObjects(String property, boolean ascending) {
		if (references.size()==0) {
			return;
		}
		MdlObjectRef first = references.get(0);
		MdlObject o = null;
		if (
			(!property.equals(MdlObject.PROPERTY_ID)) &&
			(!property.equals(MdlObject.PROPERTY_NAME)) &&
			(!property.equals(MdlObject.PROPERTY_CLASSNAME)) &&
			(!property.equals(MdlObject.PROPERTY_CREATEDON)) &&
			(!property.equals(MdlObject.PROPERTY_CREATEDBY)) &&
			(!property.equals(MdlObject.PROPERTY_CHANGEDON)) &&
			(!property.equals(MdlObject.PROPERTY_CHANGEDBY))
			) {
			o = first.getDataObject();
			if (o==null) {
				Messenger.getInstance().error(this, "Unable to sort property: " + property + "; model object not set");
				return;
			}
			MdlCollectionProperty p = DbConfig.getInstance().getModel().getCollectionPropertyByName(o.getClassName().getValue(),property);
			if (p==null) {
				Messenger.getInstance().error(this, "Unable to sort class: " + o.getClass().getName() + ", property: " + property + "; class not persisted or no method for property");
				return;
			}
		}
		
		SortedMap<Boolean,MdlObjectRefList> 		sortBoolean = new TreeMap<Boolean,MdlObjectRefList>();
		SortedMap<Float,MdlObjectRefList> 			sortFloat 	= new TreeMap<Float,MdlObjectRefList>();
		SortedMap<Long,MdlObjectRefList> 			sortLong 	= new TreeMap<Long,MdlObjectRefList>();
		SortedMap<Integer,MdlObjectRefList> 		sortInteger = new TreeMap<Integer,MdlObjectRefList>();
		SortedMap<String,MdlObjectRefList>			sortString 	= new TreeMap<String,MdlObjectRefList>();
		SortedMap<BigDecimal,MdlObjectRefList>		sortDecimal	= new TreeMap<BigDecimal,MdlObjectRefList>();
		
		for (MdlObjectRef ref: references) {
			DtObject value = null;
			if (
				(!property.equals(MdlObject.PROPERTY_ID)) &&
				(!property.equals(MdlObject.PROPERTY_NAME)) &&
				(!property.equals(MdlObject.PROPERTY_CLASSNAME)) &&
				(!property.equals(MdlObject.PROPERTY_CREATEDON)) &&
				(!property.equals(MdlObject.PROPERTY_CREATEDBY)) &&
				(!property.equals(MdlObject.PROPERTY_CHANGEDON)) &&
				(!property.equals(MdlObject.PROPERTY_CHANGEDBY))
				) {
				if (ref.getDataObject()==null) {
					Messenger.getInstance().error(this, "Unable to sort property:" + property + "; model object not set");
					return;
				}
				value = ref.getDataObject().getPropertyValue(property);
			} else {
				if (property.equals(MdlObject.PROPERTY_ID)) {
					value = ref.getId();
				} else if (property.equals(MdlObject.PROPERTY_NAME)) {
					value = ref.getName();
				} else if (property.equals(MdlObject.PROPERTY_CLASSNAME)) {
					value = ref.getClassName();
				} else if (property.equals(MdlObject.PROPERTY_CREATEDON)) {
					value = ref.getCreatedOn();
				} else if (property.equals(MdlObject.PROPERTY_CREATEDBY)) {
					value = ref.getCreatedBy();
				} else if (property.equals(MdlObject.PROPERTY_CHANGEDON)) {
					value = ref.getChangedOn();
				} else if (property.equals(MdlObject.PROPERTY_CHANGEDBY)) {
					value = ref.getChangedBy();
				}
			}
			MdlObjectRefList addList = null;
			if (value instanceof DtBoolean) {
				addList = sortBoolean.get(value.getValue());
				if (addList==null) {
					addList = new MdlObjectRefList();
					sortBoolean.put((Boolean) value.getValue(), addList);
				}
				addList.getReferences().add(ref);
			} else if (value instanceof DtDateTime) {
				Date d = ((DtDateTime) value).getValue();
				long v = -10000000;
				if (d!=null) {
					v = d.getTime();
				}
				addList = sortLong.get(v);
				if (addList==null) {
					addList = new MdlObjectRefList();
					sortLong.put(v, addList);
				}
				addList.getReferences().add(ref);
			} else if (value instanceof DtDecimal) {
				addList = sortDecimal.get(value.getValue());
				if (addList==null) {
					addList = new MdlObjectRefList();
					sortDecimal.put((BigDecimal) value.getValue(), addList);
				}
				addList.getReferences().add(ref);
			} else if (value instanceof DtFloat) {
				addList = sortFloat.get(value.getValue());
				if (addList==null) {
					addList = new MdlObjectRefList();
					sortFloat.put((Float) value.getValue(), addList);
				}
				addList.getReferences().add(ref);
			} else if (value instanceof DtInteger) {
				addList = sortInteger.get(value.getValue());
				if (addList==null) {
					addList = new MdlObjectRefList();
					sortInteger.put((Integer) value.getValue(), addList);
				}
				addList.getReferences().add(ref);
			} else if (value instanceof DtLong) {
				addList = sortLong.get(value.getValue());
				if (addList==null) {
					addList = new MdlObjectRefList();
					sortLong.put((Long) value.getValue(), addList);
				}
				addList.getReferences().add(ref);
			} else if 
				(
				(value instanceof DtString) ||
				(value instanceof DtStringBuffer)
				) {
				if (value instanceof DtStringBuffer) {
					addList = sortString.get(value.getValue().toString());
				} else {
					addList = sortString.get(value.getValue());
				}
				if (addList==null) {
					addList = new MdlObjectRefList();
					if (value instanceof DtStringBuffer) {
						sortString.put(value.getValue().toString(), addList);
					} else {
						sortString.put((String) value.getValue(), addList);
					}
				}
				addList.getReferences().add(ref);
			} else if (value instanceof DtIdRef) {
				long v = ((DtIdRef) value).getValue();
				addList = sortLong.get(v);
				if (addList==null) {
					addList = new MdlObjectRefList();
					sortLong.put(v, addList);
				}
				addList.getReferences().add(ref);
			} else if (value instanceof DtIdRefList) {
				int v = ((DtIdRefList) value).getValue().size();
				addList = sortInteger.get(v);
				if (addList==null) {
					addList = new MdlObjectRefList();
					sortInteger.put(v, addList);
				}
				addList.getReferences().add(ref);
			}
		}
		
		MdlObjectRefList newList = new MdlObjectRefList();
		if (sortBoolean.size()>0) {
			if (ascending) {
    			for (Entry<Boolean,MdlObjectRefList> entry : sortBoolean.entrySet()) {
    				MdlObjectRefList l = (MdlObjectRefList) entry.getValue();
    				for (MdlObjectRef obj: l.getReferences()) {
    					newList.getReferences().add(obj);
    				}
    			}
			} else {
				ArrayList<Boolean> keys = new ArrayList<Boolean>(sortBoolean.keySet());
				for(int i=keys.size()-1; i>=0;i--){ 
    				MdlObjectRefList l = (MdlObjectRefList) sortBoolean.get(keys.get(i));
    				for (MdlObjectRef obj: l.getReferences()) {
    					newList.getReferences().add(obj);
    				}
		        } 
			}
			setReferences(newList.getReferences());
		} else if (sortDecimal.size()>0) {
			if (ascending) {
    			for (Entry<BigDecimal,MdlObjectRefList> entry : sortDecimal.entrySet()) {
    				MdlObjectRefList l = (MdlObjectRefList) entry.getValue();
    				for (MdlObjectRef obj: l.getReferences()) {
    					newList.getReferences().add(obj);
    				}
    			}
			} else {
				ArrayList<BigDecimal> keys = new ArrayList<BigDecimal>(sortDecimal.keySet());
				for(int i=keys.size()-1; i>=0;i--){ 
    				MdlObjectRefList l = (MdlObjectRefList) sortDecimal.get(keys.get(i));
    				for (MdlObjectRef obj: l.getReferences()) {
    					newList.getReferences().add(obj);
    				}
		        } 
			}
			setReferences(newList.getReferences());
		} else if (sortFloat.size()>0) {
			if (ascending) {
    			for (Entry<Float,MdlObjectRefList> entry : sortFloat.entrySet()) {
    				MdlObjectRefList l = (MdlObjectRefList) entry.getValue();
    				for (MdlObjectRef obj: l.getReferences()) {
    					newList.getReferences().add(obj);
    				}
    			}
			} else {
				ArrayList<Float> keys = new ArrayList<Float>(sortFloat.keySet());
				for(int i=keys.size()-1; i>=0;i--){ 
    				MdlObjectRefList l = (MdlObjectRefList) sortFloat.get(keys.get(i));
    				for (MdlObjectRef obj: l.getReferences()) {
    					newList.getReferences().add(obj);
    				}
		        } 
			}
			setReferences(newList.getReferences());
		} else if (sortLong.size()>0) {
			if (ascending) {
    			for (Entry<Long,MdlObjectRefList> entry : sortLong.entrySet()) {
    				MdlObjectRefList l = (MdlObjectRefList) entry.getValue();
    				for (MdlObjectRef obj: l.getReferences()) {
    					newList.getReferences().add(obj);
    				}
    			}
			} else {
				ArrayList<Long> keys = new ArrayList<Long>(sortLong.keySet());
				for(int i=keys.size()-1; i>=0;i--){ 
    				MdlObjectRefList l = (MdlObjectRefList) sortLong.get(keys.get(i));
    				for (MdlObjectRef obj: l.getReferences()) {
    					newList.getReferences().add(obj);
    				}
		        } 
			}
			setReferences(newList.getReferences());
		} else if (sortInteger.size()>0) {
			if (ascending) {
    			for (Entry<Integer,MdlObjectRefList> entry : sortInteger.entrySet()) {
    				MdlObjectRefList l = (MdlObjectRefList) entry.getValue();
    				for (MdlObjectRef obj: l.getReferences()) {
    					newList.getReferences().add(obj);
    				}
    			}
			} else {
				ArrayList<Integer> keys = new ArrayList<Integer>(sortInteger.keySet());
				for(int i=keys.size()-1; i>=0;i--){ 
    				MdlObjectRefList l = (MdlObjectRefList) sortInteger.get(keys.get(i));
    				for (MdlObjectRef obj: l.getReferences()) {
    					newList.getReferences().add(obj);
    				}
		        } 
			}
			setReferences(newList.getReferences());
		} else if (sortString.size()>0) {
			if (ascending) {
    			for (Entry<String,MdlObjectRefList> entry : sortString.entrySet()) {
    				MdlObjectRefList l = (MdlObjectRefList) entry.getValue();
    				for (MdlObjectRef obj: l.getReferences()) {
    					newList.getReferences().add(obj);
    				}
    			}
			} else {
				ArrayList<String> keys = new ArrayList<String>(sortString.keySet());
				for(int i=keys.size()-1; i>=0;i--){ 
    				MdlObjectRefList l = (MdlObjectRefList) sortString.get(keys.get(i));
    				for (MdlObjectRef obj: l.getReferences()) {
    					newList.getReferences().add(obj);
    				}
		        } 
			}
			setReferences(newList.getReferences());
		}
		
	}

	/**
	 * @return the references
	 */
	public List<MdlObjectRef> getReferences() {
		return references;
	}

	/**
	 * @param references the references to set
	 */
	public void setReferences(List<MdlObjectRef> references) {
		this.references = references;
	}
}
