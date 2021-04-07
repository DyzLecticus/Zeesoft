package nl.zeesoft.zdk.function;

public class ExecutorFunction implements Runnable {
	protected Object		caller		= null;
	protected Function		function	= null;
	protected Executor		executor	= null;
	
	protected ExecutorFunction(Object caller, Function function, Executor executor) {
		this.caller = caller;
		this.function = function;
		this.executor = executor;
	}

	@Override
	public void run() {
		Object returnValue = function.execute(caller);
		executor.executedFunction(this, returnValue);
	}
}
