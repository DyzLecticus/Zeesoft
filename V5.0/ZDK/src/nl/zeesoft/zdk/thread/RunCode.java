package nl.zeesoft.zdk.thread;

public class RunCode {
	private final Lock	lock		= new Lock();
	
	public Object[] 	params		= new Object[1];
	
	private Exception	exception	= null;
	
	public RunCode() {
		
	}

	public RunCode(Object... params) {
		this.params = params;
	}
	
	/**
	 * Override this method to implement.
	 * 
	 * @return True if the code runner should stop.
	 */
	protected boolean run() {
		// Override to implement
		return false;
	}
	
	public final boolean tryRunCatch() {
		boolean r = false;
		lock.lock(this);
		exception = null;
		lock.unlock(this);
		try {
			r = run();
		} catch(Exception ex) {
			lock.lock(this);
			exception = ex;
			lock.unlock(this);
		}
		return r;
	}
	
	public final Exception getException() {
		lock.lock(this);
		Exception r = exception;
		lock.unlock(this);
		return r;
	}
}
