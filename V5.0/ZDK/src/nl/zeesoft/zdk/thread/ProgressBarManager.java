package nl.zeesoft.zdk.thread;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Str;

public class ProgressBarManager {
	private static Lock					lock					= new Lock();
	private static List<ProgressBar>	activeProgressBars		= new ArrayList<ProgressBar>();
	
	public static synchronized void initializedProgressBar(ProgressBar bar) {
		ProgressBarManager self = new ProgressBarManager();
		lock.lock(self);
		if (!activeProgressBars.contains(bar)) {
			activeProgressBars.add(bar);
			if (activeProgressBars.size()==0) {
				displayProgressBar(bar.getRenderedBar(),false);
			}
		}
		lock.unlock(self);
	}
	
	public static synchronized void updatedProgressBar(ProgressBar bar) {
		ProgressBarManager self = new ProgressBarManager();
		lock.lock(self);
		if (activeProgressBars.indexOf(bar)==0) {
			displayProgressBar(bar.getRenderedBar(),bar.isDone());
			if (bar.isDone()) {
				activeProgressBars.remove(bar);
				if (activeProgressBars.size()>0) {
					ProgressBar next = activeProgressBars.get(0);
					if (!next.isDone()) {
						displayProgressBar(next.getRenderedBar(),false);
					}
				}
			}
		}
		lock.unlock(self);
	}
	
	protected static void displayProgressBar(Str bar, boolean done) {
		if (bar.length()>0) {
			bar.sb().append("\r");
			if (done) {
				bar.sb().append("\n");
			}
			System.out.print(bar);
		}
	}
}
