package nl.zeesoft.zdk.test.function;

import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.function.Executor;
import nl.zeesoft.zdk.function.Function;
import nl.zeesoft.zdk.function.FunctionList;
import nl.zeesoft.zdk.function.FunctionListList;
import nl.zeesoft.zdk.test.ZdkTests;

public class TestExecutor {
	private static TestExecutor self = new TestExecutor();
	
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);

		FunctionListList fll = new FunctionListList();
		fll.addFunctionList(getTestFunctionList(true));
		fll.addFunctionList(getTestFunctionList(false));
		
		Executor executor = new Executor();
		
		List<Object> returnValues = executor.execute(self, fll, 100);
		assert returnValues.size() == 6;
		assert (boolean)returnValues.get(0);
		assert (boolean)returnValues.get(1);
		assert (boolean)returnValues.get(2);
		assert !executor.isWorking();

		executor.setWorkers(2);
		assert executor.getWorkers() == 2;
		returnValues = executor.execute(self, fll, 100);
		assert returnValues.size() == 6;
		assert (boolean)returnValues.get(0);
		assert (boolean)returnValues.get(1);
		assert (boolean)returnValues.get(2);

		returnValues = executor.execute(self, new FunctionListList(), 100);
		assert returnValues == null;

		returnValues = executor.execute(self, fll, 0);
		assert returnValues == null;
		assert executor.isWorking();
		executor.execute(self, fll, 0);
		assert executor.isWorking();
		while (executor.isWorking()) {
			ZdkTests.sleep(10);
		}
		
		executor.setWorkers(0);
		assert executor.getWorkers() == 0;
	}
	
	protected static FunctionList getTestFunctionList(boolean returnValue) {
		FunctionList fl = new FunctionList();
		fl.addFunction(new Function() {
			@Override
			protected Object exec() {
				if (returnValue) {
					Util.sleep(20);
				} else {
					Util.sleep(1);
				}
				return returnValue;
			}
		});
		fl.addFunction(new Function() {
			@Override
			protected Object exec() {
				if (returnValue) {
					Util.sleep(20);
				} else {
					Util.sleep(1);
				}
				return returnValue;
			}
		});
		fl.addFunction(new Function() {
			@Override
			protected Object exec() {
				if (returnValue) {
					Util.sleep(20);
				} else {
					Util.sleep(1);
				}
				return returnValue;
			}
		});
		return fl;
	}
}
