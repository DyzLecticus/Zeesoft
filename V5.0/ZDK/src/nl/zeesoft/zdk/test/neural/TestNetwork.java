package nl.zeesoft.zdk.test.neural;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.neural.KeyValueSDR;
import nl.zeesoft.zdk.neural.network.Network;
import nl.zeesoft.zdk.neural.network.NetworkConfig;
import nl.zeesoft.zdk.test.util.TestObject;
import nl.zeesoft.zdk.test.util.Tester;

public class TestNetwork extends TestObject {
	public TestNetwork(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestNetwork(new Tester())).runTest(args);
	}

	@Override
	protected void describe() {
		/* TODO: Describe
		System.out.println("This test shows how to use a *CellGrid* grid to model a multi dimensional set of neural cells. ");
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
		System.out.println(" * " + getTester().getLinkForClass(TestNetwork.class));
		System.out.println(" * " + getTester().getLinkForClass(CellGrid.class));
		System.out.println(" * " + getTester().getLinkForClass(Cell.class));
		System.out.println(" * " + getTester().getLinkForClass(ProximalSegment.class));
		System.out.println(" * " + getTester().getLinkForClass(DistalSegment.class));
		System.out.println(" * " + getTester().getLinkForClass(ApicalSegment.class));
		System.out.println(" * " + getTester().getLinkForClass(Synapse.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the *Str* form of an example 8*2*1 cell grid.  ");
		*/
	}

	@Override
	protected void test(String[] args) {
		NetworkConfig config = new NetworkConfig();
		config.inputNames.add(KeyValueSDR.DEFAULT_VALUE_KEY);
		
		config.addEncoder("EN");
		config.addSpatialPooler("SP");
		config.addTemporalMemory("TM");
		config.addClassifier("CL");
		
		config.addLink(KeyValueSDR.DEFAULT_VALUE_KEY, 0, "EN", 0);
		config.addLink("EN", 0, "SP", 0);
		config.addLink("SP", 0, "TM", 0);
		config.addLink("TM", 0, "CL", 0);
		config.addLink("value", 0, "CL", 1);

		Str err = config.testConfiguration();
		assertEqual(err,new Str(),"Error does not match expectation");
		
		Network network = new Network(config);
		network.initialize(true);
	}
}
