package nl.zeesoft.zadf.test.tests;

import nl.zeesoft.zadf.controller.impl.ControlFrameController;
import nl.zeesoft.zadf.controller.impl.ZADFFactory;
import nl.zeesoft.zadf.gui.GuiConfig;
import nl.zeesoft.zadf.gui.GuiFrame;
import nl.zeesoft.zadf.test.model.TestConfig;
import nl.zeesoft.zodb.database.DbConfig;

public class TestControlFrame {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DbConfig.getInstance().setDebug(true);
		DbConfig.getInstance().setInstallDir(new TestConfig().getTestDir());

		ZADFFactory factory = GuiConfig.getInstance().getGuiFactory();
		ControlFrameController cfc = factory.getNewControlFrameController();
		GuiFrame frame = (GuiFrame) cfc.getGuiObjectByName(ZADFFactory.FRAME_CONTROL);
		frame.getFrame().setVisible(true);
	}

}
