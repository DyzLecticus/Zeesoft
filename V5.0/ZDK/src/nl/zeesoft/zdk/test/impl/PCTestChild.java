package nl.zeesoft.zdk.test.impl;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.collection.PersistableProperty;

public class PCTestChild extends PCTestObject {
	@PersistableProperty()
	private int					testInt 	= 0;
	@PersistableProperty()
	private boolean				testBoolean	= false;
	@PersistableProperty()
	private PCTestParent[]		testParents	= new PCTestParent[2];
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

	public PCTestParent[] getTestParents() {
		return testParents;
	}

	public void setTestParents(PCTestParent[] testParents) {
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
