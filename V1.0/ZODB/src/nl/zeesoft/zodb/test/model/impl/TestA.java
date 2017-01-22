package nl.zeesoft.zodb.test.model.impl;

import nl.zeesoft.zodb.model.annotations.ConstrainObjectUnique;
import nl.zeesoft.zodb.model.annotations.PersistCollection;
import nl.zeesoft.zodb.model.annotations.PersistProperty;
import nl.zeesoft.zodb.model.datatypes.DtDecimal;
import nl.zeesoft.zodb.model.datatypes.DtFloat;
import nl.zeesoft.zodb.model.datatypes.DtLong;
import nl.zeesoft.zodb.test.model.TestConfig;
import nl.zeesoft.zodb.test.model.TestObject;

@PersistCollection(entity=true,module=TestConfig.MODULE_TEST,nameSingle="TestA object",nameMulti="TestA objects")
@ConstrainObjectUnique(properties={"name","testString;testLong"})
public class TestA extends TestObject {
	private DtLong			testLong 		= new DtLong();
	private DtFloat			testFloat		= new DtFloat();
	private DtDecimal		testDecimal		= new DtDecimal();
	/**
	 * @return the testLong
	 */
	@PersistProperty(property="testLong",label="Test long")
	public DtLong getTestLong() {
		return testLong;
	}
	/**
	 * @return the testFloat
	 */
	@PersistProperty(property="testFloat",label="Test float")
	public DtFloat getTestFloat() {
		return testFloat;
	}
	/**
	 * @return the testDecimal
	 */
	@PersistProperty(property="testDecimal",label="Test decimal")
	public DtDecimal getTestDecimal() {
		return testDecimal;
	}
}
