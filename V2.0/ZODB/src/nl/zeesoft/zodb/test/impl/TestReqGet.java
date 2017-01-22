package nl.zeesoft.zodb.test.impl;

import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbRequestQueue;
import nl.zeesoft.zodb.database.model.MdlClass;
import nl.zeesoft.zodb.database.model.MdlModel;
import nl.zeesoft.zodb.database.request.ReqGet;
import nl.zeesoft.zodb.database.request.ReqGetFilter;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.test.TestConfig;
import nl.zeesoft.zodb.test.TestModel;
import nl.zeesoft.zodb.test.TestRequestEventListener;

public class TestReqGet {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestConfig.getInstance();

		TestConfig.startDatabase();
		
		MdlClass packageCls = DbConfig.getInstance().getModel().getClassByFullName(MdlModel.PACKAGE_CLASS_FULL_NAME);
		
		System.out.println("====> Test get packages");
		ReqGet req = new ReqGet(packageCls.getFullName());
		req.addSubscriber(new TestRequestEventListener() {
			@Override
			public void handleEvent(EvtEvent e) {
				ReqGet r = (ReqGet) e.getValue();
				if (r.getObjects().size()!=2) {
					System.err.println("!!! " + r.getObjects().size() + "!=2");
				}
			}
		});
		DbRequestQueue.getInstance().addRequest(req,TestConfig.getInstance());
		TestConfig.sleep(1000);

		System.out.println("====> Test get one package");
		req = new ReqGet(packageCls.getFullName());
		req.setStart(1);
		req.setOrderBy("name");
		req.setOrderAscending(false);
		req.addSubscriber(new TestRequestEventListener() {
			@Override
			public void handleEvent(EvtEvent e) {
				ReqGet r = (ReqGet) e.getValue();
				if (r.getObjects().size()!=1) {
					System.err.println("!!! " + r.getObjects().size() + "!=1");
				} else {
					StringBuilder value = r.getObjects().get(0).getDataObject().getPropertyValue("name");
					if (!value.toString().equals(MdlModel.class.getPackage().getName())) {
						System.out.println("!!! " + value.toString() + "!=" + MdlModel.class.getPackage().getName());
					}
				}
			}
		});
		DbRequestQueue.getInstance().addRequest(req,TestConfig.getInstance());
		TestConfig.sleep(1000);

		MdlClass testCls = TestModel.getTestClass1();
		
		System.out.println("====> Test subset of test classes");
		req = new ReqGet(testCls.getFullName());
		req.addFilter("testNumberProperty",ReqGetFilter.LESS,"15");
		req.setStart(10);
		req.setOrderBy("testNumberProperty");
		req.setOrderAscending(false);
		req.addSubscriber(new TestRequestEventListener() {
			@Override
			public void handleEvent(EvtEvent e) {
				ReqGet r = (ReqGet) e.getValue();
				if (r.getObjects().size()!=4) {
					System.err.println("!!! " + r.getObjects().size() + "!=4");
				} else {
					StringBuilder value = r.getObjects().get(0).getDataObject().getPropertyValue("testNumberProperty");
					if (!value.toString().equals("4")) {
						System.out.println("!!! " + value.toString() + "!=4 (hasValue: " + r.getObjects().get(0).getDataObject().hasPropertyValue("testNumberProperty") + ")");
					}
				}
			}
		});
		DbRequestQueue.getInstance().addRequest(req,TestConfig.getInstance());
		TestConfig.sleep(1000);
		
		TestConfig.stopDatabase();
	}
}
