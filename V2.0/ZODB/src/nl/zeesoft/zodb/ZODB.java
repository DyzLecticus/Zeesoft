package nl.zeesoft.zodb;

import java.awt.Color;

import nl.zeesoft.zodb.database.DbController;
import nl.zeesoft.zodb.file.image.ImgIconLabel;

/**
 * The Zeesoft Object Database is an XML file based object storage database.
 * 
 * This class is the entry point to the application.
 * It uses the database controller to install, start and stop the database.
 */
public class ZODB {
	public static void main(String[] args) {
		DbController.getInstance().handleMainMethodsArguments(args);
	}
	public static ImgIconLabel getIconImage() {
		return getIconImage(Color.WHITE);
	}
	public static ImgIconLabel getIconImage(Color background) {
		return new ImgIconLabel("z",32,background);
	}
}