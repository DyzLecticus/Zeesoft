package nl.zeesoft.zdk.test.function;

import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.function.Executor;
import nl.zeesoft.zdk.function.Function;
import nl.zeesoft.zdk.function.FunctionList;
import nl.zeesoft.zdk.function.FunctionListList;
import nl.zeesoft.zdk.test.AllTests;

public class TestExecutor {
	private static TestExecutor self = new TestExecutor();
	
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);

		FunctionListList fll = new FunctionListList();
		fll.addFunctionList(getTestFunctionList());
		fll.addFunctionList(getTestFunctionList());
		
		Executor executor = new Executor();
		
		List<Object> returnValues = executor.execute(self, fll, 100);
		assert returnValues.size() == 6;
		assert !executor.isWorking();

		executor.setNumberOfWorkers(2);
		assert executor.getNumberOfWorkers() == 2;
		returnValues = executor.execute(self, fll, 100);
		assert returnValues.size() == 6;

		returnValues = executor.execute(self, new FunctionListList(), 100);
		assert returnValues.size() == 6;

		returnValues = executor.execute(self, fll, 0);
		assert returnValues.size() == 0;
		assert executor.isWorking();
		while (executor.isWorking()) {
			AllTests.sleep(10);
		}
		
		returnValues = executor.execute(self, fll, 1);
		executor.execute(self, fll, 1);
		assert executor.isWorking();
		assert returnValues.size() < 6;

		executor.setNumberOfWorkers(0);
		assert executor.getNumberOfWorkers() == 0;
	}
	
	protected static FunctionList getTestFunctionList() {
		FunctionList fl = new FunctionList();
		fl.addFunction(new Function() {
			@Override
			protected Object exec() {
				Util.sleep(20);
				return true;
			}
		});
		fl.addFunction(new Function() {
			@Override
			protected Object exec() {
				Util.sleep(20);
				return true;
			}
		});
		fl.addFunction(new Function() {
			@Override
			protected Object exec() {
				Util.sleep(10);
				return true;
			}
		});
		return fl;
	}
}
