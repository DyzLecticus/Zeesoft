package nl.zeesoft.zwc;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class CrawlerWorker extends Worker {
	private Crawler crawler	= null;
	
	public CrawlerWorker(Messenger msgr, WorkerUnion union, Crawler crawler) {
		super(msgr, union);
		this.crawler = crawler;
	}

	@Override
	public void whileWorking() {
		if (crawler.parseNextUrl()) {
			stop();
		}
	}
}
