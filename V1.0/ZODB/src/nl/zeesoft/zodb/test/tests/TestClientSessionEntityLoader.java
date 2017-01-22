package nl.zeesoft.zodb.test.tests;

import java.util.Date;

import nl.zeesoft.zodb.client.ClEntityLoader;
import nl.zeesoft.zodb.client.ClFactory;
import nl.zeesoft.zodb.client.ClSession;
import nl.zeesoft.zodb.client.ClSessionInitializationWorker;
import nl.zeesoft.zodb.client.ClSessionManager;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbFactory;
import nl.zeesoft.zodb.database.DbIndex;
import nl.zeesoft.zodb.database.DbIndexSaveWorker;
import nl.zeesoft.zodb.database.DbSessionServerWorker;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.model.impl.DbUser;
import nl.zeesoft.zodb.test.model.TestConfig;
import nl.zeesoft.zodb.test.model.impl.TestA;
import nl.zeesoft.zodb.test.model.impl.TestB;
import nl.zeesoft.zodb.test.model.impl.TestC;

public class TestClientSessionEntityLoader {
	public static final String LOADED_ENTITIES = "LOADED_ENTITIES";
	
	ClEntityLoader entityLoader = null;
			
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
		s.getRequestQueue().addRequest(s.getNewAuthorizationRequest(admin.getName().getValue(), admin.getPassword().getValue()),c);
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		String[] entities = {TestA.class.getName(),TestB.class.getName(),TestC.class.getName()}; 
		ClEntityLoader el = new ClEntityLoader(s) {

			/* (non-Javadoc)
			 * @see nl.zeesoft.zodb.client.ClEntityLoader#handleEvent(nl.zeesoft.zodb.event.EvtEvent)
			 */
			@Override
			public void handleEvent(EvtEvent e) {
				super.handleEvent(e);
				publishEvent(new EvtEvent(LOADED_ENTITIES,this,LOADED_ENTITIES));
			}
			
		};
		el.initialize(entities);
		el.addSubscriber(new TestConfig());
		el.loadEntities();
		
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
