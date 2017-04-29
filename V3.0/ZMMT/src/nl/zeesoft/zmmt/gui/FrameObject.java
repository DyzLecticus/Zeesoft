package nl.zeesoft.zmmt.gui;

import java.awt.Color;

import javax.swing.JFrame;

import nl.zeesoft.zmmt.gui.image.ImageIconLabel;

public abstract class FrameObject {
	private ControllerWindowAdapter adapter		= null;
	private ControllerKeyListener	keyListener = null;
	private JFrame					frame		= new JFrame();
	
	public FrameObject(ControllerWindowAdapter adapter,ControllerKeyListener keyListener) {
		this.adapter = adapter;
		this.keyListener = keyListener;
		frame.setIconImage(new ImageIconLabel("z",32,Color.WHITE).getBufferedImage());
		frame.addKeyListener(keyListener);
	}

	public abstract void initialize(Controller controller);
	
	public JFrame getFrame() {
		return frame;
	}

	public ControllerWindowAdapter getAdapter() {
		return adapter;
	}

	public ControllerKeyListener getKeyListener() {
		return keyListener;
	}
}
