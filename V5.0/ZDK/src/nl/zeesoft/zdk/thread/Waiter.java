package nl.zeesoft.zdk.thread;

import java.util.ArrayList;
import java.util.List;

public class Waiter {	
	public static boolean waitFor(Waitable waitable, int waitMs) {
		boolean r = false;
		int sleepMs = 1;
		int waitedMs = 0;
		while(waitable.isBusy() && waitedMs < waitMs) {
			try {
				Thread.sleep(sleepMs);
				waitedMs += sleepMs;
				if (waitedMs >= 100) {
					sleepMs = 50;
				} else if (waitedMs >= 25) {
					sleepMs = 10;
				}
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
		if (waitedMs < waitMs) {
			r = true;
		}
		return r;
	}
	
	public static boolean startAndWaitFor(CodeRunnerChain runnerChain, int waitMs) {
		boolean r = false;
		if (runnerChain.size()>0) {
			runnerChain.start();
			r = waitFor(runnerChain, waitMs);
		}
		return r;
	}
	
	public static boolean startAndWaitFor(CodeRunnerList runnerList, int waitMs) {
		boolean r = false;
		if (runnerList.size()>0) {
			runnerList.start();
			r = waitFor(runnerList, waitMs);
		}
		return r;
	}
	
	public static boolean waitForRunners(List<CodeRunner> runners, int waitMs) {
		List<Waitable> waitables = new ArrayList<Waitable>(runners);
		return waitFor(waitables, waitMs);
	}
	
	public static boolean waitFor(List<Waitable> waitables, int waitMs) {
		boolean r = false;
		int sleepMs = 1;
		int waitedMs = 0;
		while(oneOfWaitablesIsBusy(waitables) && waitedMs < waitMs) {
			try {
				Thread.sleep(sleepMs);
				waitedMs += sleepMs;
				if (waitedMs >= 100) {
					sleepMs = 50;
				} else if (waitedMs >= 25) {
					sleepMs = 10;
				}
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
		if (waitedMs < waitMs) {
			r = true;
		}
		return r;
	}
	
	public static boolean oneOfWaitablesIsBusy(List<Waitable> waitables) {
		boolean r = false;
		for (Waitable waitable: waitables) {
			if (waitable.isBusy()) {
				r = true;
				break;
			}
		}
		return r;
	}
}
