package nl.zeesoft.zdk.function;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
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
	
	protected ConcurrentLinkedDeque<Object>			returnValues		= new ConcurrentLinkedDeque<Object>();
	
	protected ExecutorTask(Object caller, SortedMap<Integer,List<Function>> stepFunctions) {
		this.caller = caller;
		this.stepFunctions = stepFunctions;
		for (List<Function> functions: stepFunctions.values()) {
			todo += functions.size();
		}
	}
	
	protected List<ExecutorFunction> getWorkingStepFunctions(Executor executor) {
		List<ExecutorFunction> r = new ArrayList<ExecutorFunction>();
		lock.lock();
		List<Function> functions = stepFunctions.get(workingStep);
		for (Function function: functions) {
			r.add(new ExecutorFunction(caller, function, executor));
			workingFunctions++;
		}
		lock.unlock();
		return r;
	}
	
	protected boolean executedFunction(Object returnValue) {
		boolean r = false;
		if (returnValue!=null) {
			returnValues.add(returnValue);
		}
		lock.lock();
		todo--;
		if (todo==0) {
			done.set(true);
		}
		workingFunctions--;
		if (workingFunctions==0 && todo>0) {
			workingStep++;
			r = true;
		}
		lock.unlock();
		return r;
	}
}
