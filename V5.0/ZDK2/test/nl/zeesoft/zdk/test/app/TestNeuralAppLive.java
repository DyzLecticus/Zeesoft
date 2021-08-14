package nl.zeesoft.zdk.test.app;

import nl.zeesoft.zdk.Console;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.app.AppActionHandler;
import nl.zeesoft.zdk.app.neural.NeuralApp;
import nl.zeesoft.zdk.app.neural.NeuralAppConfig;

public class TestNeuralAppLive {
	protected static TestNeuralAppLive	self		= new TestNeuralAppLive();
	
	public static void main(String[] args) {
		Logger.setLoggerDebug(false);
		
		NeuralAppConfig config = new NeuralAppConfig() {
			@Override
			public AppActionHandler getNewAppActionHandler() {
				return new AppActionHandlerImpl();
			}
		};
		NeuralApp app = new NeuralApp(config);
		
		Console.log("Starting app ...");
		if (app.start()) {
			Console.log("Started app");
		} else {
			Console.err("Failed to start app");
		}
	}
}
