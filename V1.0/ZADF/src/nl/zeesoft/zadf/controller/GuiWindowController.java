package nl.zeesoft.zadf.controller;

import java.awt.Window;
import java.awt.event.ActionEvent;

import nl.zeesoft.zadf.gui.GuiObject;
import nl.zeesoft.zadf.gui.GuiWindowObject;
import nl.zeesoft.zodb.event.EvtEvent;

public abstract class GuiWindowController extends GuiObjectController {
	public static String	ACTION_CLOSE_FRAME 		= "ACTION_CLOSE_FRAME";
	public static String	ACTION_ICONIFY_FRAME 	= "ACTION_ICONIFY_FRAME";
	
	public GuiWindowController(GuiWindowObject windowObject) {
		super(windowObject);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String action = "";
		if (e.getActionCommand()!=null) {
			action = e.getActionCommand();
		}
		
		if (action.equals(ACTION_CLOSE_FRAME)) {
			publishEvent(new EvtEvent(ACTION_CLOSE_FRAME, this, ACTION_CLOSE_FRAME));
		}
	}
	
	public GuiObject getGuiObjectByName(String name) {
		GuiObject r = null;
		if (getGuiObject().getName().equals(name)) {
			r = getGuiObject();
		} else {
			GuiWindowObject window = (GuiWindowObject) getGuiObject();
			r = window.getGuiObjectByName(name);
		}
		return r;
	}
	
	public void positionOverWindowController(GuiWindowController wc) {
		if ((wc!=null) && (wc.getGuiObject()!=null)) {
			Window win = ((GuiWindowObject) wc.getGuiObject()).getWindow();
			Window myWin = ((GuiWindowObject) getGuiObject()).getWindow();
			int posX = 0;
			int posY = 0;
			posX = win.getX() + (win.getWidth() / 2) - (myWin.getWidth() / 2);
			posY = win.getY() + (win.getHeight() / 2) - (myWin.getHeight() / 2);
			if (posY < 0) {
				posY = 0;
			}
			myWin.setLocation(posX, posY);
		}
	}
	
}
