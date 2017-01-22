package nl.zeesoft.zadf.test.tests;

import nl.zeesoft.zadf.controller.impl.InfoDialogController;
import nl.zeesoft.zadf.controller.impl.ZADFFactory;
import nl.zeesoft.zadf.gui.GuiConfig;
import nl.zeesoft.zadf.gui.GuiDialog;
import nl.zeesoft.zadf.test.model.TestConfig;
import nl.zeesoft.zodb.database.DbConfig;

public class TestInfoDialog {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DbConfig.getInstance().setDebug(true);
		DbConfig.getInstance().setInstallDir(new TestConfig().getTestDir());

		ZADFFactory factory = GuiConfig.getInstance().getGuiFactory();
		InfoDialogController idc = factory.getNewInfoDialogController();
		GuiDialog dialog = (GuiDialog) idc.getGuiObjectByName(ZADFFactory.DIALOG_INFO);
		dialog.getDialog().setVisible(true);
	}

}
