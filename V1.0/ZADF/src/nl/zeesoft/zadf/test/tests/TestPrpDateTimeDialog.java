package nl.zeesoft.zadf.test.tests;

import nl.zeesoft.zadf.controller.impl.PrpDateTimeDialogController;
import nl.zeesoft.zadf.controller.impl.ZADFFactory;
import nl.zeesoft.zadf.gui.GuiConfig;
import nl.zeesoft.zadf.gui.GuiDialog;
import nl.zeesoft.zadf.test.model.TestConfig;
import nl.zeesoft.zodb.database.DbConfig;

public class TestPrpDateTimeDialog {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DbConfig.getInstance().setDebug(true);
		DbConfig.getInstance().setInstallDir(new TestConfig().getTestDir());

		ZADFFactory factory = GuiConfig.getInstance().getGuiFactory();
		PrpDateTimeDialogController pdc = factory.getNewPrpDateTimeDialogController();
		GuiDialog dialog = (GuiDialog) pdc.getGuiObjectByName(ZADFFactory.DIALOG_DATETIME);
		dialog.getDialog().setVisible(true);
	}

}
