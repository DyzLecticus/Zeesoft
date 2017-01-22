package nl.zeesoft.zadf.controller;

import nl.zeesoft.zadf.gui.GuiObject;
import nl.zeesoft.zadf.gui.GuiPanel;
import nl.zeesoft.zadf.gui.GuiProperty;
import nl.zeesoft.zadf.gui.panel.PnlDetail;

public abstract class GuiPropertyPanelController extends GuiObjectController {
	private boolean	enabled = true;

	public GuiPropertyPanelController(GuiObject object) {
		super(object);
	}

	protected void setEnabled() {
		PnlDetail panel = (PnlDetail) getGuiObject();
		setEnabled(panel,enabled);
	}

	private void setEnabled(GuiPanel panel, boolean enabled) {
		for (GuiObject guiObj: panel.getPanelObjects().getObjects()) {
			if (guiObj instanceof GuiProperty) {
				((GuiProperty) guiObj).setEnabled(enabled);
			} else if (guiObj instanceof GuiPanel) {
				setEnabled(panel, enabled);
			}
		}
	}
	
	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		if (this.enabled!=enabled) {
			this.enabled = enabled;
			setEnabled();
		}
	}
	
}
