package nl.zeesoft.zacs.crawler;

import nl.zeesoft.zodb.Worker;

public class CrwManagerWorker extends Worker {
	public CrwManagerWorker() {
		setSleep(10000);
	}

	@Override
	public void start() {
		//Messenger.getInstance().debug(this,"Starting manager ...");
		super.start();
		//Messenger.getInstance().debug(this,"Started manager");
	}
	
	@Override
	public void stop() {
		//Messenger.getInstance().debug(this,"Stopping manager ...");
		super.stop();
		//Messenger.getInstance().debug(this,"Stopped manager");
	}
	
	@Override
	public void whileWorking() {
		CrwManager.getInstance().refreshCrawlers();
	}
}
