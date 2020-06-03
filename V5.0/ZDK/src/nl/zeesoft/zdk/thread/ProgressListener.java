package nl.zeesoft.zdk.thread;

public interface ProgressListener {
	public void initialized(int todo);
	public void progressed(int steps);
}
