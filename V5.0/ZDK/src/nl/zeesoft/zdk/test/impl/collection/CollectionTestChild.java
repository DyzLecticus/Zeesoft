package nl.zeesoft.zdk.test.impl.collection;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.collection.PersistableProperty;

public class CollectionTestChild extends CollectionTestObject {
	@PersistableProperty()
	private int					testInt 	= 0;
	@PersistableProperty()
	private boolean				testBoolean	= false;
	@PersistableProperty()
	private CollectionTestParent[]		testParents	= new CollectionTestParent[2];
	@PersistableProperty()
	private Str[]				testStrs	= new Str[1];
	@PersistableProperty()
	private Str					testStr		= new Str();
	
	
	public int getTestInt() {
		return testInt;
	}
	
	public void setTestInt(int testInt) {
		this.testInt = testInt;
	}
	
	public boolean isTestBoolean() {
		return testBoolean;
	}
	
	public void setTestBoolean(boolean testBoolean) {
		this.testBoolean = testBoolean;
	}

	public CollectionTestParent[] getTestParents() {
		return testParents;
	}

	public void setTestParents(CollectionTestParent[] testParents) {
		this.testParents = testParents;
	}

	public Str[] getTestStrs() {
		return testStrs;
	}

	public void setTestStrs(Str[] testStrs) {
		this.testStrs = testStrs;
	}

	public Str getTestStr() {
		return testStr;
	}

	public void setTestStr(Str testStr) {
		this.testStr = testStr;
	}
}
