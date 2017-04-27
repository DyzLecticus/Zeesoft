package nl.zeesoft.zmmt.gui;

import java.awt.Color;

import javax.swing.JFrame;

import nl.zeesoft.zmmt.gui.image.ImageIconLabel;

public abstract class FrameObject {
	private ControllerWindowAdapter adapter = null;
	private JFrame					frame	= new JFrame();
	
	public FrameObject(ControllerWindowAdapter adapter) {
		this.adapter = adapter;
		frame.setIconImage(new ImageIconLabel("z",32,Color.WHITE).getBufferedImage());
	}

	public abstract void initialize();
	
	public JFrame getFrame() {
		return frame;
	}

	public ControllerWindowAdapter getAdapter() {
		return adapter;
	}
}
