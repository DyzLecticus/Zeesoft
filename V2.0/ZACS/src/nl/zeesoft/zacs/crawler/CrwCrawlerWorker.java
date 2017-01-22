package nl.zeesoft.zacs.crawler;

import nl.zeesoft.zacs.database.model.Crawler;
import nl.zeesoft.zodb.Worker;

public class CrwCrawlerWorker extends Worker {
	private CrwCrawler crawler = null;
	
	public CrwCrawlerWorker(Crawler crawler) {
		setSleep(1000);
		this.crawler = new CrwCrawler(crawler);
	}

	protected long getCrawlerId() {
		return crawler.getCrawlerId();
	}
	
	@Override
	public void start() {
		boolean start = crawler.initialize();
		if (start) {
			//Messenger.getInstance().debug(this,"Starting crawler ...");
			super.start();
			//Messenger.getInstance().debug(this,"Started crawler");
		}
	}
	
	@Override
	public void stop() {
		//Messenger.getInstance().debug(this,"Stopping crawler ...");
		super.stop();
		//Messenger.getInstance().debug(this,"Stopped crawler");
	}
	
	@Override
	public void whileWorking() {
		boolean done = crawler.parseNextUrl();
		if (done) {
			stop();
		}
	}
}
