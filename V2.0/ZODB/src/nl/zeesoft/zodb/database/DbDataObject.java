package nl.zeesoft.zodb.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.model.MdlClass;
import nl.zeesoft.zodb.database.model.MdlProperty;
import nl.zeesoft.zodb.database.model.MdlString;
import nl.zeesoft.zodb.file.xml.XMLElem;
import nl.zeesoft.zodb.file.xml.XMLFile;

public class DbDataObject {
	private SortedMap<String,StringBuilder> 	propertyValues	= new TreeMap<String,StringBuilder>();
	private SortedMap<String,List<Long>> 		linkValues		= new TreeMap<String,List<Long>>();
	
	public void setPropertyValue(String name,StringBuilder value) {
		propertyValues.put(name,value);
	}

	public StringBuilder getPropertyValue(String name) {
		return propertyValues.get(name);
	}

	public void setLinkValue(String name,List<Long> value) {
		linkValues.put(name,value);
	}

	public void setLinkValue(String name,long value) {
		List<Long> ids = null;
		if (value>0) {
			ids = new ArrayList<Long>();
			ids.add(value);
		}
		linkValues.put(name,ids);
	}

	public List<Long> getLinkValue(String name) {
		return linkValues.get(name);
	}

	public List<String> getProperties() {
		return new ArrayList<String>(propertyValues.keySet());
	}

	public List<String> getLinks() {
		return new ArrayList<String>(linkValues.keySet());
	}
	
	/**
	 * @return the id
	 */
	public long getId() {
		long id = 0;
		StringBuilder value = getPropertyValue(MdlProperty.ID);
		if (value!=null) {
			id = Long.parseLong(value.toString());
		}
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		setPropertyValue(MdlProperty.ID,new StringBuilder("" + id));
	}

	public boolean hasPropertyValue(String name) {
		boolean r = propertyValues.containsKey(name);
		if (!r) {
			r = linkValues.containsKey(name);
		}
		return r;
	}

	public void removePropertyValue(String name) {
		if (propertyValues.remove(name)==null) {
			linkValues.remove(name);
		}
	}

	public void cleanup() {
		propertyValues.clear();
		for (Entry<String,List<Long>> entry: linkValues.entrySet()) {
			entry.getValue().clear();
		}
		linkValues.clear();
	}
	
	public XMLFile toXML(MdlClass cls,boolean encode) {
		XMLFile file = new XMLFile();
		file.setRootElement(new XMLElem("object",null,null));
		if (propertyValues.size()>0) {
			XMLElem propsElem = new XMLElem("properties",null,file.getRootElement());
			for (Entry<String,StringBuilder> entry: propertyValues.entrySet()) {
				XMLElem propElem = new XMLElem(entry.getKey(),entry.getValue(),propsElem);
				MdlProperty prop = cls.getPropertyByName(entry.getKey());
				if (prop!=null && prop instanceof MdlString) {
					propElem.setCData(true);
					if (encode && ((MdlString) prop).isEncode()) {
						propElem.setValue(Generic.encodeKey(propElem.getValue(),DbConfig.getInstance().getEncryptionKey(),0));
					}
				}
			}
		}
		if (linkValues.size()>0) {
			XMLElem linksElem = new XMLElem("links",null,file.getRootElement());
			for (Entry<String,List<Long>> entry: linkValues.entrySet()) {
				XMLElem linkElem = new XMLElem(entry.getKey(),null,linksElem);
				if (entry.getValue()!=null) {
					for (Long id: entry.getValue()) {
						new XMLElem(MdlProperty.ID,new StringBuilder("" + id),linkElem);
					}
				}
			}
		}
		return file;
	}

	public void fromXML(XMLFile file,MdlClass cls,boolean decode) {
		if (file.getRootElement()==null) {
			return;
		}
		propertyValues.clear();
		linkValues.clear();
		for (XMLElem cElem: file.getRootElement().getChildren()) {
			if (cElem.getName().equals("properties")) {
				for (XMLElem propElem: cElem.getChildren()) {
					if (!propElem.getName().equals(MdlProperty.ID) || !propElem.getValue().toString().equals("0")) {
						propertyValues.put(propElem.getName(),propElem.getValue());
					}
					MdlProperty prop = cls.getPropertyByName(propElem.getName());
					if (prop!=null && prop instanceof MdlString) {
						if (decode && ((MdlString) prop).isEncode()) {
							propertyValues.put(propElem.getName(),Generic.decodeKey(propElem.getValue(),DbConfig.getInstance().getEncryptionKey(),0));
						}
					}
				}
			}
			if (cElem.getName().equals("links")) {
				for (XMLElem linkElem: cElem.getChildren()) {
					List<Long> idList = new ArrayList<Long>();
					for (XMLElem idElem: linkElem.getChildren()) {
						try {
							long id = Long.parseLong(idElem.getValue().toString());
							if (!idList.contains(id)) {
								idList.add(id);
							}
						} catch (NumberFormatException e) {
							Messenger.getInstance().error(this,"Number format exception: " + e);
							idList = null;
							break;
						}
					}
					if (idList!=null) {
						linkValues.put(linkElem.getName(),idList);
					}
				}
			}
		}
	}
	
	/**
	 * Copies this data object
	 * @param mergeObject optional object to merge values with
	 * @return a completely new object based on this object and the (optional) merge object
	 */
	public DbDataObject copy(DbDataObject mergeObject) {
		DbDataObject copy = new DbDataObject();
		
		for (Entry<String,StringBuilder> entry: propertyValues.entrySet()) {
			if (entry.getValue()!=null) {
				copy.setPropertyValue(entry.getKey(),new StringBuilder(entry.getValue()));
			} else {
				copy.setPropertyValue(entry.getKey(),null);
			}
		}
		for (Entry<String,List<Long>> entry: linkValues.entrySet()) {
			if (entry.getValue()!=null) {
				List<Long> idList = new ArrayList<Long>();
				for (long id: entry.getValue()) {
					idList.add(new Long(id));
				}
				copy.setLinkValue(entry.getKey(),idList);
			} else {
				copy.setLinkValue(entry.getKey(),null);
			}
		}

		if (mergeObject!=null) {
			for (String prop: mergeObject.getProperties()) {
				StringBuilder mergeValue = mergeObject.getPropertyValue(prop);
				if (mergeValue!=null) {
					copy.setPropertyValue(prop,new StringBuilder(mergeValue));
				} else {
					copy.removePropertyValue(prop);
				}
			}
			for (String link: mergeObject.getLinks()) {
				List<Long> mergeValue = mergeObject.getLinkValue(link);
				if (mergeValue!=null) {
					List<Long> idList = new ArrayList<Long>();
					for (long id: mergeValue) {
						idList.add(new Long(id));
					}
					copy.setLinkValue(link,idList);
				} else {
					copy.removePropertyValue(link);
				}
			}
		}
		
		return copy;
	}
}
