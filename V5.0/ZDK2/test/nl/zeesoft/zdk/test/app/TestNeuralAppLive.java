package nl.zeesoft.zdk.test.app;

import nl.zeesoft.zdk.Console;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.app.neural.NeuralApp;
import nl.zeesoft.zdk.app.neural.NeuralAppConfig;

public class TestNeuralAppLive {
	protected static TestNeuralAppLive	self		= new TestNeuralAppLive();
	protected static int				sleepMs		= 60 * 60 * 1000;
	
	public static void main(String[] args) {
		Logger.setLoggerDebug(false);
		
		NeuralAppConfig config = new NeuralAppConfig();
		NeuralApp app = new NeuralApp(config);
		
		Console.log("Starting app ...");
		if (app.start()) {
			Console.log("Started app");
			Util.sleep(sleepMs);
			Console.log("Stopping app ...");
			assert app.stop();
			Console.log("Stopped app");
		} else {
			Console.err("Failed to start app");
		}
	}
}
