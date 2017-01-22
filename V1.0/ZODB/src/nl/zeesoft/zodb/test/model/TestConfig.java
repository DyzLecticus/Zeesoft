package nl.zeesoft.zodb.test.model;

import java.io.File;
import java.util.List;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.client.ClEntityLoader;
import nl.zeesoft.zodb.client.ClRequest;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbIndex;
import nl.zeesoft.zodb.database.query.QryAdd;
import nl.zeesoft.zodb.database.query.QryFetch;
import nl.zeesoft.zodb.database.query.QryTransaction;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.event.EvtEventSubscriber;
import nl.zeesoft.zodb.model.MdlDataObject;
import nl.zeesoft.zodb.model.MdlObjectRef;
import nl.zeesoft.zodb.model.ZODBModel;
import nl.zeesoft.zodb.model.datatypes.DtIdRefList;
import nl.zeesoft.zodb.model.impl.DbUser;
import nl.zeesoft.zodb.test.model.impl.TestA;
import nl.zeesoft.zodb.test.model.impl.TestB;
import nl.zeesoft.zodb.test.model.impl.TestC;
import nl.zeesoft.zodb.test.tests.TestClientSessionEntityLoader;

public class TestConfig extends ZODBModel implements EvtEventSubscriber {
	public final static String	MODULE_TEST 	= "TEST";
	
	private final static String	TEST_DIR 		= "C:/Zeesoft/GitHub/Zeesoft/ZODB/testdir";
	private File 				testDir			= null;
	
	public String getTestDir() {
		if (testDir==null) {
			testDir = new File(TEST_DIR);
			if (!testDir.exists()) {
				testDir.mkdir();
			}
		}
		return Generic.dirName(testDir.getAbsolutePath());
	}
	
	@Override
	protected List<String> getPersistedClassNames() {
		List<String> l = super.getPersistedClassNames();
		l.add(TestA.class.getName());
		l.add(TestB.class.getName());
		l.add(TestC.class.getName());
		return l;
	}

	@Override
	public DbUser generateInitialData(Object source) {
		DbUser admin = super.generateInitialData(source);
		
		QryTransaction t = null;
		
		TestA testA = null;
		TestB testB = null;
		TestC testC = null;

		t = new QryTransaction(admin);
		for (int i = 0; i < 20; i ++) {
			testA = new TestA();
			testA.getName().setValue("TestA:" + i);
			if (i < 10) {
				testA.getTestBoolean().setValue(true);
				testA.getTestFloat().setValue(new Float(0.5));
				testA.getTestLong().setValue(new Long(1));
			} else {
				testA.getTestBoolean().setValue(false);
				testA.getTestFloat().setValue(new Float(0.3));
				testA.getTestLong().setValue(new Long(7));
			}
			testA.getTestString().setValue("Test:" + i);
			t.addQuery(new QryAdd(testA));
		}
		DbIndex.getInstance().executeTransaction(t, source);
		
		QryFetch fetch = new QryFetch(TestA.class.getName());
		DbIndex.getInstance().executeFetch(fetch, admin, source);
		QryTransaction tc = new QryTransaction(admin);
		for (MdlObjectRef ref: fetch.getMainResults().getReferences()) {
			testA = (TestA) ref.getDataObject();
			DtIdRefList testBList = new DtIdRefList();
			
			testB = new TestB();
			testB.getName().setValue("TestB:1 for " + testA.getName());
			t = new QryTransaction(admin);
			t.addQuery(new QryAdd(testB));
			DbIndex.getInstance().executeTransaction(t, source);
			testBList.getValue().add(testB.getId().getValue());

			testB = new TestB();
			testB.getName().setValue("TestB:2 for " + testA.getName());
			t = new QryTransaction(admin);
			t.addQuery(new QryAdd(testB));
			DbIndex.getInstance().executeTransaction(t, source);
			testBList.getValue().add(testB.getId().getValue());

			testC = new TestC();
			testC.getName().setValue("TestC:1 for " + testA.getName());
			testC.getTestReference().setValue(testA);
			testC.getTestReferenceList().setValue(testBList.getValue());
			tc.addQuery(new QryAdd(testC));
		}
		DbIndex.getInstance().executeTransaction(tc, source);
		
		return admin;
	}
	
	public static void showModel() {
		ZODBModel model = DbConfig.getInstance().getModel();
		System.out.println(model.getDescription());
	}

	@Override
	public void handleEvent(EvtEvent e) {
		if (e.getType().equals(ClRequest.RECEIVED_REQUEST_RESPONSE)) {
			ClRequest r = (ClRequest) e.getValue();
			System.out.println("Received request response:");
			System.out.println(ClRequest.toXml(r).toStringReadFormat());
		} else if (e.getType().equals(TestClientSessionEntityLoader.LOADED_ENTITIES)) {
			ClEntityLoader el = (ClEntityLoader) e.getSource();
			MdlDataObject obj = null;
			obj = el.getCollectionObjectByName(TestA.class.getName(),"TestA:1");
			if (obj!=null) {
				TestA testA = (TestA) obj;
				System.out.println("Loaded entity object: " + testA.getName().getValue());
				List<MdlDataObject> objs = testA.getChildObjects(TestC.class.getName() + Generic.SEP_STR + "testReference");
				if (objs!=null) {
					System.out.println("Child TestC objects: " + objs.size());
					for (MdlDataObject cObj: objs) {
						System.out.println("Child TestC object: " + cObj.getName().getValue());
					}
				}
			}
		}
	}
}
