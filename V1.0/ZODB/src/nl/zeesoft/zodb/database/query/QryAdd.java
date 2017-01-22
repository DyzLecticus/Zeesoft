package nl.zeesoft.zodb.database.query;

import nl.zeesoft.zodb.model.MdlDataObject;

public class QryAdd extends QryObject {
	public MdlDataObject dataObject = null;

	public QryAdd(MdlDataObject dataObject) {
		this.dataObject = dataObject;
	}
	
	/**
	 * @return the dataObject
	 */
	public MdlDataObject getDataObject() {
		return dataObject;
	}

	/**
	 * @param dataObject the dataObject to set
	 */
	public void setDataObject(MdlDataObject dataObject) {
		this.dataObject = dataObject;
	}
}
