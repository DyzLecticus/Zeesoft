package nl.zeesoft.zadf.test.tests;

import nl.zeesoft.zadf.controller.impl.DetailDialogController;
import nl.zeesoft.zadf.controller.impl.ZADFFactory;
import nl.zeesoft.zadf.gui.GuiConfig;
import nl.zeesoft.zadf.gui.GuiDialog;
import nl.zeesoft.zadf.test.model.TestConfig;
import nl.zeesoft.zodb.database.DbConfig;

public class TestDetailDialog {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DbConfig.getInstance().setDebug(true);
		DbConfig.getInstance().setInstallDir(new TestConfig().getTestDir());

		ZADFFactory factory = GuiConfig.getInstance().getGuiFactory();
		DetailDialogController ddc = factory.getNewDetailDialogController();
		GuiDialog dialog = (GuiDialog) ddc.getGuiObjectByName(ZADFFactory.DIALOG_DETAIL);
		dialog.getDialog().setVisible(true);
	}

}
