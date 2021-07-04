package nl.zeesoft.zdk.app.neural;

public class NetworkStateManager {
	public static String	CREATED			= "Created";
	public static String	INITIALIZING	= "Initializing";
	public static String	LOADING			= "Loading";
	public static String	RESETTING		= "Resetting";
	public static String	READY			= "Ready";
	public static String	PROCESSING		= "Processing";
	public static String	SAVING			= "Saving";
	
	protected String		state			= CREATED;
	
	public synchronized boolean ifSetState(String state) {
		boolean r = false;
		if (isAllowedTransition(state)) {
			this.state = state;
			r = true;
		}
		return r;
	}
	
	public synchronized String getState() {
		return this.state;
	}
	
	public synchronized boolean isReady() {
		return this.state.equals(READY);
	}
	
	protected boolean isAllowedTransition(String state) {
		return (
			(this.state.equals(CREATED) && state.equals(INITIALIZING)) ||
			(this.state.equals(INITIALIZING) && state.equals(LOADING)) ||
			(this.state.equals(LOADING) && state.equals(READY)) ||
			(this.state.equals(READY) && state.equals(RESETTING)) ||
			(this.state.equals(RESETTING) && state.equals(READY)) ||
			(this.state.equals(READY) && state.equals(PROCESSING)) ||
			(this.state.equals(PROCESSING) && state.equals(READY)) ||
			(this.state.equals(READY) && state.equals(SAVING)) ||
			(this.state.equals(SAVING) && state.equals(READY))
		);
	}
}
