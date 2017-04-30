package nl.zeesoft.zmmt.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ControllerKeyListener implements KeyListener {
	private Controller 	controller	= null;

	public ControllerKeyListener(Controller controller) {
		this.controller = controller;
	}
	
	@Override
	public void keyPressed(KeyEvent evt) {
		if (evt.getKeyCode()==KeyEvent.VK_F1) {
			controller.switchTo(FrameMain.COMPOSITION);
		} else if (evt.getKeyCode()==KeyEvent.VK_F2) {
			controller.switchTo(FrameMain.INSTRUMENTS);
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// Ignore
	}

	@Override
	public void keyTyped(KeyEvent evt) {
		// Ignore
	}
	
	protected Controller getController() {
		return controller;
	}
}
