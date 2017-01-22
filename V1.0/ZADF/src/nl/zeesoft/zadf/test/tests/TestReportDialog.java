package nl.zeesoft.zadf.test.tests;

import nl.zeesoft.zadf.controller.impl.ReportDialogController;
import nl.zeesoft.zadf.controller.impl.ZADFFactory;
import nl.zeesoft.zadf.gui.GuiConfig;
import nl.zeesoft.zadf.gui.GuiDialog;
import nl.zeesoft.zadf.test.model.TestConfig;
import nl.zeesoft.zodb.database.DbConfig;

public class TestReportDialog {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DbConfig.getInstance().setDebug(true);
		DbConfig.getInstance().setInstallDir(new TestConfig().getTestDir());

		ZADFFactory factory = GuiConfig.getInstance().getGuiFactory();
		ReportDialogController rdc = factory.getNewReportDialogController();
		GuiDialog dialog = (GuiDialog) rdc.getGuiObjectByName(ZADFFactory.DIALOG_REPORT);
		dialog.getDialog().setVisible(true);
	}

}
