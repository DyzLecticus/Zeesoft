package nl.zeesoft.zmmt;

import nl.zeesoft.zmmt.gui.Controller;
import nl.zeesoft.zmmt.gui.Settings;

public class ZeeTracker {
	public static void main(String[] args) {
		Settings settings = new Settings();
		Controller controller = new Controller(settings);
		controller.initialize();
		controller.start(true);
	}
}
