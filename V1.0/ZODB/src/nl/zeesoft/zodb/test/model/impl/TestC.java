package nl.zeesoft.zodb.test.model.impl;

import nl.zeesoft.zodb.model.annotations.PersistCollection;
import nl.zeesoft.zodb.model.annotations.PersistReference;
import nl.zeesoft.zodb.model.datatypes.DtIdRef;
import nl.zeesoft.zodb.model.datatypes.DtIdRefList;
import nl.zeesoft.zodb.test.model.TestConfig;
import nl.zeesoft.zodb.test.model.TestObject;

@PersistCollection(entity=false,module=TestConfig.MODULE_TEST,nameSingle="TestC object",nameMulti="TestC objects")
public class TestC extends TestObject {
	private DtIdRef			testReference		= new DtIdRef();
	private DtIdRefList		testReferenceList	= new DtIdRefList();

	/**
	 * @return the testReference
	 */
	@PersistReference(property="testReference",className="nl.zeesoft.zodb.test.model.impl.TestA",label="Test A object",entityLabel="Test C objects")
	public DtIdRef getTestReference() {
		return testReference;
	}
	/**
	 * @return the testReferenceList
	 */
	@PersistReference(property="testReferenceList",className="nl.zeesoft.zodb.test.model.impl.TestB",label="Test B objects",entityLabel="Test C objects")
	public DtIdRefList getTestReferenceList() {
		return testReferenceList;
	}
}
