package nl.zeesoft.zdk.test.app;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.app.neural.NeuralApp;
import nl.zeesoft.zdk.app.neural.NeuralAppConfig;

public class TestNeuralAppLive {
	protected static TestNeuralAppLive	self		= new TestNeuralAppLive();
	protected static int				sleepMs		= 600000;
	
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);
		
		NeuralAppConfig config = new NeuralAppConfig();
		NeuralApp app = new NeuralApp(config);
		
		Logger.debug(self,"Starting app ...");
		if (app.start()) {
			Logger.debug(self,"Started app");
			Util.sleep(sleepMs);
			Logger.debug(self,"Stopping app ...");
			assert app.stop();
			Logger.debug(self,"Stopped app");
		} else {
			Logger.debug(self,"Failed to start app");
		}
	}
}
