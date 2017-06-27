package nl.zeesoft.zeetracker.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class ControllerWindowAdapter extends WindowAdapter implements WindowFocusListener {
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

	@Override
	public void windowGainedFocus(WindowEvent e) {
		controller.windowStateChanged(e);
	}

	@Override
	public void windowLostFocus(WindowEvent e) {
		controller.windowStateChanged(e);
	}
}
