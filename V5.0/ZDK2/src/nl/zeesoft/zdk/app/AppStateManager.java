package nl.zeesoft.zdk.app;

public class AppStateManager {
	public static String	STOPPED		= "Stopped";
	public static String	STARTING	= "Starting";
	public static String	STARTED		= "Started";
	public static String	STOPPING	= "Stopping";
	
	protected String		state		= STOPPED;
	
	public synchronized boolean ifSetState(String state) {
		boolean r = false;
		if (
			(this.state.equals(STOPPED) && state.equals(STARTING)) ||
			(this.state.equals(STARTING) && state.equals(STARTED)) ||
			(this.state.equals(STARTING) && state.equals(STOPPED)) ||
			(this.state.equals(STARTED) && state.equals(STOPPING)) ||
			(this.state.equals(STOPPING) && state.equals(STOPPED))
			) {
			this.state = state;
			r = true;
		}
		return r;
	}
	
	public synchronized String getState() {
		return this.state;
	}
	
	public synchronized boolean isStarted() {
		return this.state.equals(STARTED);
	}
}
