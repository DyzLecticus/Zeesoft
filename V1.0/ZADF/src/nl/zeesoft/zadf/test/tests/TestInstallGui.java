package nl.zeesoft.zadf.test.tests;

import java.io.File;

import nl.zeesoft.zadf.ZADFController;
import nl.zeesoft.zadf.gui.GuiConfig;
import nl.zeesoft.zadf.test.model.TestConfig;
import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.file.FileObject;

public class TestInstallGui {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ZADFController ctrlr = new ZADFController(args);
		
		TestConfig tc = new TestConfig();

		DbConfig.getInstance().setDebug(true);
		DbConfig.getInstance().setInstallDir(tc.getTestDir());
		DbConfig.getInstance().setEncryptionKey(Generic.generateNewKey(128));

		ctrlr.installGui();

		File iconDir = new File(GuiConfig.getInstance().getFullIconDir());
		iconDir.mkdir();
		
		String resourceIconDirName = Generic.dirName(tc.getResourceDir()) + GuiConfig.getInstance().getIconDir();
		File resourceIconDir = new File(resourceIconDirName);
		if (resourceIconDir.exists()) {
			for (File icon: resourceIconDir.listFiles()) {
				if (icon.isFile()) {
					Exception e = FileObject.copyFile(icon.getAbsolutePath(), Generic.dirName(iconDir.getAbsolutePath()) + icon.getName());
					if (e!=null) {
						Messenger.getInstance().error(resourceIconDir, "Unable to copy icons to test dir: " + e.getMessage());
						break;
					}
				}
			}
		} else {
			Messenger.getInstance().error(resourceIconDir, "Resource icon directory not found: " + resourceIconDirName);
		}
	}

}
