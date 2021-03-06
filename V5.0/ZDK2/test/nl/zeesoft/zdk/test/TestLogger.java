package nl.zeesoft.zdk.test;

import nl.zeesoft.zdk.Logger;

public class TestLogger {
	private static TestLogger	self	= new TestLogger();
	
	public static void main(String[] args) {
		assert Logger.isLoggerDebug() == false;
		Logger.debug(self,"Test debug message will not be logged");
		Logger.setLoggerDebug(true);
		assert Logger.isLoggerDebug();
		Logger.debug(self,"Test debug message");
		Logger.error(self,"Test error message");
		Logger.error(self,"Test error message with exception",new Exception("Test exception"));
	}
}
