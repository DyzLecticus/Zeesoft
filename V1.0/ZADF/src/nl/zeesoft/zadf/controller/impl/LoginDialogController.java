package nl.zeesoft.zadf.controller.impl;

import java.awt.event.ActionEvent;

import nl.zeesoft.zadf.controller.GuiWindowController;
import nl.zeesoft.zadf.gui.GuiDialog;
import nl.zeesoft.zodb.event.EvtEvent;

public class LoginDialogController extends GuiWindowController {
	public static final String LOGIN_BUTTON_CLICKED = "LOGIN_BUTTON_CLICKED"; 
	
	public LoginDialogController(GuiDialog loginDialog) {
		super(loginDialog);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String action = "";
		if (e.getActionCommand()!=null) {
			action = e.getActionCommand();
		}

		if (action.equals(ACTION_CLOSE_FRAME)) {
			super.actionPerformed(e);
		} else if (action.equals(ZADFFactory.BUTTON_LOGIN)) {
			publishEvent(new EvtEvent(LOGIN_BUTTON_CLICKED,this,LOGIN_BUTTON_CLICKED));
		}
	}

}
