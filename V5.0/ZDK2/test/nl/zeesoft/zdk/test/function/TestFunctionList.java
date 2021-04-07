package nl.zeesoft.zdk.test.function;

import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.function.Function;
import nl.zeesoft.zdk.function.FunctionList;

public class TestFunctionList {
	private static TestFunctionList self = new TestFunctionList();
	
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);

		// Test default exception handling
		FunctionList list = new FunctionList();
		list.exceptionHandler = null;
		list.functions.add(new Function());
		@SuppressWarnings("unchecked")
		List<Object> returnValues1 = (List<Object>) list.execute(self);
		assert returnValues1!=null;
		assert returnValues1.size()==0;
		list.functions.clear();
		
		// Test custom exception handler
		list.exceptionHandler = TestFunction.MOCK_EXCEPTION_HANDLER;
		MockFunction function = new MockFunction();
		function.param1 = "2";
		list.functions.add(function);
		function = new MockFunction();
		function.param1 = "Pizza";
		list.addFunction(function);
		list.addFunction(null);
		@SuppressWarnings("unchecked")
		List<Object> returnValues2 = (List<Object>) list.execute(self);
		assert returnValues2!=null;
		assert returnValues2.size()==1;
		assert returnValues2.get(0)!=null;
		assert returnValues2.get(0) instanceof Integer;
		assert (int)returnValues2.get(0)==2;
	}
}
