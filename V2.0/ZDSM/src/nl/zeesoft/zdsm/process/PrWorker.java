package nl.zeesoft.zdsm.process;

import nl.zeesoft.zodb.Worker;

public class PrWorker extends Worker {
	private PrHandler handler = null;
	
	public PrWorker(PrHandler handler) {
		setSleep(1);
		this.handler = handler;
	}
	
	@Override
	public void start() {
		super.start();
		//Messenger.getInstance().debug(this,"Started handler worker");
	}

	@Override
	public void stop() {
		super.stop();
		//Messenger.getInstance().debug(this,"Stopped handler worker");
	}

	@Override
	public void whileWorking() {
		handler.work();
		stop();
	}

	/**
	 * @return the handler
	 */
	protected PrHandler getResponse() {
		return handler;
	}
}
