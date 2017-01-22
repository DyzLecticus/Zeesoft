package nl.zeesoft.zadf.controller;

import nl.zeesoft.zodb.Worker;

public final class GuiGridWorker extends Worker {
	private GuiGridController controller = null;
	
	public GuiGridWorker(GuiGridController c) {
		controller = c;
		setSleep(10);
	}
	
	@Override
	public void whileWorking() {
		controller.checkRefreshData();
	}
}
