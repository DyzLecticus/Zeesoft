package nl.zeesoft.zdk.test.grid;

import nl.zeesoft.zdk.grid.ColumnFunction;
import nl.zeesoft.zdk.grid.Grid;
import nl.zeesoft.zdk.grid.GridColumn;
import nl.zeesoft.zdk.test.util.TestObject;
import nl.zeesoft.zdk.test.util.Tester;
import nl.zeesoft.zdk.thread.CodeRunnerList;
import nl.zeesoft.zdk.thread.Waiter;

public class TestGrid extends TestObject {
	public TestGrid(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestGrid(new Tester())).runTest(args);
	}

	@Override
	protected void describe() {
		// TODO: Describe
		//System.out.println("This test is not yet included in the ZDK test set");
	}

	@Override
	protected void test(String[] args) {
		Grid grid = new Grid();
		grid.setValue(10, 15, 4, 0.5F);
		grid.initialize(48, 48, 16, 0F);
		assertEqual(grid.getColumns().size(), 2304, "Number of columns does not match expectation");
		System.out.println("Columns: " + grid.getColumns().size());
		
		assertEqual((float)grid.getValue(10, 15, 4), 0.0F, "Value does not match expectation (1)");
		System.out.println("Value at (10, 15, 4): " + grid.getValue(10, 15, 4));
		
		grid.setValue(10, 15, 4, 0.5F);
		assertEqual((float)grid.getValue(10, 15, 4), 0.5F, "Value does not match expectation (2)");
		System.out.println("New value at (10, 15, 4): " + grid.getValue(10, 15, 4));
		
		grid.getValue(10, 15, 17);
		grid.setValue(10, 150, 4, 0.5F);
		assertEqual((float)grid.getValue(10, 15, 4), 0.5F, "Value does not match expectation (3)");
		
		ColumnFunction increment1 = new ColumnFunction() {
			@Override
			public Object applyFunction(GridColumn column, Object value) {
				return (float)value + 1F;
			}
		};
		grid.applyFunction(increment1);
		assertEqual((float)grid.getValue(10, 15, 4), 1.5F, "Value does not match expectation (4)");
		System.out.println("New value at (10, 15, 4): " + grid.getValue(10, 15, 4));
		
		CodeRunnerList runnerList = new CodeRunnerList();
		grid.applyFunction(increment1, runnerList);
		Waiter.startAndWaitFor(runnerList, 100);
		assertEqual((float)grid.getValue(10, 15, 4), 2.5F, "Value does not match expectation (5)");
		System.out.println("New value at (10, 15, 4): " + grid.getValue(10, 15, 4));
		
		Waiter.startAndWaitFor(runnerList, 100);
		assertEqual((float)grid.getValue(10, 15, 4), 3.5F, "Value does not match expectation (6)");
		System.out.println("New value at (10, 15, 4): " + grid.getValue(10, 15, 4));
	}
}
