package nl.zeesoft.zodb.test.tests;

import java.util.Date;

import nl.zeesoft.zodb.client.ClConfig;
import nl.zeesoft.zodb.client.ClControlSession;
import nl.zeesoft.zodb.client.ClFactory;
import nl.zeesoft.zodb.client.ClRequest;
import nl.zeesoft.zodb.client.ClSessionInitializationWorker;
import nl.zeesoft.zodb.client.ClSessionManager;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbControlServerWorker;
import nl.zeesoft.zodb.database.DbFactory;
import nl.zeesoft.zodb.database.DbIndex;
import nl.zeesoft.zodb.database.DbIndexSaveWorker;
import nl.zeesoft.zodb.database.DbSessionServerWorker;
import nl.zeesoft.zodb.event.EvtEventSubscriber;
import nl.zeesoft.zodb.test.model.TestConfig;

public class TestClientSessionControl {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DbConfig c = DbConfig.getInstance();
		c.setInstallDir(new TestConfig().getTestDir());
		
		DbFactory dbfac = new DbFactory();
		dbfac.loadDatabase(dbfac);
		
		System.out.println("Index size: " + DbIndex.getInstance().getSize());

		ClFactory clFac = new ClFactory();
		clFac.loadClient();
		
		ClControlSession cs = null;
		Date start = null;
		
		DbIndexSaveWorker.getInstance().start();
		DbControlServerWorker.getInstance().start();
		DbSessionServerWorker.getInstance().start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		start = new Date();
		ClSessionInitializationWorker sw = ClSessionManager.getInstance().initializeNewControlSession();
		while (sw.isWorking()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		cs = (ClControlSession) sw.getSession();
		System.out.println("Started client control session: " + cs.getSessionId() + ", in: " + ((new Date()).getTime() - start.getTime()) + " ms");

		ClRequest r = null;
		EvtEventSubscriber sub = (EvtEventSubscriber) DbConfig.getInstance().getModel();
		
		r = cs.getNewAuthorizationRequest(ClConfig.getInstance().getUserName(),ClConfig.getInstance().getUserPassword());
		r.addSubscriber(sub);
		System.out.println("Adding authorization request to queue");
		cs.getRequestQueue().addRequest(r, cs);
		System.out.println("Added authorization request to queue");

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		r = cs.getNewServerIsWorkingRequest();
		r.addSubscriber(sub);
		System.out.println("Adding server is working request to queue");
		cs.getRequestQueue().addRequest(r, cs);
		System.out.println("Added server is working request to queue");
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		r = cs.getNewStopServerRequest();
		r.addSubscriber(sub);
		System.out.println("Adding stop server request to queue");
		cs.getRequestQueue().addRequest(r, cs);
		System.out.println("Added stop server request to queue");
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		r = cs.getNewStartServerRequest();
		r.addSubscriber(sub);
		System.out.println("Adding start server request to queue");
		cs.getRequestQueue().addRequest(r, cs);
		System.out.println("Added start server request to queue");
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		r = cs.getNewStopZODBProgramRequest();
		r.addSubscriber(sub);
		System.out.println("Adding stop ZODB program request to queue");
		cs.getRequestQueue().addRequest(r, cs);
		System.out.println("Added stop ZODB program request to queue");
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if (cs!=null) {
			start = new Date();
			ClSessionManager.getInstance().stopSession(cs.getSessionId());
			System.out.println("Stopped client control session: " + cs.getSessionId() + ", in: " + ((new Date()).getTime() - start.getTime()) + " ms");
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		DbSessionServerWorker.getInstance().stop();
		DbControlServerWorker.getInstance().stop();
		DbIndexSaveWorker.getInstance().stop();
	}
}
