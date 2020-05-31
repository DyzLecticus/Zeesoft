package nl.zeesoft.zdk.thread;

import java.util.ArrayList;
import java.util.List;

public class CodeRunnerManager {
	private static final Lock				lock			= new Lock();
	private static final List<CodeRunner> 	activeRunners	= new ArrayList<CodeRunner>();
	
	public static void startedRunner(CodeRunner runner) {
		Object self = new CodeRunnerManager();
		lock.lock(self);
		activeRunners.add(runner);
		lock.unlock(self);
	}
	
	public static void stoppedRunner(CodeRunner runner) {
		Object self = new CodeRunnerManager();
		lock.lock(self);
		activeRunners.remove(runner);
		lock.unlock(self);
	}
	
	public static List<CodeRunner> getActiverRunners() {
		Object self = new CodeRunnerManager();
		lock.lock(self);
		List<CodeRunner> r = new ArrayList<CodeRunner>(activeRunners);
		lock.unlock(self);
		return r;
	}
}
