package nl.zeesoft.zadf.test.tests;

import nl.zeesoft.zadf.controller.impl.BusyWindowController;
import nl.zeesoft.zadf.controller.impl.ZADFFactory;
import nl.zeesoft.zadf.gui.GuiConfig;
import nl.zeesoft.zadf.test.model.TestConfig;
import nl.zeesoft.zodb.database.DbConfig;

public class TestBusyWindow {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DbConfig.getInstance().setDebug(true);
		DbConfig.getInstance().setInstallDir(new TestConfig().getTestDir());

		ZADFFactory factory = GuiConfig.getInstance().getGuiFactory();
		BusyWindowController bwc = factory.getNewBusyWindowController();
		bwc.show();
		
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		bwc.hide();
	}

}
