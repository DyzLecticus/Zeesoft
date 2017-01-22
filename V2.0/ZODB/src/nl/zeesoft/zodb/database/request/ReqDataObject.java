package nl.zeesoft.zodb.database.request;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zodb.database.DbDataObject;
import nl.zeesoft.zodb.database.model.MdlClass;
import nl.zeesoft.zodb.database.model.MdlProperty;
import nl.zeesoft.zodb.file.xml.XMLElem;
import nl.zeesoft.zodb.file.xml.XMLFile;

/**
 * This class is a wrapper for request data objects
 * It is needed to return data object specific errors and child index id lists
 */
public class ReqDataObject {
	private DbDataObject 					dataObject 			= null;
	private List<ReqError>					errors				= new ArrayList<ReqError>();
	private SortedMap<String,List<Long>>	childIndexIdList	= new TreeMap<String,List<Long>>();

	protected ReqDataObject() {
		// Used by fromXML
	}

	public ReqDataObject(DbDataObject dataObject) {
		this.dataObject = dataObject;
	}
	
	protected ReqError addError(String code, String message) {
		ReqError err = new ReqError(code,message);
		errors.add(err);
		return err;
	}

	public ReqDataObject copy() {
		ReqDataObject copy = new ReqDataObject();
		copy.setDataObject(dataObject.copy(null));
		copy.getErrors().clear();
		for (ReqError error: errors) {
			copy.getErrors().add(error.copy());
		}
		for (Entry<String,List<Long>> entry: childIndexIdList.entrySet()) {
			List<Long> newIdList = new ArrayList<Long>();
			for(long id: entry.getValue()) {
				newIdList.add(new Long(id));
			}
			copy.getChildIndexIdList().put(new String(entry.getKey()),newIdList);
		}
		return copy;
	}
	
	protected XMLFile toXML(MdlClass cls) {
		XMLFile file = new XMLFile();
		file.setRootElement(new XMLElem("requestObject",null,null));
		dataObject.toXML(cls,false).getRootElement().setParent(file.getRootElement());
		if (errors.size()>0) {
			XMLElem errElem = new XMLElem("errors",null,file.getRootElement());
			for (ReqError error: errors) {
				error.toXML().getRootElement().setParent(errElem);
			}
		}
		if (childIndexIdList.size()>0) {
			XMLElem cidxsElem = new XMLElem("childIndexes",null,file.getRootElement());
			for (Entry<String,List<Long>> entry: childIndexIdList.entrySet()) {
				XMLElem cidxElem = new XMLElem("childIndex",null,cidxsElem);
				new XMLElem("name",new StringBuilder(entry.getKey()),cidxElem);
				XMLElem cidxIdListElem = new XMLElem("idList",null,cidxElem);
				for (long id: entry.getValue()) {
					new XMLElem(MdlProperty.ID,new StringBuilder("" + id),cidxIdListElem);
				}
			}
		}
		return file;
	}

	protected void fromXML(XMLElem rootElem,MdlClass cls) {
		if (rootElem.getName().equals("requestObject")) {
			for (XMLElem cElem: rootElem.getChildren()) {
				if (cElem.getName().equals("object")) {
					dataObject = new DbDataObject();
					XMLFile f = new XMLFile();
					f.setRootElement(cElem);
					dataObject.fromXML(f,cls,false);
				}
				if (cElem.getName().equals("errors")) {
					for (XMLElem errElem: cElem.getChildren()) {
						ReqError error = new ReqError();
						error.fromXML(errElem);
						errors.add(error);
					}
				}
				if (cElem.getName().equals("childIndexes")) {
					for (XMLElem cidxElem: cElem.getChildren()) {
						String key = null;
						List<Long> idList = new ArrayList<Long>();
						for (XMLElem cidxCElem: cidxElem.getChildren()) {
							if (cidxCElem.getName().equals("name") && cidxCElem.getValue()!=null && cidxCElem.getValue().length()>0) {
								key = cidxCElem.getValue().toString();
							}
							if (cidxCElem.getName().equals("idList")) {
								for (XMLElem idElem: cidxCElem.getChildren()) {
									if (idElem.getValue()!=null && idElem.getValue().length()>0) {
										idList.add(Long.parseLong(idElem.getValue().toString()));
									}
								}
							}
						}
						if (key.length()>0) {
							childIndexIdList.put(key, idList);
						}
					}
				}
			}
		}
	}

	/**
	 * @return the dataObject
	 */
	public DbDataObject getDataObject() {
		return dataObject;
	}

	/**
	 * @param dataObject the dataObject to set
	 */
	public void setDataObject(DbDataObject dataObject) {
		this.dataObject = dataObject;
	}

	/**
	 * @return the errors
	 */
	public List<ReqError> getErrors() {
		return errors;
	}

	/**
	 * @return the childIndexIdList
	 */
	public SortedMap<String, List<Long>> getChildIndexIdList() {
		return childIndexIdList;
	}
}
