package nl.zeesoft.zodb.test.impl;

import java.awt.Color;

import nl.zeesoft.zodb.file.image.ImgIconLabel;
import nl.zeesoft.zodb.test.TestConfig;

public class TestImgIconLabel {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestConfig.getInstance();
		
		ImgIconLabel iconLabel = new ImgIconLabel("z",32,Color.WHITE);
		
		iconLabel.writePngFile(TestConfig.TEST_DIR + "test.png");
		
		iconLabel.setVisible(true);
	}

}
