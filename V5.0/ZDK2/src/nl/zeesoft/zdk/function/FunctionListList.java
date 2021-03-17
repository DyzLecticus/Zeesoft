package nl.zeesoft.zdk.function;

import java.util.ArrayList;
import java.util.List;

public class FunctionListList extends Function {
	public List<FunctionList>	functionLists	= new ArrayList<FunctionList>();
	
	@Override
	protected Object exec() {
		List<Object> returnValues = new ArrayList<Object>();
		for (FunctionList fl: functionLists) {
			if (exceptionHandler!=null) {
				fl.exceptionHandler = exceptionHandler;
			}
			@SuppressWarnings("unchecked")
			List<Object> listReturnValues = (List<Object>) fl.execute(caller);
			returnValues.addAll(listReturnValues);
		}
		return returnValues;
	}
	
	public void addFunction(Function function) {
		FunctionList fl = new FunctionList();
		fl.functions.add(function);
		functionLists.add(fl);
	}
}
