package nl.zeesoft.zdk.function;

public class Function {
	public FunctionExceptionHandler		exceptionHandler	= null;
	
	public Object						param1				= null;
	public Object						param2				= null;
	public Object						param3				= null;
	
	public Object						caller				= null;
	
	public final Object execute(Object caller) {
		this.caller = caller;
		Object r = null;
		try {
			r = exec();
		} catch (Exception ex) {
			if (exceptionHandler!=null) {
				exceptionHandler.handleException(caller, ex);
			} else {
				FunctionExceptionHandler.handleExceptionDefault(caller, ex);
			}
		}
		return r;
	}
	
	protected Object exec() {
		// Override to implement
		return null;
	}
}
