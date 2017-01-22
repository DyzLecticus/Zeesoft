package nl.zeesoft.zodb.test.model.impl;

import nl.zeesoft.zodb.model.annotations.PersistCollection;
import nl.zeesoft.zodb.model.annotations.PersistProperty;
import nl.zeesoft.zodb.model.datatypes.DtInteger;
import nl.zeesoft.zodb.test.model.TestConfig;
import nl.zeesoft.zodb.test.model.TestObject;

@PersistCollection(entity=true,module=TestConfig.MODULE_TEST,nameSingle="TestB object",nameMulti="TestB objects")
public class TestB extends TestObject {
	private DtInteger 		testInteger 	= new DtInteger();

	/**
	 * @return the testInteger
	 */
	@PersistProperty(property="testInteger",label="Test integer")
	public DtInteger getTestInteger() {
		return testInteger;
	}
}
