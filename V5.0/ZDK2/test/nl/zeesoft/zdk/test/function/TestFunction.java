package nl.zeesoft.zdk.test.function;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.function.FunctionExceptionHandler;
import nl.zeesoft.zdk.function.Function;

public class TestFunction {
	public static String MOCK_RETURN_VALUE = "mockReturnValue";

	public static FunctionExceptionHandler MOCK_EXCEPTION_HANDLER = new FunctionExceptionHandler() {
		@Override
		protected void handleException(Object caller, Exception ex) {
			Logger.debug(caller,"Custom exception handler caught exception: " + ex);
		}
	};
	
	public static Function MOCK_FUNCTION = new Function() {
		@Override
		protected Object exec() {
			int value = Integer.parseInt("" + param1);
			Logger.debug(caller,"Integer.parseInt(\"" + param1 + "\") = " + value);
			return MOCK_RETURN_VALUE;
		}
	};
	
	private static TestFunction self = new TestFunction();
	
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);

		// Test default function
		Function function = new Function();
		Object returnValue = function.execute(self);
		assert returnValue == null;
		
		// Test happy flow
		function = new MockFunction();
		function.param1 = "1";
		returnValue = function.execute(self);
		assert returnValue != null;
		assert returnValue instanceof Integer;
		assert (int)returnValue == 1;
		
		// Test default exception handling
		function.param1 = "DefaultHandling";
		returnValue = function.execute(self);
		assert returnValue == null;
		
		// Test default exception handler
		function.exceptionHandler = new FunctionExceptionHandler();
		function.param1 = "DefaultHandler";
		returnValue = function.execute(self);
		assert returnValue == null;

		// Test custom exception handler
		function.exceptionHandler = MOCK_EXCEPTION_HANDLER;
		function.param1 = "CustomHandler";
		returnValue = function.execute(self);
		assert returnValue == null;
	}
}
