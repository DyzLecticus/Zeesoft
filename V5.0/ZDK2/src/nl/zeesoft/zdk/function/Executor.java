package nl.zeesoft.zdk.function;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import nl.zeesoft.zdk.Lock;
import nl.zeesoft.zdk.Util;

public class Executor {
	protected Lock							lock			= new Lock(this);
	protected int							workers			= 0;
	protected ThreadPoolExecutor			executor		= null;
	
	protected SortedMap<Integer,Long>		nsPerStep		= null;
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
			if (workers==0) {
				executor = null;
			} else {
				executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(workers);
			}
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
	
	public List<Object> execute(Object caller, FunctionListList fll, long timeoutMs) {
		List<Object> r = null;
		if (getWorkers()==0) {
			r = executeInCurrentThread(caller, fll);
			nsPerStep = new TreeMap<Integer,Long>(fll.nsPerStep);
		} else {
			SortedMap<Integer,List<Function>> stepFunctions = fll.getStepFunctions();
			if (stepFunctions.size()>0) {
				setTask(caller, stepFunctions);
				int waitNs = 0;
				while (isWorking() && (waitNs / 1000000)<timeoutMs) {
					Util.sleepNs(1000);
					waitNs += 1000;
				}
				if ((waitNs / 1000000)<timeoutMs) {
					r = getReturnValues();
				}
			}
		}
		return r;
	}
	
	public boolean isWorking() {
		lock.lock();
		boolean r = (task!=null && !task.done.get());
		lock.unlock();
		return r;
	}
	
	public SortedMap<Integer,Long> getNsPerStep() {
		lock.lock();
		SortedMap<Integer,Long> r = nsPerStep;
		lock.unlock();
		return r;
	}
	
	protected void setTask(Object caller, SortedMap<Integer,List<Function>> stepFunctions) {
		lock.lock();
		task = new ExecutorTask(caller, stepFunctions);
		addFunctionsToExecutor(task.getWorkingStepFunctions(this));
		lock.unlock();
	}
	
	protected List<Object> getReturnValues() {
		lock.lock();
		List<Object> r = new ArrayList<Object>();
		for (Object object: task.returnValues) {
			r.add(object);
		}
		nsPerStep = new TreeMap<Integer,Long>(task.nsPerStep);
		lock.unlock();
		return r;
	}
	
	protected void executedFunction(ExecutorFunction function, Object returnValue) {
		List<ExecutorFunction> functions = task.executedFunction(returnValue, this);
		if (functions!=null) {
			addFunctionsToExecutor(functions);
		}
	}
	
	@SuppressWarnings("unchecked")
	protected List<Object> executeInCurrentThread(Object caller, FunctionListList fll) {
		return (List<Object>) fll.execute(caller);
	}
	
	protected void addFunctionsToExecutor(List<ExecutorFunction> functions) {
		for (ExecutorFunction function: functions) {
			executor.execute(function);
		}
	}
}
