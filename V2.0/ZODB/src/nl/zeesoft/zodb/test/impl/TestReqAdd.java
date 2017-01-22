package nl.zeesoft.zodb.test.impl;

import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbDataObject;
import nl.zeesoft.zodb.database.DbIndex;
import nl.zeesoft.zodb.database.DbRequestQueue;
import nl.zeesoft.zodb.database.model.MdlClass;
import nl.zeesoft.zodb.database.model.MdlModel;
import nl.zeesoft.zodb.database.request.ReqAdd;
import nl.zeesoft.zodb.database.request.ReqDataObject;
import nl.zeesoft.zodb.test.TestConfig;
import nl.zeesoft.zodb.test.TestModel;

public class TestReqAdd {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestConfig.getInstance();

		TestConfig.startDatabase();
		
		//DbConfig.getInstance().setDebugPerformance(true);
		
		MdlClass packageCls = DbConfig.getInstance().getModel().getClassByFullName(MdlModel.PACKAGE_CLASS_FULL_NAME);
		
		System.out.println("====> Test add packages");
		ReqAdd req = new ReqAdd(packageCls.getFullName());
		DbDataObject dataObject = new DbDataObject();
		dataObject.setPropertyValue("name",new StringBuilder(MdlModel.class.getPackage().getName()));
		req.getObjects().add(new ReqDataObject(dataObject));
		dataObject = new DbDataObject();
		dataObject.setPropertyValue("name",new StringBuilder(TestConfig.class.getPackage().getName()));
		req.getObjects().add(new ReqDataObject(dataObject));
		DbRequestQueue.getInstance().addRequest(req,TestConfig.getInstance());

		TestConfig.sleep(1000);

		MdlClass testCls1 = TestModel.getTestClass1();
		System.out.println("====> Test add " + testCls1.getFullName() + " objects");
		req = new ReqAdd(testCls1.getFullName());
		for (int i = 1; i <= 220; i++) {
			dataObject = new DbDataObject();
			dataObject.setPropertyValue("testStringProperty",new StringBuilder("Test string " + i));
			dataObject.setPropertyValue("testNumberProperty",new StringBuilder("" + i));
			req.getObjects().add(new ReqDataObject(dataObject));
		}
		DbRequestQueue.getInstance().addRequest(req,TestConfig.getInstance());

		TestConfig.sleep(1000);
		
		System.out.println("====> Test property value too large (maximize value to 9999)");
		req = new ReqAdd(testCls1.getFullName());
		dataObject = new DbDataObject();
		dataObject.setPropertyValue("testStringProperty",new StringBuilder("Bla bla bla"));
		dataObject.setPropertyValue("testNumberProperty",new StringBuilder("12345"));
		req.getObjects().add(new ReqDataObject(dataObject));
		DbRequestQueue.getInstance().addRequest(req,TestConfig.getInstance());
		
		if (!req.getObjects().get(0).getDataObject().getPropertyValue("testNumberProperty").toString().equals("9999")) {
			System.err.println("!!! Value not maximized: " + req.getObjects().get(0).getDataObject().getPropertyValue("testNumberProperty") + "!=9999)");
		}

		TestConfig.sleep(1000);

		System.out.println("====> Test property value not a number error");
		req = new ReqAdd(testCls1.getFullName());
		dataObject = new DbDataObject();
		dataObject.setPropertyValue("testStringProperty",new StringBuilder("Bla bla bla"));
		dataObject.setPropertyValue("testNumberProperty",new StringBuilder("Bla"));
		req.getObjects().add(new ReqDataObject(dataObject));
		DbRequestQueue.getInstance().addRequest(req,TestConfig.getInstance());

		TestConfig.sleep(1000);

		MdlClass testCls2 = TestModel.getTestClass2();
		System.out.println("====> Test add " + testCls2.getFullName() + " objects");
		req = new ReqAdd(testCls2.getFullName());
		
		for (long l = 1; l <= 220; l++) {
			for (int i = 1; i<20; i++) {
				dataObject = new DbDataObject();
				dataObject.setPropertyValue("testStringProperty",new StringBuilder("Test string " + (i * l)));
				dataObject.setLinkValue("testLinkProperty",l);
				req.getObjects().add(new ReqDataObject(dataObject));
			}
		}
		DbRequestQueue.getInstance().addRequest(req,TestConfig.getInstance());

		TestConfig.sleep(15000);
		
		DbIndex.getInstance().debug(TestConfig.getInstance());

		TestConfig.stopDatabase();
	}
}
