package nl.zeesoft.zdk.test.grid;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.grid.ColumnFunction;
import nl.zeesoft.zdk.grid.Grid;
import nl.zeesoft.zdk.grid.GridColumn;
import nl.zeesoft.zdk.grid.Position;
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
			public Object applyFunction(GridColumn column, int posZ, Object value) {
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
		
		// Test flatten from 3D to 1D
		grid = new Grid();
		grid.initialize(4, 4, 4, false);
		grid.setValue(0, 1, 0, true);
		grid.setValue(0, 1, 1, true);
		grid.setValue(0, 1, 2, true);
		grid.setValue(0, 1, 3, true);
		assertEqual(grid.getValuePositions(true).toString(),"[0,1,0, 0,1,1, 0,1,2, 0,1,3]","Value positions do not match expectation (1)");
		grid.flatten();
		assertEqual(grid.getValuePositions(true).toString(),"[4,0,0, 4,0,1, 4,0,2, 4,0,3]","Value positions do not match expectation (2)");
		grid.flatten();
		assertEqual(grid.getValuePositions(true).toString(),"[16,0,0, 17,0,0, 18,0,0, 19,0,0]","Value positions do not match expectation (3)");
		
		
		Position pos = new Position(13,2,0);
		List<Position> positions = new ArrayList<Position>();
		positions.add(pos);
		
		positions = Position.squareTo3D(4, positions);
		assertEqual(positions.get(0).toString(),"3,2,1","Squared position does not match expectation");
		
		positions = Position.flattenTo2D(4, positions);
		assertEqual(positions.get(0).toString(),"13,2,0","Squared position does not match expectation");
	}
}
