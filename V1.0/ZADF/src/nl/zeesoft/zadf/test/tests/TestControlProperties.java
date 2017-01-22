package nl.zeesoft.zadf.test.tests;

import nl.zeesoft.zadf.controller.impl.ControlConfigurationDialogController;
import nl.zeesoft.zadf.controller.impl.ZADFFactory;
import nl.zeesoft.zadf.gui.GuiConfig;
import nl.zeesoft.zadf.gui.GuiDialog;
import nl.zeesoft.zadf.test.model.TestConfig;
import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.database.DbConfig;

public class TestControlProperties {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DbConfig.getInstance().setDebug(true);
		DbConfig.getInstance().setInstallDir(new TestConfig().getTestDir());
		DbConfig.getInstance().setEncryptionKey(Generic.generateNewKey(128));

		ZADFFactory factory = GuiConfig.getInstance().getGuiFactory();
		ControlConfigurationDialogController cpfc = factory.getNewControlConfigurationDialogController();
		GuiDialog dialog = (GuiDialog) cpfc.getGuiObjectByName(ZADFFactory.DIALOG_CONTROL_CONFIGURATION);
		dialog.getDialog().setVisible(true);
	}

}
