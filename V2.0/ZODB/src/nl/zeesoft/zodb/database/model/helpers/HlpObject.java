package nl.zeesoft.zodb.database.model.helpers;

import nl.zeesoft.zodb.Locker;
import nl.zeesoft.zodb.database.DbDataObject;
import nl.zeesoft.zodb.database.request.ReqDataObject;
import nl.zeesoft.zodb.database.request.ReqUpdate;
import nl.zeesoft.zodb.event.EvtEventSubscriber;

public abstract class HlpObject extends Locker {
	private long 	id						= 0;

	public void fromDataObject(DbDataObject obj) {
		setId(obj.getId());
	}

	public DbDataObject toDataObject() {
		DbDataObject r = new DbDataObject();
		if (getId()>0) {
			r.setId(getId());
		}
		return r;
	}

	public ReqUpdate getNewUpdateRequest(EvtEventSubscriber subscriber) {
		ReqUpdate updateRequest = new ReqUpdate(this.getClass().getName(),getId());
		updateRequest.getObjects().add(new ReqDataObject(toDataObject()));
		if (subscriber!=null) {
			updateRequest.addSubscriber(subscriber);
		}
		return updateRequest;
	}
	
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
}
