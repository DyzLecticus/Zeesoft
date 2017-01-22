package nl.zeesoft.zodb.test.impl;

import nl.zeesoft.zodb.database.gui.GuiMainFrame;
import nl.zeesoft.zodb.test.TestConfig;

public class TestGuiMainFrame {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestConfig.getInstance();
		GuiMainFrame mainFrame = new GuiMainFrame();
		mainFrame.setVisible(true);
	}

}
