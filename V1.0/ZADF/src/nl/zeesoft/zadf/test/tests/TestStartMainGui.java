package nl.zeesoft.zadf.test.tests;

import nl.zeesoft.zadf.controller.GuiController;
import nl.zeesoft.zadf.test.model.TestConfig;
import nl.zeesoft.zodb.database.DbConfig;

public class TestStartMainGui {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DbConfig.getInstance().setDebug(true);
		DbConfig.getInstance().setInstallDir(new TestConfig().getTestDir());
		
		GuiController.getInstance().startMainGui();
	}

}
