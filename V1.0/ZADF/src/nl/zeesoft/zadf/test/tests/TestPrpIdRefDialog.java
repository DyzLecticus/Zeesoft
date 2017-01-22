package nl.zeesoft.zadf.test.tests;

import nl.zeesoft.zadf.controller.impl.PrpIdRefDialogController;
import nl.zeesoft.zadf.controller.impl.ZADFFactory;
import nl.zeesoft.zadf.gui.GuiConfig;
import nl.zeesoft.zadf.gui.GuiDialog;
import nl.zeesoft.zadf.test.model.TestConfig;
import nl.zeesoft.zodb.database.DbConfig;

public class TestPrpIdRefDialog {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DbConfig.getInstance().setDebug(true);
		DbConfig.getInstance().setInstallDir(new TestConfig().getTestDir());

		ZADFFactory factory = GuiConfig.getInstance().getGuiFactory();
		PrpIdRefDialogController pdc = factory.getNewPrpIdRefDialogController(ZADFFactory.DIALOG_ID_REF);
		GuiDialog dialog = (GuiDialog) pdc.getGuiObjectByName(ZADFFactory.DIALOG_ID_REF);
		dialog.getDialog().setVisible(true);
	}

}
