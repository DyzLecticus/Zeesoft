package nl.zeesoft.zmmt.gui;

import java.awt.Color;
import java.awt.Window;

import javax.swing.JFrame;

import nl.zeesoft.zdk.image.ImageIcon;

public abstract class FrameObject {
	private	Controller	controller	= null;
	private JFrame		frame		= new JFrame();
	
	public FrameObject(Controller controller) {
		this.controller = controller;
		frame.setIconImage(new ImageIcon("z",32,Color.WHITE).getBufferedImage());
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
