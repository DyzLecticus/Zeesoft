package nl.zeesoft.zodb.database.query;

import nl.zeesoft.zodb.model.MdlDataObject;

public class QryRemove extends QryObject {
	private QryFetch fetch = null;
	
	public QryRemove(QryFetch fetch) {
		setFetch(fetch);
	}

	public QryRemove(MdlDataObject valueObject) {
		setFetch(new QryFetch(valueObject));
	}

	public void setFetch(QryFetch fetch) {
		this.fetch = fetch;
	}

	public QryFetch getFetch() {
		return fetch;
	}
}
