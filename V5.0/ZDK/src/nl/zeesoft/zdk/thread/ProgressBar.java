package nl.zeesoft.zdk.thread;

import nl.zeesoft.zdk.Str;

public class ProgressBar implements ProgressListener {
	private Lock	lock			= new Lock();
	private String	description		= "";
	private int		length			= 20;
	
	private int		todo			= 0;
	private int		done			= 0;
	private int		percentage		= 0;

	public ProgressBar(String description) {
		this.description = description;
	}
	
	public ProgressBar(String description, int length) {
		this.description = description;
		this.length = length;
	}
	
	@Override
	public void initialize(int todo) {
		lock.lock(this);
		this.done = 0;
		this.todo = todo;
		this.percentage = 0;
		lock.unlock(this);
		initializeProgressBar(todo);
	}

	@Override
	public void progressed(int done) {
		lock.lock(this);
		this.done += done;
		int dn = this.done;
		int td = todo;
		int pc = percentage;
		lock.unlock(this);
		if (td>0) {
			int newPc = calculatePercentage(dn, td);
			int minDiff = 100 / length;
			if (dn==td || newPc - pc >= minDiff) {
				updateProgressBar(dn,td);
				lock.lock(this);
				percentage = newPc;
				lock.unlock(this);
			}
			if (dn==td) {
				System.out.println();
			}
		}
	}
	
	protected void initializeProgressBar(int todo) {
		if (todo>0) {
			displayProgressBar(renderProgressBar(0,todo));
		}
	}
	
	protected void updateProgressBar(int done, int todo) {
		if (todo>0) {
			displayProgressBar(renderProgressBar(done,todo));
		}
	}
	
	protected Str renderProgressBar(int done, int todo) {
		Str r = new Str();
		int filled = (int)((float)done / (float)todo * (float)length);
		r.sb().append(description);
		r.sb().append(" [");
		for (int i = 0; i < filled; i++) {
			r.sb().append("=");
		}
		for (int i = filled; i < length; i++) {
			r.sb().append(" ");
		}
		r.sb().append("] ");
		r.sb().append(calculatePercentage(done, todo));
		r.sb().append("%");
		return r;
	}
	
	protected void displayProgressBar(Str bar) {
		if (bar.length()>0) {
			System.out.print(bar);
			System.out.print("\r");
		}
	}
	
	private static int calculatePercentage(int done, int todo) {
		return (int)((float)done / (float)todo * 100F);
	}
}
