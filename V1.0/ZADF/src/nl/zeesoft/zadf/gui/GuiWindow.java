package nl.zeesoft.zadf.gui;

import javax.swing.JWindow;

public class GuiWindow extends GuiWindowObject {
	public GuiWindow(String name) {
		super(name,new JWindow());
	}
	
	/**
	 * @return the window
	 */
	public JWindow getWindow() {
		return (JWindow) super.getWindow();
	}
}
