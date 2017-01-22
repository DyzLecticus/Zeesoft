package nl.zeesoft.zodb.test.impl;

import nl.zeesoft.zodb.database.DbRequestQueue;
import nl.zeesoft.zodb.database.model.MdlClass;
import nl.zeesoft.zodb.database.request.ReqGetFilter;
import nl.zeesoft.zodb.database.request.ReqUpdate;
import nl.zeesoft.zodb.test.TestConfig;
import nl.zeesoft.zodb.test.TestModel;

public class TestReqUpdate {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestConfig.getInstance();

		TestConfig.startDatabase();

		//DbConfig.getInstance().setDebugPerformance(true);
		
		MdlClass testCls2 = TestModel.getTestClass2();
		System.out.println("====> Test update " + testCls2.getFullName() + " objects");
		ReqUpdate req = new ReqUpdate(testCls2.getFullName());
		StringBuilder val = new StringBuilder("Test update");
		req.getUpdateObject().setPropertyValue("testStringProperty",val);
		req.getGet().addFilter("testStringProperty",true,ReqGetFilter.EQUALS,val.toString());
		req.getGet().setStart(0);
		req.getGet().setLimit(10);
		DbRequestQueue.getInstance().addRequest(req,TestConfig.getInstance());

		TestConfig.sleep(1000);

		TestConfig.stopDatabase();
	}
}
