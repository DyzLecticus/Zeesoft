package nl.zeesoft.zdk.function;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentMap;

public class ExecutorTask {
	protected Object								caller				= null;
	protected SortedMap<Integer,List<Function>>		stepFunctions		= null;
	
	protected int									todo				= 0;
	protected int									workingStep			= 0;
	protected int									workingFunctions	= 0;
	
	protected volatile long							start				= System.nanoTime();
	protected volatile boolean						done				= false;
	
	protected ConcurrentLinkedDeque<Object>			returnValues		= new ConcurrentLinkedDeque<Object>();
	protected ConcurrentMap<Integer,Long>			nsPerStep			= new ConcurrentHashMap<Integer,Long>();
	
	protected ExecutorTask() {
		
	}
	
	protected ExecutorTask(Object caller, SortedMap<Integer,List<Function>> stepFunctions) {
		this.caller = caller;
		this.stepFunctions = stepFunctions;
		for (List<Function> functions: stepFunctions.values()) {
			todo += functions.size();
		}
	}
	
	public List<Object> getReturnValues() {
		return new ArrayList<Object>(returnValues);
	}
	
	public SortedMap<Integer,Long> getNsPerStep() {
		return new TreeMap<Integer,Long>(nsPerStep);
	}
	
	protected List<ExecutorFunction> executedFunction(Object returnValue, Executor executor) {
		if (returnValue!=null) {
			returnValues.add(returnValue);
		}
		return executedFunction(executor);
	}
	
	protected synchronized List<ExecutorFunction> executedFunction(Executor executor) {
		todo--;
		List<ExecutorFunction> r = decrementWorkingFunctions(executor);
		if (todo==0) {
			done = true;
		}
		return r;
	}
	
	protected List<ExecutorFunction> decrementWorkingFunctions(Executor executor) {
		List<ExecutorFunction> r = null;
		workingFunctions--;
		if (workingFunctions==0) {
			nsPerStep.put(workingStep, System.nanoTime() - start);
			start = System.nanoTime();
			if (todo>0) {
				workingStep++;
				r = getWorkingStepFunctions(executor);
			}
		}
		return r;
	}
	
	protected List<ExecutorFunction> getWorkingStepFunctions(Executor executor) {
		List<ExecutorFunction> r = new ArrayList<ExecutorFunction>();
		List<Function> functions = stepFunctions.get(workingStep);
		for (Function function: functions) {
			r.add(new ExecutorFunction(caller, function, executor));
			workingFunctions++;
		}
		return r;
	}
}
