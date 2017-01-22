package nl.zeesoft.zadf.gui.panel;

import nl.zeesoft.zadf.gui.GuiPanel;

public class PnlBusy extends GuiPanel {
	private PnlBusyJPanelWorker worker = new PnlBusyJPanelWorker();
	
	public PnlBusy(String name,int row,int column) {
		super(name,row,column);
	}

	@Override
	public void renderComponent() {
		if (getComponent()==null) {
			setComponent(worker.getPanel());
		}
	}
	
	public void start() {
		worker.start();
	}
	
	public void stop() {
		worker.stop();
	}
}
