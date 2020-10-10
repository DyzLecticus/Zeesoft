package nl.zeesoft.zdk.test.neural;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.neural.model.ApicalSegment;
import nl.zeesoft.zdk.neural.model.Cell;
import nl.zeesoft.zdk.neural.model.CellGrid;
import nl.zeesoft.zdk.neural.model.DistalSegment;
import nl.zeesoft.zdk.neural.model.ProximalSegment;
import nl.zeesoft.zdk.neural.model.Segment;
import nl.zeesoft.zdk.neural.model.Synapse;
import nl.zeesoft.zdk.test.util.TestObject;
import nl.zeesoft.zdk.test.util.Tester;

public class TestCellGrid extends TestObject {
	public TestCellGrid(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestCellGrid(new Tester())).runTest(args);
	}

	@Override
	protected void describe() {
		// TODO: Describe
		//System.out.println("This test is not yet included in the ZDK test set");
	}

	@Override
	protected void test(String[] args) {
		CellGrid grid = new CellGrid();
		grid.initialize(8, 2);
		Cell cell = (Cell)grid.getValue(1, 0, 0);
		generateCellSegments(cell, 0);
		cell = (Cell)grid.getValue(2, 1, 0);
		generateCellSegments(cell, 1);
		
		Str str = grid.toStr();
		
		System.out.println(str);
		
		CellGrid grid2 = new CellGrid();
		grid2.fromStr(str);

		Str str2 = grid2.toStr();
		assertEqual(str,str2,"Cell grid Str does not match expectation");
	}
	
	private void generateCellSegments(Cell cell, int basePosX) {
		ProximalSegment ps = new ProximalSegment();
		generateSegmentSynapses(ps, basePosX + 0);
		cell.proximalSegments.add(ps);
		
		ps = new ProximalSegment();
		generateSegmentSynapses(ps, basePosX + 1);
		cell.proximalSegments.add(ps);
		
		DistalSegment ds = new DistalSegment();
		generateSegmentSynapses(ds, basePosX + 2);
		cell.distalSegments.add(ds);
		
		ds = new DistalSegment();
		generateSegmentSynapses(ds, basePosX + 3);
		cell.distalSegments.add(ds);
		
		ApicalSegment as = new ApicalSegment();
		generateSegmentSynapses(as, basePosX + 4);
		cell.apicalSegments.add(as);
		
		as = new ApicalSegment();
		generateSegmentSynapses(as, basePosX + 5);
		cell.apicalSegments.add(as);
	}
	
	private void generateSegmentSynapses(Segment segment, int posX) {
		for (int x = 0; x < 2; x++) {
			Synapse syn = new Synapse();
			syn.connectTo.x = posX;
			syn.connectTo.y = 1;
			syn.connectTo.z = 0;
			syn.permanence = 0.1F;
			segment.synapses.add(syn);
		}
	}
}
