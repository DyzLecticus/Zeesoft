package nl.zeesoft.zjmo.orchestra.controller;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ControllerWindowAdapter extends WindowAdapter{
	private OrchestraController controller = null;
	
	public ControllerWindowAdapter(OrchestraController controller) {
		this.controller = controller;
	}
	
	public void windowClosing(WindowEvent e) {
		controller.windowClosing(e);
	}
	
	public void windowIconified(WindowEvent e) {
		controller.windowIconified(e);
	}
	
	public void windowStateChanged(WindowEvent e) {
		// Ignore
	}
}
