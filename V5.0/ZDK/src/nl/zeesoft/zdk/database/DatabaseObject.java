package nl.zeesoft.zdk.database;

import nl.zeesoft.zdk.Instantiator;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.collection.PersistableObject;
import nl.zeesoft.zdk.collection.PersistableProperty;
import nl.zeesoft.zdk.thread.Lock;

@PersistableObject
public abstract class DatabaseObject {
	protected final Lock	lock	= new Lock();
	
	@PersistableProperty
	private Str				id		= new Str();

	public DatabaseObject copy() {
		DatabaseObject r = (DatabaseObject) Instantiator.getNewClassInstanceForName(this.getClass().getName());
		r.setId(getId());
		copyTo(r);
		return r;
	}
	
	public final Str getId() {
		lock.lock(this);
		Str r = new Str(id);
		lock.unlock(this);
		return r;
	}

	protected void setId(Str id) {
		if (id!=null) {
			lock.lock(this);
			this.id = id;
			lock.unlock(this);
		}
	}
	
	protected abstract void copyTo(DatabaseObject copy);
}
