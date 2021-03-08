package nl.zeesoft.zdk.test;

import nl.zeesoft.zdk.Console;
import nl.zeesoft.zdk.Logger;

public class TestLogger {
	private static TestLogger	self	= new TestLogger();
	
	public static void main(String[] args) {
		Console console = new Console();
		console.logObject("Test debug message");
		Console.err("Test error message");
		console.errObject("Test error message with exception",new Exception("Test exception"));

		Logger.setLoggerDebug(false);
		assert Logger.isLoggerDebug() == false;
		Logger.debug(self,"Test debug message will not be logged");
		Logger.setLoggerDebug(true);
		assert Logger.isLoggerDebug();
		Logger.debug(self,"Test debug message");
		Logger.error(self,"Test error message");
		Logger.error(self,"Test error message with exception",new Exception("Test exception"));
	}
}
