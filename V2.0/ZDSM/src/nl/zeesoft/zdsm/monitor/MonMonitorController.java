package nl.zeesoft.zdsm.monitor;

import nl.zeesoft.zodb.database.DbController;
import nl.zeesoft.zodb.database.model.helpers.HlpControllerObject;
import nl.zeesoft.zodb.event.EvtEvent;

public class MonMonitorController extends HlpControllerObject {
	private static MonMonitorController		controller		= null;
	
	private MonMonitorWorker				monitorWorker	= new MonMonitorWorker();
	private MonRemoverWorker				removerWorker	= new MonRemoverWorker();
	
	private MonMonitorController() {
		// Singleton
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	
	public static MonMonitorController getInstance() {
		if (controller==null) {
			controller = new MonMonitorController();
		}
		return controller;
	}
	
	@Override
	public void handleEvent(EvtEvent e) {
		if (e.getType().equals(DbController.DB_STARTED) && e.getValue().toString().equals("true")) {
			initialize();
		} else if (e.getType().equals(DbController.DB_UPDATING_MODEL)) {
			monitorWorker.stop();
			removerWorker.stop();
		} else if (e.getType().equals(DbController.DB_UPDATED_MODEL)) {
			monitorWorker.start();
			removerWorker.start();
		} else if (e.getType().equals(DbController.DB_STOPPING)) {
			monitorWorker.stop();
			removerWorker.stop();
		}
	}

	@Override
	protected void initialize() {
		monitorWorker.start();
		removerWorker.start();
	}
}
