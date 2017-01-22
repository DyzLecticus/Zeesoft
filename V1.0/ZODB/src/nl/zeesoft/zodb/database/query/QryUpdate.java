package nl.zeesoft.zodb.database.query;

import nl.zeesoft.zodb.model.MdlDataObject;

public class QryUpdate extends QryAdd {
	private QryFetch fetch = null;

	public QryUpdate(MdlDataObject valueObject) {
		super(valueObject);
		setFetch(new QryFetch(valueObject));
	}
	
	public QryUpdate(QryFetch fetch, MdlDataObject valueObject) {
		super(valueObject);
		setFetch(fetch);
	}
	
	public void setFetch(QryFetch fetch) {
		this.fetch = fetch;
	}

	public QryFetch getFetch() {
		return fetch;
	}
}
