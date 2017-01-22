package nl.zeesoft.zodb.test.impl;

import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbController;
import nl.zeesoft.zodb.database.DbDataObject;
import nl.zeesoft.zodb.database.DbIndex;
import nl.zeesoft.zodb.database.DbRequestQueue;
import nl.zeesoft.zodb.database.model.MdlModel;
import nl.zeesoft.zodb.database.model.MdlProperty;
import nl.zeesoft.zodb.database.request.ReqAdd;
import nl.zeesoft.zodb.database.request.ReqDataObject;
import nl.zeesoft.zodb.database.request.ReqGet;
import nl.zeesoft.zodb.database.request.ReqGetFilter;
import nl.zeesoft.zodb.database.request.ReqUpdate;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.test.TestConfig;
import nl.zeesoft.zodb.test.TestRequestEventListener;

public class TestMdlModelUpdateIndex {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestConfig.getInstance();

		TestConfig.startDatabase();
		
		//DbConfig.getInstance().setDebugPerformance(true);

		final ReqGet reqGetTestClass3 = new ReqGet(TestMdlModelUpdateIndex.class.getPackage().getName() + ".TestClass3");
		reqGetTestClass3.getProperties().add(MdlProperty.ID);
		reqGetTestClass3.addSubscriber(new TestRequestEventListener() {
			@Override
			public void handleEvent(EvtEvent e) {
				super.handleEvent(e);
				if (e.getValue()==reqGetTestClass3 && reqGetTestClass3.getObjects().size()==0) {
					System.out.println("====> Test add objects to class: " + TestMdlModelUpdateIndex.class.getPackage().getName() + ".TestClass3");
					ReqAdd reqAdd = new ReqAdd(TestMdlModelUpdateIndex.class.getPackage().getName() + ".TestClass3");
					for (int i = 1; i <= 2345; i++) {
						DbDataObject obj = new DbDataObject();
						obj.setPropertyValue("testString",new StringBuilder("Test " + i));
						obj.setPropertyValue("testNumber",new StringBuilder("" + i));
						reqAdd.getObjects().add(new ReqDataObject(obj));
					}
					DbRequestQueue.getInstance().addRequest(reqAdd,TestConfig.getInstance());
				}
			}
		});
		DbRequestQueue.getInstance().addRequest(reqGetTestClass3,TestConfig.getInstance());
		TestConfig.sleep(3000);
		
		System.out.println("====> Test update testNumber property");
		final ReqGet reqGetNumber = new ReqGet(MdlModel.NUMBER_CLASS_FULL_NAME);
		reqGetNumber.addFilter("name",ReqGetFilter.EQUALS,"testNumber");
		reqGetNumber.addSubscriber(new TestRequestEventListener() {
			@Override
			public void handleEvent(EvtEvent e) {
				super.handleEvent(e);
				if (e.getValue()==reqGetNumber) {
					if (!reqGetNumber.hasError()) {
						for (ReqDataObject obj: reqGetNumber.getObjects()) {
							System.out.println("====> Test update number: testNumber");
							ReqUpdate reqUpd = new ReqUpdate(MdlModel.NUMBER_CLASS_FULL_NAME,obj.getDataObject().getId());
							reqUpd.getUpdateObject().setPropertyValue("index",new StringBuilder("true"));
							DbRequestQueue.getInstance().addRequest(reqUpd,TestConfig.getInstance());
						}
					} else {
						// Ignore
					}
				}
			}
		});
		DbRequestQueue.getInstance().addRequest(reqGetNumber,TestConfig.getInstance());
		
		TestConfig.sleep(3000);

		System.out.println("====> Test update MdlModel: " + DbConfig.getInstance().getModel().getClass().getName());
		DbController.getInstance().update();
		
		TestConfig.sleep(10000);
		
		DbIndex.getInstance().debug(TestConfig.getInstance());

		TestConfig.stopDatabase();
	}
}
