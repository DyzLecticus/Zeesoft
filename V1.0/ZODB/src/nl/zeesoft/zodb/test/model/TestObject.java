package nl.zeesoft.zodb.test.model;

import nl.zeesoft.zodb.model.MdlDataObject;
import nl.zeesoft.zodb.model.annotations.PersistProperty;
import nl.zeesoft.zodb.model.datatypes.DtBoolean;
import nl.zeesoft.zodb.model.datatypes.DtString;

public abstract class TestObject extends MdlDataObject {
	private DtString			testString 		= new DtString();
	private DtBoolean			testBoolean		= new DtBoolean();
	
	/**
	 * @return the testString
	 */
	@PersistProperty(property = "testString",label="Test string")
	public DtString getTestString() {
		return testString;
	}
	
	/**
	 * @return the testBoolean
	 */
	@PersistProperty(property = "testBoolean",label="Test boolean")
	public DtBoolean getTestBoolean() {
		return testBoolean;
	}
}
