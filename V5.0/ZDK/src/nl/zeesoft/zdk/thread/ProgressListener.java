package nl.zeesoft.zdk.thread;

public interface ProgressListener {
	public void initialize(int todo);
	public void progressed(int done);
}
