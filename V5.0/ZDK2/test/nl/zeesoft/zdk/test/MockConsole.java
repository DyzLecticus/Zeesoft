package nl.zeesoft.zdk.test;

import nl.zeesoft.zdk.Console;

public class MockConsole extends Console {
	public void logObject(Object obj) {
		// Ignore
	}
	public void errObject(Object obj, Exception ex) {
		// Ignore
	}
}
