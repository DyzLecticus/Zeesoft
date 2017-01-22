package nl.zeesoft.zadf.test.tests;

import nl.zeesoft.zadf.controller.impl.MainFrameController;
import nl.zeesoft.zadf.controller.impl.ZADFFactory;
import nl.zeesoft.zadf.gui.GuiConfig;
import nl.zeesoft.zadf.gui.GuiFrame;
import nl.zeesoft.zadf.test.model.TestConfig;
import nl.zeesoft.zodb.database.DbConfig;

public class TestMainFrame {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DbConfig.getInstance().setDebug(true);
		DbConfig.getInstance().setInstallDir(new TestConfig().getTestDir());

		ZADFFactory factory = GuiConfig.getInstance().getGuiFactory();
		MainFrameController mfc = factory.getNewMainFrameController();
		GuiFrame frame = (GuiFrame) mfc.getGuiObjectByName(ZADFFactory.FRAME_MAIN);
		frame.getFrame().setVisible(true);
	}

}
