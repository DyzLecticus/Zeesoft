package nl.zeesoft.zdk.function;

import java.util.ArrayList;
import java.util.List;

public class FunctionList extends Function {
	public List<Function>	functions	= new ArrayList<Function>();
	
	@Override
	protected Object exec() {
		List<Object> returnValues = new ArrayList<Object>();
		for (Function function: functions) {
			if (exceptionHandler!=null) {
				function.exceptionHandler = exceptionHandler;
			}
			returnValues.add(function.execute(caller));
		}
		return returnValues;
	}
}
