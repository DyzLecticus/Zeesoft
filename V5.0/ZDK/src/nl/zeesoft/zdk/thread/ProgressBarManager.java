package nl.zeesoft.zdk.thread;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Str;

public class ProgressBarManager {
	private static Lock					lock					= new Lock();
	private static List<ProgressBar>	activeProgressBars		= new ArrayList<ProgressBar>();
	
	public static void initializedProgressBar(ProgressBar bar) {
		boolean initialize = false;
		ProgressBarManager self = new ProgressBarManager();
		lock.lock(self);
		if (!activeProgressBars.contains(bar)) {
			activeProgressBars.add(bar);
			if (activeProgressBars.size()==0) {
				initialize = true;
			}
		}
		lock.unlock(self);
		if (initialize) {
			displayProgressBar(bar.getRenderedBar(),false);
		}
	}
	
	public static void updatedProgressBar(ProgressBar bar) {
		boolean update = false;
		ProgressBar next = null;
		ProgressBarManager self = new ProgressBarManager();
		lock.lock(self);
		if (activeProgressBars.indexOf(bar)==0) {
			update = true;
			if (bar.isDone()) {
				activeProgressBars.remove(bar);
				if (activeProgressBars.size()>0) {
					next = activeProgressBars.get(0);
					if (next.isDone()) {
						next = null;
					}
				}
			}
		}
		lock.unlock(self);
		if (update) {
			displayProgressBar(bar.getRenderedBar(),bar.isDone());
			if (next!=null) {
				displayProgressBar(next.getRenderedBar(),false);
			}
		}
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
