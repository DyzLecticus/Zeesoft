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
			if (activeProgressBars.size()==0) {
				initialize = true;
			}
			activeProgressBars.add(bar);
		}
		lock.unlock(self);
		if (initialize) {
			displayProgressBar(bar.getRenderedBar());
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
			displayProgressBar(bar.getRenderedBar());
			if (next!=null) {
				displayProgressBar(next.getRenderedBar());
			}
		}
	}
	
	protected static void displayProgressBar(Str bar) {
		if (bar.length()>0) {
			System.out.print(bar);
			System.out.print("\r");
		}
	}
}
