package nl.zeesoft.zdk.function;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import nl.zeesoft.zdk.Lock;
import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.Worker;

public class Executor {
	protected Lock									lock			= new Lock(this);
	protected List<Worker>							workers			= new ArrayList<Worker>();
	
	protected Object								caller			= null;
	protected SortedMap<Integer,List<Function>>		stepFunctions	= null;
	protected int									workingStep		= -1;
	protected List<Integer>							workingIndexes	= new ArrayList<Integer>();
	protected List<Integer>							doneIndexes		= new ArrayList<Integer>();
	protected List<Object>							returnValues	= new ArrayList<Object>();

	public Executor() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				setNumberOfWorkers(0);
			}
		});  
	}
	
	public void setNumberOfWorkers(int num) {
		lock.lock();
		if (workers.size() > num) {
			while (workers.size() > num) {
				int index = workers.size() - 1;
				workers.get(index).stop();
				workers.remove(index);
			}
		} else if (workers.size() < num) {
			while (workers.size() < num) {
				int index = workers.size();
				workers.add(getNewWorker());
				workers.get(index).start();
			}
		}
		lock.unlock();
	}
	
	public int getNumberOfWorkers() {
		lock.lock();
		int r = workers.size();
		lock.unlock();
		return r;
	}
	
	public List<Object> execute(Object caller, FunctionListList fll, int timeoutMs) {
		List<Object> r = null;
		boolean wait = execute(caller, fll);
		if (wait) {
			int waitMs = 0;
			while (isWorking() && waitMs<timeoutMs) {
				Util.sleep(1);
				waitMs++;
			}
		}
		if (!isWorking()) {
			lock.lock();
			r = new ArrayList<Object>(returnValues);
			lock.unlock();
		}
		return r;
	}
	
	public boolean isWorking() {
		lock.lock();
		boolean r = (stepFunctions != null);
		lock.unlock();
		return r;
	}
	
	public Function getNextFunction() {
		Function r = null;
		lock.lock();
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
	
	public Object getCaller() {
		lock.lock();
		Object r = caller;
		lock.unlock();
		return r;
	}
	
	public void executedFunction(Function function, Object returnValue) {
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
	
	protected boolean execute(Object caller, FunctionListList fll) {
		boolean r = false;
		lock.lock();
		if (workers.size() == 0) {
			returnValues = executeInCurrentThread(caller, fll);
		} else if (stepFunctions == null) {
			stepFunctions = fll.getStepFunctions();
			if (stepFunctions.size()>0) {
				r = true;
				this.caller = caller;
				workingStep = 0;
				workingIndexes.clear();
				doneIndexes.clear();
				returnValues = new ArrayList<Object>();
				setSleepMs(0);
			} else {
				stepFunctions = null;
			}
		}
		lock.unlock();
		return r;
	}
	
	@SuppressWarnings("unchecked")
	protected List<Object> executeInCurrentThread(Object caller, FunctionListList fll) {
		return (List<Object>) fll.execute(caller);
	}
	
	protected void setSleepMs(int sleepMs) {
		for (Worker worker: workers) {
			worker.setSleepMs(sleepMs);
		}
	}
	
	protected Worker getNewWorker() {
		Worker r = new Worker() {
			@Override
			public void exec() {
				Function function = getNextFunction();
				if (function!=null) {
					Object caller = getCaller();
					Object returnValue = function.execute(caller);
					executedFunction(function, returnValue);
				}
			}
		};
		r.setMinSleepMs(100);
		return r;
	}
}
