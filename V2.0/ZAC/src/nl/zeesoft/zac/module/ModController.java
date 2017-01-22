package nl.zeesoft.zac.module;

import nl.zeesoft.zodb.database.DbController;
import nl.zeesoft.zodb.database.model.helpers.HlpControllerObject;
import nl.zeesoft.zodb.event.EvtEvent;

public class ModController extends HlpControllerObject {
	private static ModController		controller		= null;
	
	private ModHandlingWorker			handlingWorker	= new ModHandlingWorker();
	
	private ModController() {
		// Singleton
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	
	public static ModController getInstance() {
		if (controller==null) {
			controller = new ModController();
		}
		return controller;
	}
	
	@Override
	public void handleEvent(EvtEvent e) {
		if (e.getType().equals(DbController.DB_STARTED) && e.getValue().toString().equals("true")) {
			initialize();
		} else if (e.getType().equals(DbController.DB_UPDATING_MODEL)) {
			handlingWorker.stop();
		} else if (e.getType().equals(DbController.DB_UPDATED_MODEL)) {
			handlingWorker.start();
		} else if (e.getType().equals(DbController.DB_STOPPING)) {
			handlingWorker.stop();
		}
	}

	@Override
	protected void initialize() {
		handlingWorker.start();
	}
}
