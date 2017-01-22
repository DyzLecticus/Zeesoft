package nl.zeesoft.zadf.controller.impl;

import nl.zeesoft.zadf.controller.GuiWindowController;
import nl.zeesoft.zadf.gui.GuiObject;
import nl.zeesoft.zadf.gui.GuiWindow;
import nl.zeesoft.zadf.gui.panel.PnlBusy;

public class BusyWindowController extends GuiWindowController {
	private PnlBusy busyPanel = null;
	
	public BusyWindowController(GuiWindow busyWindow) {
		super(busyWindow);
		GuiObject obj = busyWindow.getPanelObjects().getGuiObjectByName(ZADFFactory.PANEL_BUSY);
		if ((obj!=null) && (obj instanceof PnlBusy)) {
			busyPanel = (PnlBusy) obj;
		}
	}
	
	public void show() {
		if (busyPanel!=null) {
			busyPanel.start();
			((GuiWindow) getGuiObject()).getWindow().setVisible(true);
		}
	}

	public void hide() {
		((GuiWindow) getGuiObject()).getWindow().setVisible(false);
		busyPanel.stop();
	}
}
