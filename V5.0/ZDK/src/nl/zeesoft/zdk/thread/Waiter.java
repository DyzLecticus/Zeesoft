package nl.zeesoft.zdk.thread;

import java.util.List;

public class Waiter {
	public static void waitWhile(RunCode whileCode, int waitMs) {
		int sleepMs = 1;
		int waitedMs = 0;
		while(whileCode.tryRunCatch() && waitedMs < waitMs) {
			try {
				Thread.sleep(sleepMs);
				waitedMs += sleepMs;
				if (waitedMs >= 100) {
					sleepMs = 50;
				} else if (waitedMs >= 10) {
					sleepMs = 10;
				}
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public static void waitTillDone(Waitable waitable, int waitMs) {
		int sleepMs = 1;
		int waitedMs = 0;
		while(waitable.isBusy() && waitedMs < waitMs) {
			try {
				Thread.sleep(sleepMs);
				waitedMs += sleepMs;
				if (waitedMs >= 100) {
					sleepMs = 50;
				} else if (waitedMs >= 10) {
					sleepMs = 10;
				}
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public static void startAndWaitTillDone(List<CodeRunner> runners, int waitMs) {
		for (CodeRunner runner: runners) {
			runner.start();
		}
		if (runners.size()>0) {
			waitTillDone(runners, waitMs);
		}
	}
	
	public static void waitTillDone(List<CodeRunner> runners, int waitMs) {
		int sleepMs = 1;
		int waitedMs = 0;
		while(oneOfRunnersIsBusy(runners) && waitedMs < waitMs) {
			try {
				Thread.sleep(sleepMs);
				waitedMs += sleepMs;
				if (waitedMs >= 100) {
					sleepMs = 50;
				} else if (waitedMs >= 10) {
					sleepMs = 10;
				}
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public static boolean oneOfRunnersIsBusy(List<CodeRunner> runners) {
		boolean r = false;
		for (CodeRunner runner: runners) {
			if (runner.isBusy()) {
				r = true;
				break;
			}
		}
		return r;
	}
}