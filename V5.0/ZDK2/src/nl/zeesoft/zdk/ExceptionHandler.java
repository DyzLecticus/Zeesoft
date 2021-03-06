package nl.zeesoft.zdk;

import nl.zeesoft.zdk.Logger;

public class ExceptionHandler {
	protected void handleException(Object source, Exception ex) {
		handleExceptionDefault(source,ex);
	}
	
	public static void handleExceptionDefault(Object source, Exception ex) {
		Logger.error(source, "Caught exception", ex);
	}
}
