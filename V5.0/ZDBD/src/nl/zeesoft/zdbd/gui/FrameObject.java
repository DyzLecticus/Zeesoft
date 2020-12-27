package nl.zeesoft.zdbd.gui;

import java.awt.Window;

import javax.swing.JFrame;
import javax.swing.UIManager;

import nl.zeesoft.zdbd.EventListener;
import nl.zeesoft.zdbd.ThemeController;

public abstract class FrameObject implements EventListener {
	protected ThemeController		controller	= null;
	protected JFrame				frame		= null;
	
	public FrameObject(ThemeController controller) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			JFrame.setDefaultLookAndFeelDecorated(true);
		} catch (Exception e) {
			// Ignore
		}
		this.controller = controller;
		controller.eventPublisher.addListener(this);
		frame = new JFrame();
	}
	
	public ThemeController getController() {
		return controller;
	}
	
	public JFrame getFrame() {
		return frame;
	}

	public abstract void initialize();
	
	public static void positionFrameOverFrame(Window top,Window bottom) {
		int x = (bottom.getX() + (bottom.getWidth() / 2)) - (top.getWidth() / 2);
		int y = (bottom.getY() + (bottom.getHeight() / 2)) - (top.getHeight() / 2);
		top.setLocation(x, y);
	}

}
