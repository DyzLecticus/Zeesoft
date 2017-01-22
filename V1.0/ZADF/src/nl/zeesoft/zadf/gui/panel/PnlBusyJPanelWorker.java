package nl.zeesoft.zadf.gui.panel;

import nl.zeesoft.zodb.Worker;

public class PnlBusyJPanelWorker extends Worker {
	private PnlBusyJPanel panel = new PnlBusyJPanel();
	
	public PnlBusyJPanelWorker() {
		setSleep(100);
	}
	
	@Override
	public void whileWorking() {
		panel.step();
		panel.repaint();
	}

	/**
	 * @return the panel
	 */
	public PnlBusyJPanel getPanel() {
		return panel;
	}
}
