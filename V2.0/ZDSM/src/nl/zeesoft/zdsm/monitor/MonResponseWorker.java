package nl.zeesoft.zdsm.monitor;

import nl.zeesoft.zodb.Worker;

public class MonResponseWorker extends Worker {
	private MonResponse response = null;
	
	protected MonResponseWorker(MonResponse response) {
		setSleep(1);
		this.response = response;
	}
	
	@Override
	public void start() {
		super.start();
		//Messenger.getInstance().debug(this,"Started response worker");
	}

	@Override
	public void stop() {
		super.stop();
		//Messenger.getInstance().debug(this,"Stopped response worker");
	}

	@Override
	public void whileWorking() {
		response.work();
		stop();
	}

	/**
	 * @return the response
	 */
	protected MonResponse getResponse() {
		return response;
	}
}
