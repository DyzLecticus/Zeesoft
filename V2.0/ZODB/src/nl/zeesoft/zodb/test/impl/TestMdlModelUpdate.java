package nl.zeesoft.zodb.test.impl;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbController;
import nl.zeesoft.zodb.database.DbDataObject;
import nl.zeesoft.zodb.database.DbIndex;
import nl.zeesoft.zodb.database.DbRequestQueue;
import nl.zeesoft.zodb.database.model.MdlModel;
import nl.zeesoft.zodb.database.model.MdlProperty;
import nl.zeesoft.zodb.database.request.ReqAdd;
import nl.zeesoft.zodb.database.request.ReqDataObject;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.test.TestConfig;
import nl.zeesoft.zodb.test.TestRequestEventListener;

public class TestMdlModelUpdate {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestConfig.getInstance();

		TestConfig.startDatabase();
		
		//DbConfig.getInstance().setDebugPerformance(true);
		
		System.out.println("====> Test add package: " + TestMdlModelUpdate.class.getPackage().getName());
		final ReqAdd reqAddPackage = new ReqAdd(MdlModel.PACKAGE_CLASS_FULL_NAME);
		DbDataObject dataObject = new DbDataObject();
		dataObject.setPropertyValue("name",new StringBuilder(TestMdlModelUpdate.class.getPackage().getName()));
		reqAddPackage.getObjects().add(new ReqDataObject(dataObject));
		reqAddPackage.addSubscriber(new TestRequestEventListener() {
			private ReqAdd reqAddClass = new ReqAdd(MdlModel.CLASS_CLASS_FULL_NAME);
			private ReqAdd reqAddString = new ReqAdd(MdlModel.STRING_CLASS_FULL_NAME);
			private List<Long> classVal = null;
			@Override
			public void handleEvent(EvtEvent e) {
				super.handleEvent(e);
				if (e.getValue()==reqAddPackage) {
					if (!reqAddPackage.hasError()) {
						System.out.println("====> Test add class: TestClass3");
						long packageId = Long.parseLong(reqAddPackage.getObjects().get(0).getDataObject().getPropertyValue(MdlProperty.ID).toString());
						DbDataObject dataObject = new DbDataObject();
						dataObject.setLinkValue("package",packageId);
						dataObject.setPropertyValue("name",new StringBuilder("TestClass3"));
						dataObject.setPropertyValue("abstract",new StringBuilder("false"));
						reqAddClass.getObjects().add(new ReqDataObject(dataObject));
						reqAddClass.addSubscriber(this);
						DbRequestQueue.getInstance().addRequest(reqAddClass,TestConfig.getInstance());
					} else {
						// Ignore
					}
				} else if (e.getValue()==reqAddClass) {
					System.out.println("====> Test add string: testString");
					long classId = Long.parseLong(reqAddClass.getObjects().get(0).getDataObject().getPropertyValue(MdlProperty.ID).toString());
					classVal = new ArrayList<Long>();
					classVal.add(classId);
					DbDataObject dataObject = new DbDataObject();
					dataObject.setLinkValue("class",classVal);
					dataObject.setPropertyValue("name",new StringBuilder("testString"));
					dataObject.setPropertyValue("index",new StringBuilder("true"));
					dataObject.setPropertyValue("maxLength",new StringBuilder("64"));
					reqAddString.getObjects().add(new ReqDataObject(dataObject));
					reqAddString.addSubscriber(this);
					DbRequestQueue.getInstance().addRequest(reqAddString,TestConfig.getInstance());
				} else if (e.getValue()==reqAddString) {
					System.out.println("====> Test add number: testNumber");
					DbDataObject dataObject = new DbDataObject();
					dataObject.setLinkValue("class",classVal);
					dataObject.setPropertyValue("name",new StringBuilder("testNumber"));
					dataObject.setPropertyValue("index",new StringBuilder("false"));
					dataObject.setPropertyValue("minValue",new StringBuilder("0"));
					dataObject.setPropertyValue("maxValue",new StringBuilder("9999"));
					ReqAdd reqAddNumber = new ReqAdd(MdlModel.NUMBER_CLASS_FULL_NAME);
					reqAddNumber.getObjects().add(new ReqDataObject(dataObject));
					DbRequestQueue.getInstance().addRequest(reqAddNumber,TestConfig.getInstance());
				}
			}
		});
		DbRequestQueue.getInstance().addRequest(reqAddPackage,TestConfig.getInstance());

		TestConfig.sleep(3000);
		
		System.out.println("====> Test update MdlModel: " + DbConfig.getInstance().getModel().getClass().getName());
		DbController.getInstance().update();
		
		TestConfig.sleep(10000);
		
		DbIndex.getInstance().debug(TestConfig.getInstance());

		TestConfig.stopDatabase();
	}
}
