package nl.zeesoft.zdk.function;

import java.util.ArrayList;
import java.util.List;

public class FunctionList extends Function {
	public List<Function>	functions	= new ArrayList<Function>();
	
	@Override
	protected Object exec() {
		List<Object> returnValues = new ArrayList<Object>();
		for (Function function: functions) {
			Object returnValue = executeFunction(function);
			if (returnValue!=null) {
				returnValues.add(returnValue);
			}
		}
		return returnValues;
	}
	
	public void addFunction(Function function) {
		if (function!=null) {
			functions.add(function);
		}
	}
	
	protected Object executeFunction(Function function) {
		if (exceptionHandler!=null) {
			function.exceptionHandler = exceptionHandler;
		}
		return function.execute(caller);
	}
}
