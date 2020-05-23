package nl.zeesoft.zdk.thread;

public class RunCode {
	public Object[] 	params		= new Object[1];
	
	private Exception	exception	= null;
	
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
		exception = null;
		try {
			r = run();
		} catch(Exception ex) {
			exception = ex;
		}
		return r;
	}
	
	public final Exception getException() {
		return exception;
	}
}
