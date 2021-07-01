package nl.zeesoft.zdk.test;

import nl.zeesoft.zdk.Console;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.test.app.TestApp;
import nl.zeesoft.zdk.test.code.TestCodeAnalyzer;
import nl.zeesoft.zdk.test.function.TestExecutor;
import nl.zeesoft.zdk.test.function.TestFunction;
import nl.zeesoft.zdk.test.function.TestFunctionList;
import nl.zeesoft.zdk.test.function.TestFunctionListList;
import nl.zeesoft.zdk.test.http.ZdkHttpTests;
import nl.zeesoft.zdk.test.json.TestJson;
import nl.zeesoft.zdk.test.matrix.TestMatrix;
import nl.zeesoft.zdk.test.matrix.TestMatrixExecutor;
import nl.zeesoft.zdk.test.matrix.TestMatrixStringConvertor;
import nl.zeesoft.zdk.test.matrix.TestPosition;
import nl.zeesoft.zdk.test.matrix.TestSize;
import nl.zeesoft.zdk.test.neural.ZdkNeuralTests;

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
		
		ZdkNeuralTests.main(args);

		Console.log("Test App ...");
		TestApp.main(args);		
		
		ZdkHttpTests.main(args);
	}
}
