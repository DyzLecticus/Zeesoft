package nl.zeesoft.zdbd.api;

import nl.zeesoft.zdbd.theme.Event;
import nl.zeesoft.zdbd.theme.EventListener;
import nl.zeesoft.zdbd.theme.ThemeController;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.Lock;
import nl.zeesoft.zdk.thread.ProgressListener;

public class ControllerMonitor implements EventListener, ProgressListener {
	private Lock			lock	= new Lock();
	
	private String			text	= "";
	private int				todo	= 0;
	private int				done	= 0;

	public ControllerMonitor(ThemeController controller) {
		controller.eventPublisher.addListener(this);
	}
	
	@Override
	public void handleEvent(Event event) {
		lock.lock(this);
		if (!event.name.equals(ThemeController.DONE)) {
			this.text = event.name;
		}
		lock.unlock(this);
	}
	
	@Override
	public void initialized(int todo) {
		lock.lock(this);
		this.todo = todo;
		this.done = 0;
		lock.unlock(this);
	}

	@Override
	public void progressed(int steps) {
		lock.lock(this);
		done += steps;
		lock.unlock(this);
	}

	public void startChain(CodeRunnerChain chain) {
		chain.addProgressListener(this);
		chain.start();
	}
	
	public String getText() {
		lock.lock(this);
		String r = this.text;
		lock.unlock(this);
		return r;
	}
	
	public float getDonePercentage() {
		lock.lock(this);
		float r = getDonePercentageNoLock();
		lock.unlock(this);
		return r;
	}
	
	public Str getStateResponse() {
		Str r = new Str();
		lock.lock(this);
		r.sb().append("text:");
		r.sb().append(text);
		r.sb().append("\n");
		r.sb().append("donePercentage:");
		r.sb().append(getDonePercentageNoLock());
		lock.unlock(this);
		return r;
	}
	
	protected float getDonePercentageNoLock() {
		float r = 1;
		if (todo>0) {
			r = ((float)done / (float)todo);
		}
		return r;
	}
}
