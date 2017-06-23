package nl.zeesoft.zmmt.gui;

import java.awt.Window;

import javax.swing.JFrame;

import nl.zeesoft.zdk.ZDKFactory;

public abstract class FrameObject {
	private	Controller	controller	= null;
	private JFrame		frame		= new JFrame();
	
	public FrameObject(Controller controller) {
		this.controller = controller;
		ZDKFactory factory = new ZDKFactory();
		frame.setIconImage(factory.getZeesoftIcon32().getBufferedImage());
		frame.addKeyListener(controller.getPlayerKeyListener());
	}

	public abstract void initialize();
	
	public JFrame getFrame() {
		return frame;
	}

	protected Controller getController() {
		return controller;
	}

	public static void positionFrameOverFrame(Window top,Window bottom) {
		int x = (bottom.getX() + (bottom.getWidth() / 2)) - (top.getWidth() / 2);
		int y = (bottom.getY() + (bottom.getHeight() / 2)) - (top.getHeight() / 2);
		top.setLocation(x, y);
	}
}
