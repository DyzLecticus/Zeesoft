package nl.zeesoft.zacs.crawler;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zacs.database.model.Crawler;
import nl.zeesoft.zodb.Locker;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.DbController;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.event.EvtEventExceptionSubscriber;

public class CrwManager extends Locker implements EvtEventExceptionSubscriber {
	private static CrwManager				manager				= null;
	private	CrwManagerWorker				worker				= null;
	private List<CrwCrawlerWorker> 			crawlerWorkers 		= new ArrayList<CrwCrawlerWorker>();
	private CrwController					controller			= null;
	
	private CrwManager() {
		// Singleton
	}
	
	public static CrwManager getInstance() {
		if (manager==null) {
			manager = new CrwManager();
		}
		return manager;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
	    throw new CloneNotSupportedException(); 
	}

	@Override
	public void handleEventException(EvtEvent evt, Exception ex) {
		stop();
	}

	@Override
	public void handleEvent(EvtEvent e) {
		if (e.getType().equals(DbController.DB_STARTED) && e.getValue().toString().equals("true")) {
			initialize();
			start();
		} else if (e.getType().equals(DbController.DB_UPDATING_MODEL)) {
			stop();
		} else if (e.getType().equals(DbController.DB_UPDATED_MODEL)) {
			start();
		} else if (e.getType().equals(DbController.DB_STOPPING)) {
			stop();
		} 
	}

	protected void refreshCrawlers() {
		lockMe(this);
		controller.reinitialize();
		for (Crawler crw: controller.getCrawlersAsList()) {
			CrwCrawlerWorker foundWorker = null;
			for (CrwCrawlerWorker worker: crawlerWorkers) {
				if (worker.getCrawlerId()==crw.getId()) {
					foundWorker = worker;
					break;
				}
			}
			if (crw.isCrawl() && foundWorker==null) {
				CrwCrawlerWorker newWorker = new CrwCrawlerWorker(crw);
				crawlerWorkers.add(newWorker);
				newWorker.start();
			} else if (!crw.isCrawl() && foundWorker!=null) {
				foundWorker.stop();
				crawlerWorkers.remove(foundWorker);
			}
		}
		unlockMe(this);
	}
		
	private void initialize() {
		lockMe(this);
		worker = new CrwManagerWorker();
		controller = new CrwController();
		controller.initialize();
		for (Crawler crw: controller.getCrawlersAsList()) {
			if (crw.isCrawl()) {
				CrwCrawlerWorker worker = new CrwCrawlerWorker(crw);
				crawlerWorkers.add(worker);
			}
		}
		unlockMe(this);
	}

	private void start() {
		lockMe(this);
		worker.start();
		for (CrwCrawlerWorker worker: crawlerWorkers) {
			worker.start();
		}
		unlockMe(this);
	}
	
	private void stop() {
		lockMe(this);
		worker.stop();
		for (CrwCrawlerWorker worker: crawlerWorkers) {
			worker.stop();
			while(worker.isWorking()) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					Messenger.getInstance().debug(this,"Waiting for crawlers was interrupted");
				}
			}
		}
		unlockMe(this);
	}
}
