package nl.zeesoft.zdk.test.impl;

import nl.zeesoft.zdk.persist.PersistableObject;
import nl.zeesoft.zdk.persist.PersistableProperty;

@PersistableObject()
public abstract class PersistableAbstract {
	@PersistableProperty()
	private String	testString 	= "";
	
	public String getTestString() {
		return testString;
	}
	
	public void setTestString(String testString) {
		this.testString = testString;
	}
}
