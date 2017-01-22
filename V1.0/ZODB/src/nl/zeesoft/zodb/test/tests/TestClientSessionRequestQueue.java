package nl.zeesoft.zodb.test.tests;

import java.util.Date;

import nl.zeesoft.zodb.client.ClFactory;
import nl.zeesoft.zodb.client.ClRequest;
import nl.zeesoft.zodb.client.ClSession;
import nl.zeesoft.zodb.client.ClSessionInitializationWorker;
import nl.zeesoft.zodb.client.ClSessionManager;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbFactory;
import nl.zeesoft.zodb.database.DbIndex;
import nl.zeesoft.zodb.database.DbIndexSaveWorker;
import nl.zeesoft.zodb.database.DbSessionServerWorker;
import nl.zeesoft.zodb.database.query.QryFetch;
import nl.zeesoft.zodb.database.query.QryFetchList;
import nl.zeesoft.zodb.event.EvtEventSubscriber;
import nl.zeesoft.zodb.model.impl.DbUser;
import nl.zeesoft.zodb.test.model.TestConfig;
import nl.zeesoft.zodb.test.model.impl.TestA;

public class TestClientSessionRequestQueue {

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
		
		ClSession s = null;
		Date start = null;
		
		DbIndexSaveWorker.getInstance().start();
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
		s = sw.getSession();
		System.out.println("Started client session: " + s.getSessionId() + ", in: " + ((new Date()).getTime() - start.getTime()) + " ms");

		DbUser admin = DbConfig.getInstance().getModel().getAdminUser(s);
		
		QryFetch fetch = null;
		ClRequest r = null;
		EvtEventSubscriber sub = (EvtEventSubscriber) DbConfig.getInstance().getModel();
		
		fetch = new QryFetch(TestA.class.getName());
		r = s.getRequestQueue().getNewRequest(s);
		r.setQueryRequest(new QryFetchList(admin,fetch));
		r.addSubscriber(sub);
		System.out.println("Adding fetch request to queue");
		s.getRequestQueue().addRequest(r, s);
		System.out.println("Added fetch request to queue");
		
		r = s.getNewAuthorizationRequest("userAdmin",DbConfig.getInstance().encodePassword(new StringBuffer("1userAdmin!")));
		r.addSubscriber(sub);
		System.out.println("Adding authorization request to queue");
		s.getRequestQueue().addRequest(r, s);
		System.out.println("Added authorization request to queue");

		fetch = new QryFetch(TestA.class.getName());
		r = s.getRequestQueue().getNewRequest(s);
		r.setQueryRequest(new QryFetchList(admin,fetch));
		r.addSubscriber(sub);
		System.out.println("Adding fetch request to queue");
		s.getRequestQueue().addRequest(r, s);
		System.out.println("Added fetch request to queue");
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if (s!=null) {
			start = new Date();
			ClSessionManager.getInstance().stopSession(s.getSessionId());
			System.out.println("Stopped client session: " + s.getSessionId() + ", in: " + ((new Date()).getTime() - start.getTime()) + " ms");
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		DbSessionServerWorker.getInstance().stop();
		DbIndexSaveWorker.getInstance().stop();
	}

}
