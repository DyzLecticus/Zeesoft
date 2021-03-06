package nl.zeesoft.zdk.test;

import nl.zeesoft.zdk.test.function.TestFunction;
import nl.zeesoft.zdk.test.function.TestFunctionList;
import nl.zeesoft.zdk.test.matrix.TestMatrix;
import nl.zeesoft.zdk.test.neural.TestSdr;
import nl.zeesoft.zdk.test.neural.TestSdrHistory;

public class AllTests {
	public static int sleepMs = 0;
	
	public static void main(String[] args) {
		long started = System.currentTimeMillis();
		runAllTests(args);
		System.out.println();
		System.out.println("Tests: SUCCESS");
		long totalMs = (System.currentTimeMillis() - started);
		System.out.println("Total time: " + totalMs + " ms");
		System.out.println("Sleep time: " + sleepMs + " ms");
	}
	
	public static synchronized void sleep(int millis) {
		try {
			Thread.sleep(millis);
			sleepMs+=millis;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	protected static void runAllTests(String[] args) {
		TestLock.main(args);
		TestLogger.main(args);
		TestFunction.main(args);
		TestFunctionList.main(args);
		TestMatrix.main(args);
		TestSdr.main(args);
		TestSdrHistory.main(args);
	}
}
