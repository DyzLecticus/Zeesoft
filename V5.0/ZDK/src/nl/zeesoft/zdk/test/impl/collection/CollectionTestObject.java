package nl.zeesoft.zdk.test.impl.collection;

import nl.zeesoft.zdk.collection.PersistableObject;
import nl.zeesoft.zdk.collection.PersistableProperty;

@PersistableObject()
public abstract class CollectionTestObject {
	@PersistableProperty()
	private String	testString 	= "";
	
	public String getTestString() {
		return testString;
	}
	
	public void setTestString(String testString) {
		this.testString = testString;
	}
}
