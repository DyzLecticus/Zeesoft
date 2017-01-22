package nl.zeesoft.zodb.test.impl;

import nl.zeesoft.zodb.database.gui.GuiProgressFrame;
import nl.zeesoft.zodb.test.TestConfig;

public class TestGuiProgressFrame {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestConfig.getInstance();
		
		GuiProgressFrame progressFrame = new GuiProgressFrame(300,20);
		progressFrame.setTitle("Doing stuff");
		
		progressFrame.setTodo(1000);
		
		progressFrame.setVisible(true);
		
		progressFrame.setDone(100);
		
		TestConfig.sleep(1000);

		progressFrame.setDone(200);

		TestConfig.sleep(1000);

		progressFrame.setDone(500);

		TestConfig.sleep(1000);

		progressFrame.setDone(1000);
	}

}
