package nl.zeesoft.zdk.thread;

public class LockListener {
	protected void lockFailed(Object source, int attempts) {
		System.err.println("Lock failed after " + attempts + " attempts. Source: " + source);
	}
}
