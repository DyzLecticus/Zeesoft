package nl.zeesoft.zdk.function;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Worker;

public class ExecutorWorkers {
	protected List<Worker>	workers		= new ArrayList<Worker>();

	protected void setNumberOfWorkers(int num, Executor executor) {
		if (workers.size() > num) {
			while (workers.size() > num) {
				int index = workers.size() - 1;
				workers.get(index).stop();
				workers.remove(index);
			}
		} else if (workers.size() < num) {
			while (workers.size() < num) {
				int index = workers.size();
				workers.add(getNewWorker(executor));
				workers.get(index).start();
			}
		}
	}
	
	protected int size() {
		return workers.size();
	}
	
	protected void setSleepMs(int sleepMs) {
		for (Worker worker: workers) {
			worker.setSleepMs(sleepMs);
		}
	}
	
	protected Worker getNewWorker(Executor executor) {
		Worker r = new Worker() {
			@Override
			public void exec() {
				ExecutorTask task = executor.getTask();
				if (task!=null) {
					Function function = task.getNextFunction();
					if (function!=null) {
						Object caller = task.getCaller();
						Object returnValue = function.execute(caller);
						task.executedFunction(function, returnValue);
					}
				}
			}
		};
		r.setMinSleepMs(100);
		return r;
	}
}
