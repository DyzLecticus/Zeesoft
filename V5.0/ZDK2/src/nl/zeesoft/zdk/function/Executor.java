package nl.zeesoft.zdk.function;

import java.util.List;

import nl.zeesoft.zdk.Lock;
import nl.zeesoft.zdk.Util;

public class Executor {
	protected Lock									lock			= new Lock(this);
	protected ExecutorWorkers						workers			= new ExecutorWorkers();
	
	protected ExecutorTask							task 			= null;

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
		workers.setNumberOfWorkers(num, this);
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
		if (getNumberOfWorkers()==0) {
			r = executeInCurrentThread(caller, fll);
		} else {
			boolean wait = setTask(caller, fll);
			if (wait) {
				int waitMs = 0;
				while (isWorking() && waitMs<timeoutMs) {
					Util.sleep(1);
					waitMs++;
				}
				r = getReturnValuesIfDone();
			}
		}
		return r;
	}
	
	public boolean isWorking() {
		lock.lock();
		boolean r = (task != null && !task.isDone());
		lock.unlock();
		return r;
	}
	
	protected boolean setTask(Object caller, FunctionListList fll) {
		boolean r = false;
		lock.lock();
		if (task==null || task.isDone()) {
			task = new ExecutorTask(caller, fll.getStepFunctions());
			workers.setSleepMs(0);
			r = true;
		}
		lock.unlock();
		return r;
	}
	
	protected ExecutorTask getTask() {
		lock.lock();
		ExecutorTask r = task;
		lock.unlock();
		return r;
	}
	
	protected List<Object> getReturnValuesIfDone() {
		List<Object> r = null;
		lock.lock();
		if (task.isDone()) {
			r = task.getReturnValues();
		}
		lock.unlock();
		return r;
	}
	
	@SuppressWarnings("unchecked")
	protected List<Object> executeInCurrentThread(Object caller, FunctionListList fll) {
		return (List<Object>) fll.execute(caller);
	}
}
