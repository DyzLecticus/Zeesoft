package nl.zeesoft.zodb.test.impl;

import nl.zeesoft.zodb.database.DbIndex;
import nl.zeesoft.zodb.database.DbRequestQueue;
import nl.zeesoft.zodb.database.model.MdlClass;
import nl.zeesoft.zodb.database.request.ReqGetFilter;
import nl.zeesoft.zodb.database.request.ReqRemove;
import nl.zeesoft.zodb.test.TestConfig;
import nl.zeesoft.zodb.test.TestModel;

public class TestReqRemove {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestConfig.getInstance();

		TestConfig.startDatabase();

		//DbConfig.getInstance().setDebugPerformance(true);

		MdlClass testCls1 = TestModel.getTestClass1();
		System.out.println("====> Test remove " + testCls1.getFullName() + " object error");
		ReqRemove req = new ReqRemove(testCls1.getFullName());
		req.getGet().setStart(10);
		req.getGet().setLimit(1);
		DbRequestQueue.getInstance().addRequest(req,TestConfig.getInstance());

		TestConfig.sleep(1000);
		
		StringBuilder val = new StringBuilder("Test update");

		MdlClass testCls2 = TestModel.getTestClass2();
		System.out.println("====> Test update " + testCls2.getFullName() + " objects");
		req = new ReqRemove(testCls2.getFullName());
		req.getGet().addFilter("testStringProperty",ReqGetFilter.EQUALS,val.toString());
		DbRequestQueue.getInstance().addRequest(req,TestConfig.getInstance());
		
		TestConfig.sleep(1000);
		
		DbIndex.getInstance().debug(TestConfig.getInstance());
		
		TestConfig.stopDatabase();
	}
}
