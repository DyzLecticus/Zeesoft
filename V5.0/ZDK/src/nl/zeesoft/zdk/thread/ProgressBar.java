package nl.zeesoft.zdk.thread;

import nl.zeesoft.zdk.Str;

public class ProgressBar implements ProgressListener {
	private Lock		lock			= new Lock();
	
	private String		description		= "";
	private int			length			= 20;
	
	private int			todo			= 0;
	private int			done			= 0;
	private int			percentage		= 0;
	private int			prevPercentage	= 0;
	
	private Str			renderedBar		= new Str();

	public ProgressBar(String description) {
		this.description = description;
	}
	
	public ProgressBar(String description, int length) {
		this.description = description;
		this.length = length;
	}
	
	@Override
	public void initialized(int todo) {
		boolean initialized = false;
		lock.lock(this);
		if (this.done>=this.todo) {
			this.todo = todo;
			done = 0;
			percentage = 0;
			prevPercentage = 0;
			renderedBar = renderProgressBarNoLock(description, length, done, todo);
			initialized = true;
		}
		lock.unlock(this);
		if (initialized) {
			ProgressBarManager.initializedProgressBar(this);
		}
	}

	@Override
	public void progressed(int steps) {
		boolean updated = false;
		lock.lock(this);
		if (todo > 0) {
			done += steps;
			percentage = calculatePercentage(done, todo);
			int minDiff = 100 / length;
			if (minDiff < 1) {
				minDiff = 1;
			}
			if (done >= todo || percentage - prevPercentage >= minDiff) {
				prevPercentage = percentage;
				renderedBar = renderProgressBarNoLock(description, length, done, todo);
				updated = true;
			}
		}
		lock.unlock(this);
		if (updated) {
			ProgressBarManager.updatedProgressBar(this);
		}
	}
	
	protected Str renderProgressBarNoLock(String description, int length, int done, int todo) {
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

	protected final boolean isDone() {
		lock.unlock(this);
		boolean r = done>=todo; 
		lock.unlock(this);
		return r;
	}

	protected final Str getRenderedBar() {
		lock.unlock(this);
		Str r = renderedBar; 
		lock.unlock(this);
		return r;
	}
	
	protected void initializeProgressBar(int todo) {
		if (todo>0) {
			displayProgressBar(renderProgressBar(0,todo));
		}
	}
	
	protected void progressed(int dn, int td, int pc) {
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
