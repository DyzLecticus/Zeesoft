package nl.zeesoft.zodb.test.impl;

import nl.zeesoft.zodb.database.gui.GuiDebugFrame;
import nl.zeesoft.zodb.test.TestConfig;

public class TestGuiDebugFrame {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestConfig.getInstance();
		
		GuiDebugFrame debugFrame = new GuiDebugFrame();
		debugFrame.setVisible(true);
	}

}
