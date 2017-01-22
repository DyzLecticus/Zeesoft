package nl.zeesoft.zodb.protocol;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.Worker;
import nl.zeesoft.zodb.WorkerUnion;
import nl.zeesoft.zodb.database.DbCacheSaveWorker;
import nl.zeesoft.zodb.database.DbControlServerWorker;
import nl.zeesoft.zodb.database.DbIndexPreloadManager;
import nl.zeesoft.zodb.database.DbIndexSaveWorker;
import nl.zeesoft.zodb.database.DbWhiteListWorker;

public class PtcServerControlStopProgramWorker extends Worker {
	private PtcServerControl control = null;
	
	public PtcServerControlStopProgramWorker(PtcServerControl control) {
		setSleep(1);
		this.control = control;
	}

	@Override
	public void whileWorking() {
		try {
			control.stopServer();
			control.stopBatch();
			DbControlServerWorker.getInstance().stop();
			DbWhiteListWorker.getInstance().stop();
			if (DbIndexPreloadManager.getInstance().isWorking()) {
				DbIndexPreloadManager.getInstance().stop();
			}
			DbIndexSaveWorker.getInstance().stop();
			DbCacheSaveWorker.getInstance().stop();
			WorkerUnion.getInstance().stopWorkers(this);
			Messenger.getInstance().debug(this, "Stopping program ...");
			stop();
			System.exit(0);
		} catch (Exception e) {
			String callStack = Generic.getCallStackString(e.getStackTrace(),"");
			Messenger.getInstance().error(this, "An error occured while stopping the program: " + e.getMessage() + "\n" + callStack);
			stop();
			System.exit(1);
		} finally {
			stop();
		}
	}
}
