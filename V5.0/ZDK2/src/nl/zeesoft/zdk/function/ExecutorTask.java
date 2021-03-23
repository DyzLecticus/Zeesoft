package nl.zeesoft.zdk.function;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import nl.zeesoft.zdk.Lock;

public class ExecutorTask {
	protected Lock									lock			= new Lock(this);
	protected Object								caller			= null;
	protected SortedMap<Integer,List<Function>>		stepFunctions	= null;
	protected int									workingStep		= 0;
	protected List<Integer>							workingIndexes	= new ArrayList<Integer>();
	protected List<Integer>							doneIndexes		= new ArrayList<Integer>();
	protected List<Object>							returnValues	= new ArrayList<Object>();
	
	public ExecutorTask(Object caller, SortedMap<Integer,List<Function>> stepFunctions) {
		this.caller = caller;
		this.stepFunctions = stepFunctions;
	}
	
	protected Function getNextFunction() {
		Function r = null;
		lock.lock();
		if (stepFunctions!=null && workingStep>=stepFunctions.size()) {
			stepFunctions = null;
		}
		if (stepFunctions!=null) {
			List<Function> workingFunctions = stepFunctions.get(workingStep);
			Integer index = 0;
			for (Function workingFunction: workingFunctions) {
				if (!doneIndexes.contains(index) && !workingIndexes.contains(index)) {
					workingIndexes.add(index);
					r = workingFunction;
					break;
				}
				index++;
			}
		}
		lock.unlock();
		return r;
	}
	
	protected Object getCaller() {
		lock.lock();
		Object r = caller;
		lock.unlock();
		return r;
	}
	
	protected boolean isDone() {
		lock.lock();
		boolean r = stepFunctions == null; 
		lock.unlock();
		return r;
	}
	
	protected List<Object> getReturnValues() {
		lock.lock();
		List<Object> r = new ArrayList<Object>(returnValues);
		lock.unlock();
		return r;
	}
	
	protected void executedFunction(Function function, Object returnValue) {
		lock.lock();
		returnValues.add(returnValue);
		List<Function> workingFunctions = stepFunctions.get(workingStep);
		Integer index = workingFunctions.indexOf(function);
		workingIndexes.remove(index);
		doneIndexes.add(index);
		if (doneIndexes.size()==workingFunctions.size()) {
			workingStep++;
			doneIndexes.clear();
			if (workingStep>stepFunctions.lastKey()) {
				stepFunctions = null;
			}
		}
		lock.unlock();
	}
}
