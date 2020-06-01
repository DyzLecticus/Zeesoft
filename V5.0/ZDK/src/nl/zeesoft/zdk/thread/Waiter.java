package nl.zeesoft.zdk.thread;

import java.util.ArrayList;
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
	
	public static void startAndWaitTillDone(CodeRunnerChain runnerChain, int waitMs) {
		if (runnerChain.size()>0) {
			runnerChain.start();
			waitTillDone(runnerChain, waitMs);
		}
	}
	
	public static void startAndWaitTillDone(CodeRunnerList runnerList, int waitMs) {
		if (runnerList.size()>0) {
			runnerList.start();
			waitTillDone(runnerList, waitMs);
		}
	}
	
	public static void startAndWaitTillDone(List<CodeRunner> runners, int waitMs) {
		for (CodeRunner runner: runners) {
			runner.start();
		}
		if (runners.size()>0) {
			waitTillRunnersDone(runners, waitMs);
		}
	}
	
	public static void waitTillRunnersDone(List<CodeRunner> runners, int waitMs) {
		List<Waitable> waitables = new ArrayList<Waitable>(runners);
		waitTillDone(waitables, waitMs);
	}
	
	public static void waitTillDone(List<Waitable> waitables, int waitMs) {
		int sleepMs = 1;
		int waitedMs = 0;
		while(oneOfWaitablesIsBusy(waitables) && waitedMs < waitMs) {
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
