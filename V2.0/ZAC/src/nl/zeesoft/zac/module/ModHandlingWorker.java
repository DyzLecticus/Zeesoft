package nl.zeesoft.zac.module;

import nl.zeesoft.zodb.Worker;

public class ModHandlingWorker extends Worker {
	private ModHander		handler			= null;
	private int				refresh			= 0;

	protected ModHandlingWorker() {
		setSleep(1000);
		this.handler = new ModHander();
	}
	
	@Override
	public void start() {
		handler.refreshModules();
		super.start();
		//Messenger.getInstance().debug(this,"Started handling worker");
	}

	@Override
	public void stop() {
		handler.stopHandling();
		super.stop();
		//Messenger.getInstance().debug(this,"Stopped handling worker");
	}

	@Override
	public void whileWorking() {
		handler.handle();
		refresh++;
		if (refresh>10) {
			handler.refreshModules();
			refresh = 0;
		}
	}
}
