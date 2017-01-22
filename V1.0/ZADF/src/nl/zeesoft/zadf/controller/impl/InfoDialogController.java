package nl.zeesoft.zadf.controller.impl;

import java.awt.event.ActionEvent;

import nl.zeesoft.zadf.controller.GuiWindowController;
import nl.zeesoft.zadf.gui.GuiDialog;
import nl.zeesoft.zadf.gui.property.PrpTextAreaString;
import nl.zeesoft.zodb.event.EvtEvent;

public class InfoDialogController extends GuiWindowController {
	public InfoDialogController(GuiDialog infoDialog) {
		super(infoDialog);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String action = "";

		if (e.getActionCommand()!=null) {
			action = e.getActionCommand();
		}

		if (action.equals(ACTION_CLOSE_FRAME)) {
			super.actionPerformed(e);
		} else if (action.equals(ZADFFactory.BUTTON_INFO_OK)) {
			publishEvent(new EvtEvent(ACTION_CLOSE_FRAME, this, ACTION_CLOSE_FRAME));
		}
	}

	public void setInfo(String errors) {
		PrpTextAreaString prpErr = (PrpTextAreaString) getGuiObjectByName(ZADFFactory.PROPERTY_INFO_TEXT);
		prpErr.getValueObject().setValue(errors);
		prpErr.updateComponentValue();
	}
}
