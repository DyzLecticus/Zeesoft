package nl.zeesoft.zdk.test.app;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.app.App;
import nl.zeesoft.zdk.app.AppConfig;

public class TestAppLive {
	protected static TestAppLive	self		= new TestAppLive();
	protected static int			sleepMs		= 600000;
	
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);
		
		AppConfig config = new AppConfig();
		App app = new App(config);
		
		Logger.debug(self,"Starting app ...");
		assert app.start();
		Logger.debug(self,"Started app ...");
		assert app.isStarted();
		Util.sleep(sleepMs);
		Logger.debug(self,"Stopping app ...");
		assert app.stop();
		Logger.debug(self,"Stopped app ...");
	}
}
