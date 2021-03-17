package nl.zeesoft.zdk.test;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.test.function.TestFunction;
import nl.zeesoft.zdk.test.function.TestFunctionList;
import nl.zeesoft.zdk.test.matrix.TestMatrix;
import nl.zeesoft.zdk.test.matrix.TestPosition;
import nl.zeesoft.zdk.test.matrix.TestSize;
import nl.zeesoft.zdk.test.neural.TestCell;
import nl.zeesoft.zdk.test.neural.TestCells;
import nl.zeesoft.zdk.test.neural.TestClassifier;
import nl.zeesoft.zdk.test.neural.TestMerger;
import nl.zeesoft.zdk.test.neural.TestScalarEncoder;
import nl.zeesoft.zdk.test.neural.TestSdr;
import nl.zeesoft.zdk.test.neural.TestSdrHistory;
import nl.zeesoft.zdk.test.neural.TestSegment;
import nl.zeesoft.zdk.test.neural.TestSpatialPooler;
import nl.zeesoft.zdk.test.neural.TestSpatialPoolerOverlap;
import nl.zeesoft.zdk.test.neural.TestTemporalMemory;
import nl.zeesoft.zdk.test.neural.TestTemporalMemoryBurst;

public class AllTests {
	public static int sleepMs = 0;
	
	public static void main(String[] args) {
		Logger.setLoggerConsole(new MockConsole());
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
		TestRand.main(args);
		TestUtil.main(args);
		TestFunction.main(args);
		TestFunctionList.main(args);
		TestPosition.main(args);
		TestSize.main(args);
		TestMatrix.main(args);
		TestSdr.main(args);
		TestScalarEncoder.main(args);
		TestSdrHistory.main(args);
		TestSpatialPooler.main(args);
		TestSpatialPoolerOverlap.main(args);
		TestSegment.main(args);
		TestCell.main(args);
		TestCells.main(args);
		TestTemporalMemory.main(args);
		TestTemporalMemoryBurst.main(args);
		TestClassifier.main(args);
		TestMerger.main(args);
	}
}
