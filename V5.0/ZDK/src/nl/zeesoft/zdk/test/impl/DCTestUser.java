package nl.zeesoft.zdk.test.impl;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.collection.PersistableProperty;
import nl.zeesoft.zdk.database.DatabaseObject;

public class DCTestUser extends DatabaseObject {
	@PersistableProperty
	private String	name		= "";
	@PersistableProperty
	private Str		password	= new Str();

	@Override
	protected void copyTo(DatabaseObject copy) {
		DCTestUser user = (DCTestUser) copy;
		user.setName(getName());
		user.setPassword(getPassword());
	}
	
	public String getName() {
		lock.lock(this);
		String r = name;
		lock.unlock(this);
		return r;
	}
	
	public void setName(String name) {
		lock.lock(this);
		this.name = name;
		lock.unlock(this);
	}
	
	public Str getPassword() {
		lock.lock(this);
		Str r = new Str(password);
		lock.unlock(this);
		return r;
	}
	
	public void setPassword(Str password) {
		lock.lock(this);
		this.password = password;
		lock.unlock(this);
	}
}
