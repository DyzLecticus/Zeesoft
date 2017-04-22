package nl.zeesoft.zjmo.orchestra.controller;

import javax.swing.JFrame;

public class FrameObject {
	private JFrame	frame	= null;

	protected JFrame getFrame() {
		return frame;
	}

	protected void setFrame(JFrame frame) {
		this.frame = frame;
	}
}
