package nl.zeesoft.zals.simulator;

import nl.zeesoft.zodb.database.DbController;
import nl.zeesoft.zodb.database.model.helpers.HlpControllerObject;
import nl.zeesoft.zodb.event.EvtEvent;

public class SimController extends HlpControllerObject {
	private static SimController		controller			= null;
	
	private SimEnvironmentWorker		environmentWorker	= new SimEnvironmentWorker();
	
	private SimController() {
		// Singleton
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	
	public static SimController getInstance() {
		if (controller==null) {
			controller = new SimController();
		}
		return controller;
	}
	
	@Override
	public void handleEvent(EvtEvent e) {
		if (e.getType().equals(DbController.DB_STARTED) && e.getValue().toString().equals("true")) {
			initialize();
			environmentWorker.start();
		} else if (e.getType().equals(DbController.DB_UPDATING_MODEL)) {
			environmentWorker.stop();
		} else if (e.getType().equals(DbController.DB_UPDATED_MODEL)) {
			environmentWorker.start();
		} else if (e.getType().equals(DbController.DB_STOPPING)) {
			environmentWorker.stop();
		}
	}

	@Override
	protected void initialize() {
		environmentWorker.initialize();
	}
}
