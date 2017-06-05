package nl.zeesoft.zmmt.gui;

import java.awt.Color;

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
}
