package nl.zeesoft.zdk.test;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.TextIcon;

public class TestTextIcon {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);
		
		int size = 0;
		int assertSize = 16;
		for (int i = 0; i <= 5; i++) {
			TextIcon icon = new TextIcon();
			assert icon.getBufferedImage() == null;
			icon.size = size;
			icon.renderPanel();
			
			assert icon.getPanel().getWidth() == assertSize;
			assert icon.getPanel().getHeight() == assertSize;
			assert icon.getPanel().getPreferredSize().width == assertSize;
			assert icon.getPanel().getPreferredSize().height == assertSize;
			assert icon.getBufferedImage() != null;
			
			size = size + 16;
			if (size>16 && size<=64) {
				assertSize += 16;
			}
		}
		
		TextIcon icon = new TextIcon();
		icon.background = null;
		icon.renderPanel();
		assert icon.getPanel() != null;
		assert icon.getByteArray(true) == null;
		assert icon.getByteArray(false).length > 0;
	}
}
