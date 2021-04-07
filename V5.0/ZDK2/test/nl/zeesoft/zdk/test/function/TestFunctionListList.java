package nl.zeesoft.zdk.test.function;

import java.util.List;
import java.util.SortedMap;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.function.Function;
import nl.zeesoft.zdk.function.FunctionList;
import nl.zeesoft.zdk.function.FunctionListList;

public class TestFunctionListList {
	private static TestFunctionListList self = new TestFunctionListList();
	
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);

		// Test default exception handling
		FunctionListList fll = new FunctionListList();
		fll.exceptionHandler = null;
		fll.addFunction(new Function());
		assert fll.functionLists.size() == 1;
		assert fll.functionLists.get(0).functions.size() == 1;
		assert fll.nsPerStep.size()==0;
		@SuppressWarnings("unchecked")
		List<Object> returnValues1 = (List<Object>) fll.execute(self);
		assert returnValues1!=null;
		assert returnValues1.size()==1;
		fll.functionLists.clear();
		assert fll.nsPerStep.size()==1;
		assert fll.nsPerStep.get(0)>1;
		
		// Test custom exception handler
		fll.exceptionHandler = TestFunction.MOCK_EXCEPTION_HANDLER;
		MockFunction function = new MockFunction();
		function.param1 = "2";
		fll.addFunction(function);
		function = new MockFunction();
		function.param1 = "Pizza";
		fll.addFunction(function);
		@SuppressWarnings("unchecked")
		List<Object> returnValues2 = (List<Object>) fll.execute(self);
		fll.addFunction(null);
		fll.addFunctionList(null);
		fll.addFunctionList(new FunctionList());
		assert returnValues2!=null;
		assert returnValues2.size()==2;
		assert returnValues2.get(0)!=null;
		assert returnValues2.get(0) instanceof Integer;
		assert (int)returnValues2.get(0)==2;
		assert returnValues2.get(1)==null;
		
		SortedMap<Integer,List<Function>> stepFunctions = fll.getStepFunctions();
		assert stepFunctions.size() == 2;
		assert stepFunctions.get(0).size() == 1;
		assert stepFunctions.get(1).size() == 1;
		
		fll.functionLists.get(1).functions.remove(0);
		stepFunctions = fll.getStepFunctions();
		assert stepFunctions.size() == 1;
		assert stepFunctions.get(0).size() == 1;
	}
}
