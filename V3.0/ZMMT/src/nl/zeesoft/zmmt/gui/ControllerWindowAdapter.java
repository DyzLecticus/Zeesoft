package nl.zeesoft.zmmt.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ControllerWindowAdapter extends WindowAdapter {
	private Controller 	controller = null;
	
	public ControllerWindowAdapter(Controller controller) {
		this.controller = controller;
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		controller.windowClosing(e);
	}
	
	@Override
	public void windowIconified(WindowEvent e) {
		controller.windowIconified(e);
	}
	
	@Override
	public void windowStateChanged(WindowEvent e) {
		controller.windowStateChanged(e);
	}
}
