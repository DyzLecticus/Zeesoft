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
		System.out.println("This test shows how to use a *CellGrid* to model a multi dimensional set of neural cells. ");
		System.out.println("The *Cell* instances in the grid are designed to support the Numenta HTM cell model. ");
		System.out.println("This allows the Numenta HTM algorithms to be implemented on these grids. ");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the grid");
		System.out.println("CellGrid grid = new CellGrid();");
		System.out.println("// Initialize the grid");
		System.out.println("grid.initialize(8, 2);");
		System.out.println("// Get a cell");
		System.out.println("Cell cell = (Cell)grid.getValue(1, 0, 0);");
		System.out.println("// Create a segment (proximal/distal/apical)");
		System.out.println("DistalSegment segment = new DistalSegment();");
		System.out.println("// Attach the segment to the cell");
		System.out.println("cell.distalSegments.add(segment);");
		System.out.println("// Create a synapse");
		System.out.println("Synapse syn = new Synapse();");
		System.out.println("syn.connectTo.x = 1;");
		System.out.println("syn.connectTo.y = 1;");
		System.out.println("syn.connectTo.z = 1;");
		System.out.println("syn.permanence = 0.1F;");
		System.out.println("// Attach the synapse to the segment");
		System.out.println("segment.synapses.add(syn);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestCellGrid.class));
		System.out.println(" * " + getTester().getLinkForClass(CellGrid.class));
		System.out.println(" * " + getTester().getLinkForClass(Cell.class));
		System.out.println(" * " + getTester().getLinkForClass(ProximalSegment.class));
		System.out.println(" * " + getTester().getLinkForClass(DistalSegment.class));
		System.out.println(" * " + getTester().getLinkForClass(ApicalSegment.class));
		System.out.println(" * " + getTester().getLinkForClass(Synapse.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the *Str* form of an example 8 by 2 by 1 cell grid.  ");
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
		assertEqual(str,str2,"Cell grid Str does not match expectation (1)");
		
		grid2 = new CellGrid(grid);
		str2 = grid2.toStr();
		assertEqual(str,str2,"Cell grid Str does not match expectation (2)");
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
			if (posX>1) {
				syn.permanence = 1.0F;
			} else {
				syn.permanence = 0.1F;
			}
			segment.synapses.add(syn);
		}
	}
}
