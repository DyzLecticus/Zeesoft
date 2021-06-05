package nl.zeesoft.zdk.test;

import nl.zeesoft.zdk.Console;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.test.code.TestCodeAnalyzer;
import nl.zeesoft.zdk.test.function.TestExecutor;
import nl.zeesoft.zdk.test.function.TestFunction;
import nl.zeesoft.zdk.test.function.TestFunctionList;
import nl.zeesoft.zdk.test.function.TestFunctionListList;
import nl.zeesoft.zdk.test.json.TestJson;
import nl.zeesoft.zdk.test.matrix.TestMatrix;
import nl.zeesoft.zdk.test.matrix.TestMatrixExecutor;
import nl.zeesoft.zdk.test.matrix.TestMatrixStringConvertor;
import nl.zeesoft.zdk.test.matrix.TestPosition;
import nl.zeesoft.zdk.test.matrix.TestSize;
import nl.zeesoft.zdk.test.neural.TestCell;
import nl.zeesoft.zdk.test.neural.TestCells;
import nl.zeesoft.zdk.test.neural.TestCellsStringConvertor;
import nl.zeesoft.zdk.test.neural.TestClassifier;
import nl.zeesoft.zdk.test.neural.TestClassifierJson;
import nl.zeesoft.zdk.test.neural.TestMerger;
import nl.zeesoft.zdk.test.neural.TestNetwork;
import nl.zeesoft.zdk.test.neural.TestNetworkConfig;
import nl.zeesoft.zdk.test.neural.TestNetworkConfigFactory;
import nl.zeesoft.zdk.test.neural.TestNetworkTrainer;
import nl.zeesoft.zdk.test.neural.TestScalarEncoder;
import nl.zeesoft.zdk.test.neural.TestSdr;
import nl.zeesoft.zdk.test.neural.TestSdrHistory;
import nl.zeesoft.zdk.test.neural.TestSdrStringConvertor;
import nl.zeesoft.zdk.test.neural.TestSegment;
import nl.zeesoft.zdk.test.neural.TestSpatialPooler;
import nl.zeesoft.zdk.test.neural.TestSpatialPoolerJson;
import nl.zeesoft.zdk.test.neural.TestSpatialPoolerOverlap;
import nl.zeesoft.zdk.test.neural.TestTemporalMemory;
import nl.zeesoft.zdk.test.neural.TestTemporalMemoryBurst;
import nl.zeesoft.zdk.test.neural.TestTemporalMemoryJson;

public class ZdkTests {
	public static int sleepMs = 0;
	
	public static void main(String[] args) {
		Logger.setLoggerConsole(new MockConsole());
		long started = System.currentTimeMillis();
		runAllTests(args);
		
		Console.log("");
		Console.log("Tests: SUCCESS");
		long totalMs = (System.currentTimeMillis() - started);
		Console.log("Total time: " + totalMs + " ms");
		Console.log("Sleep time: " + sleepMs + " ms");
		
		Console.log("");
		TestCode.main(args);
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
		Console.log("Test HistoricalFloat ...");
		TestHistoricalFloat.main(args);
		TestHistoricalFloatStringConvertor.main(args);
		Console.log("Test Lock ...");
		TestLock.main(args);
		Console.log("Test Logger ...");
		TestLogger.main(args);
		Console.log("Test Rand ...");
		TestRand.main(args);
		Console.log("Test Util ...");
		TestUtil.main(args);
		Console.log("Test StrUtil ...");
		TestStrUtil.main(args);
		Console.log("Test ArrUtil ...");
		TestArrUtil.main(args);
		Console.log("Test Instantiator ...");
		TestInstantiator.main(args);
		Console.log("Test Reflector ...");
		TestReflector.main(args);

		Console.log("Test Analyzer ...");
		TestCodeAnalyzer.main(args);

		Console.log("Test Function ...");
		TestFunction.main(args);
		Console.log("Test FunctionList ...");
		TestFunctionList.main(args);
		Console.log("Test FunctionListList ...");
		TestFunctionListList.main(args);
		Console.log("Test Executor ...");
		TestExecutor.main(args);
		
		Console.log("Test Json ...");
		TestJson.main(args);
		
		Console.log("Test Position ...");
		TestPosition.main(args);
		Console.log("Test Size ...");
		TestSize.main(args);
		Console.log("Test Matrix ...");
		TestMatrix.main(args);
		TestMatrixStringConvertor.main(args);
		Console.log("Test MatrixExecutor...");
		TestMatrixExecutor.main(args);
		
		Console.log("Test Sdr ...");
		TestSdr.main(args);
		Console.log("Test SdrHistory ...");
		TestSdrHistory.main(args);
		TestSdrStringConvertor.main(args);
		Console.log("Test ScalarEncoder ...");
		TestScalarEncoder.main(args);
		Console.log("Test Cells ...");
		TestSegment.main(args);
		TestCell.main(args);
		TestCells.main(args);
		TestCellsStringConvertor.main(args);
		Console.log("Test SpatialPooler ...");
		TestSpatialPooler.main(args);
		TestSpatialPoolerOverlap.main(args);
		TestSpatialPoolerJson.main(args);
		Console.log("Test TemporalMemory ...");
		TestTemporalMemory.main(args);
		TestTemporalMemoryBurst.main(args);
		TestTemporalMemoryJson.main(args);
		Console.log("Test Classifier ...");
		TestClassifier.main(args);
		TestClassifierJson.main(args);
		Console.log("Test Merger ...");
		TestMerger.main(args);
		Console.log("Test Network ...");
		TestNetworkConfig.main(args);
		TestNetworkConfigFactory.main(args);
		TestNetwork.main(args);
		TestNetworkTrainer.main(args);
	}
}
