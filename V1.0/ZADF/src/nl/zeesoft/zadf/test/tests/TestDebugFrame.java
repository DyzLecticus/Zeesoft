package nl.zeesoft.zadf.test.tests;

import nl.zeesoft.zadf.controller.impl.DebugFrameController;
import nl.zeesoft.zadf.controller.impl.ZADFFactory;
import nl.zeesoft.zadf.gui.GuiConfig;
import nl.zeesoft.zadf.gui.GuiFrame;
import nl.zeesoft.zadf.test.model.TestConfig;
import nl.zeesoft.zodb.database.DbConfig;

public class TestDebugFrame {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DbConfig.getInstance().setDebug(true);
		DbConfig.getInstance().setInstallDir(new TestConfig().getTestDir());

		ZADFFactory factory = GuiConfig.getInstance().getGuiFactory();
		DebugFrameController dfc = factory.getNewDebugFrameController();
		GuiFrame frame = (GuiFrame) dfc.getGuiObjectByName(ZADFFactory.FRAME_DEBUG);
		frame.getFrame().setVisible(true);
	}

}
