package nl.zeesoft.zadf.test.model;

import java.io.File;

import nl.zeesoft.zadf.model.ZADFModel;
import nl.zeesoft.zodb.Generic;

public class TestConfig extends ZADFModel {
	private final static String	TEST_DIR 		= "C:/Zeesoft/GitHub/Zeesoft/ZADF/testdir";
	private final static String	RESOURCE_DIR	= "C:/Zeesoft/GitHub/Zeesoft/ZADF/resources";
	
	private File 				testDir			= null;
	private File 				resourceDir		= null;
	
	public String getTestDir() {
		if (testDir==null) {
			testDir = new File(TEST_DIR);
			if (!testDir.exists()) {
				testDir.mkdir();
			}
		}
		return Generic.dirName(testDir.getAbsolutePath());
	}

	public String getResourceDir() {
		if (resourceDir==null) {
			resourceDir = new File(RESOURCE_DIR);
			if (!resourceDir.exists()) {
				resourceDir.mkdir();
			}
		}
		return Generic.dirName(resourceDir.getAbsolutePath());
	}

}
