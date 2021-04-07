package nl.zeesoft.zdk.function;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicBoolean;

import nl.zeesoft.zdk.Lock;

public class ExecutorTask {
	protected Lock									lock				= new Lock(this);
	
	protected Object								caller				= null;
	protected SortedMap<Integer,List<Function>>		stepFunctions		= null;
	
	protected int									todo				= 0;
	protected int									workingStep			= 0;
	protected int									workingFunctions	= 0;
	protected AtomicBoolean							done				= new AtomicBoolean(false);

	protected long									start				= System.nanoTime();
	protected SortedMap<Integer,Long>				nsPerStep			= new TreeMap<Integer,Long>();
	
	protected ConcurrentLinkedDeque<Object>			returnValues		= new ConcurrentLinkedDeque<Object>();
	
	protected ExecutorTask(Object caller, SortedMap<Integer,List<Function>> stepFunctions) {
		this.caller = caller;
		this.stepFunctions = stepFunctions;
		for (List<Function> functions: stepFunctions.values()) {
			todo += functions.size();
		}
	}
	
	protected List<ExecutorFunction> executedFunction(Object returnValue, Executor executor) {
		List<ExecutorFunction> r = null;
		if (returnValue!=null) {
			returnValues.add(returnValue);
		}
		lock.lock();
		todo--;
		r = decrementWorkingFunctions(executor);
		if (todo==0) {
			done.set(true);
		}
		lock.unlock();
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
