package nl.zeesoft.zodb.batch;

import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.Worker;

public final class BtcBatchWorker extends Worker {
	private static BtcBatchWorker batchWorker = null;
	
	private BtcBatchWorker() {
		setSleep(1000);
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
	    throw new CloneNotSupportedException(); 
	}
	
	public static BtcBatchWorker getInstance() {
		if (batchWorker==null) {
			batchWorker = new BtcBatchWorker();
		}
		return batchWorker;
	}
	
	@Override
	public void start() {
		Messenger.getInstance().debug(this, "Starting batch ...");
		super.start();
		Messenger.getInstance().debug(this, "Started batch");
    }

	@Override
    public void stop() {
		Messenger.getInstance().debug(this, "Stopping batch ...");
		BtcBatch.getInstance().stopPrograms();
		super.stop();
		while(isWorking()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				//e.printStackTrace();
			}
		}
		Messenger.getInstance().debug(this, "Stopped batch");
    }
	
	@Override
	public void whileWorking() {
		BtcBatch.getInstance().updateAndStartPrograms(this);
	}
}
