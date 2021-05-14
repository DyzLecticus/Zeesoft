package nl.zeesoft.zdk.function;

import java.util.List;
import java.util.SortedMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import nl.zeesoft.zdk.Lock;

public class Executor {
	protected Lock							lock			= new Lock(this);
	protected int							workers			= 0;
	protected ThreadPoolExecutor			executor		= null;
	
	protected ExecutorTask					task			= null;
	
	public Executor() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				setWorkers(0);
			}
		});  
	}
	
	public void setWorkers(int workers) {
		lock.lock();
		if (this.workers!=workers)  {
			if (executor!=null) {
				executor.shutdown();
			}
			setExecutor(workers);
			this.workers = workers;
		}
		lock.unlock();
	}
	
	public int getWorkers() {
		lock.lock();
		int r = workers;
		lock.unlock();
		return r;
	}
	
	public ExecutorTask execute(Object caller, FunctionListList fll, long timeoutMs) {
		ExecutorTask r = null;
		if (getWorkers()==0) {
			r = new ExecutorTask(); 
			executeInCurrentThread(caller, fll, r);
		} else {
			r = executeUsingWorkers(caller, fll, timeoutMs);
		}
		return r;
	}
	
	public boolean isWorking() {
		lock.lock();
		boolean r = (task!=null && !task.done);
		lock.unlock();
		return r;
	}
	
	@SuppressWarnings("unchecked")
	protected void executeInCurrentThread(Object caller, FunctionListList fll, ExecutorTask task) {
		task.returnValues.addAll((List<Object>)fll.execute(caller));
		task.nsPerStep.putAll(fll.nsPerStep);
	}
	
	protected ExecutorTask executeUsingWorkers(Object caller, FunctionListList fll, long timeoutMs) {
		ExecutorTask r = null;
		SortedMap<Integer,List<Function>> stepFunctions = fll.getStepFunctions();
		if (stepFunctions.size()>0) {
			ExecutorTask task = setTask(caller, stepFunctions);
			if (waitTillDone(task, timeoutMs)) {
				r = task;
			}
		}
		return r;
	}
	
	protected boolean waitTillDone(ExecutorTask task, long timeoutMs) {
		boolean r = true;
		long started = System.currentTimeMillis();
		while (!task.done) {
			if (System.currentTimeMillis() - started >= timeoutMs) {
				r = false;
				break;
			}
		}
		return r;
	}

	protected ExecutorTask setTask(Object caller, SortedMap<Integer,List<Function>> stepFunctions) {
		ExecutorTask r = null;
		lock.lock();
		task = new ExecutorTask(caller, stepFunctions);
		addFunctionsToExecutor(task.getWorkingStepFunctions(this));
		r = task;
		lock.unlock();
		return r;
	}
	
	protected void executedFunction(ExecutorFunction function, Object returnValue) {
		List<ExecutorFunction> functions = task.executedFunction(returnValue, this);
		if (functions!=null) {
			addFunctionsToExecutor(functions);
		}
	}
	
	protected void addFunctionsToExecutor(List<ExecutorFunction> functions) {
		for (ExecutorFunction function: functions) {
			executor.execute(function);
		}
	}
	
	protected void setExecutor(int workers) {
		if (workers==0) {
			executor = null;
		} else {
			executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(workers);
		}
	}
}
