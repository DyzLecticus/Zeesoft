package nl.zeesoft.zdk.test.collection;

import nl.zeesoft.zdk.Str;

public class CollectionTestChild extends CollectionTestObject {
	private int						testInt 	= 0;
	private boolean					testBoolean	= false;
	private CollectionTestParent[]	testParents	= new CollectionTestParent[2];
	private Str[]					testStrs	= new Str[1];
	private Str						testStr		= new Str();
	
	
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
