package nl.zeesoft.zdk.function;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

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
	
	public void addFunctionList(FunctionList fl) {
		if (fl!=null && fl.functions.size()>0) {
			functionLists.add(fl);
		}
	}
	
	public void addFunction(Function function) {
		if (function!=null) {
			FunctionList fl = new FunctionList();
			fl.functions.add(function);
			addFunctionList(fl);
		}
	}
	
	public SortedMap<Integer,List<Function>> getStepFunctions() {
		SortedMap<Integer,List<Function>> r = new TreeMap<Integer,List<Function>>();
		int step = 0;
		for (FunctionList fl: functionLists) {
			if (fl.functions.size()>0) {
				r.put(step, new ArrayList<Function>(fl.functions));
				step++;
			}
		}
		return r;
	}
}
