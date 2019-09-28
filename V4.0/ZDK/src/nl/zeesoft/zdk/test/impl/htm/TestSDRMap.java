package nl.zeesoft.zdk.test.impl.htm;

import java.util.List;
import java.util.SortedMap;

import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.htm.sdr.SDRMap;
import nl.zeesoft.zdk.htm.sdr.SDRMapElement;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestSDRMap extends TestObject {	
	public TestSDRMap(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestSDRMap(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to use an *SDRMap* to maintain a list of Sparse Distributed Representations.");
		System.out.println("By default, an *SDRMap* uses an index for each bit of every SDR in the list so it can quickly retrieve matching SDRs.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the SDR map");
		System.out.println("SDRMap sdrMap = new SDRMap(100);");
		System.out.println("// Create an SDR");
		System.out.println("SDR sdrA = new SDR(100);");
		System.out.println("sdrA.randomize(2);");
		System.out.println("// Create another SDR");
		System.out.println("SDR sdrB = new SDR(100);");
		System.out.println("sdrB.randomize(2);");
		System.out.println("// Add the SDRs to the SDR map");
		System.out.println("sdrMap.add(sdrA);");
		System.out.println("sdrMap.add(sdrB);");
		System.out.println("// Create a third SDR");
		System.out.println("SDR sdrC = new SDR(100);");
		System.out.println("sdrC.randomize(2);");
		System.out.println("// Get matches from the SDR map");
		System.out.println("SortedMap<Integer,List<SDR>> matchesByOverlapScore = sdrMap.getMatches(sdrC);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestSDRMap.class));
		System.out.println(" * " + getTester().getLinkForClass(SDRMap.class));
		System.out.println(" * " + getTester().getLinkForClass(SDR.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the SDR map, a third SDR and the number of matches for the that SDR in the set.");
	}
	
	@Override
	protected void test(String[] args) {
		SDRMap sdrMap = new SDRMap(100,2);
		SDR sdrA = new SDR(100);
		sdrA.randomize(2);
		SDR sdrB = new SDR(100);
		sdrB.randomize(2);
		sdrMap.add(sdrA);
		sdrMap.add(sdrB);
		System.out.println("SDR map: " + sdrMap.toStringBuilder());
		
		SortedMap<Integer,List<SDRMapElement>> matchesByOverlapScore = null;
		matchesByOverlapScore = sdrMap.getMatches(sdrA);
		System.out.println("Number of SDR A matches in SDR map: " + matchesByOverlapScore.size());
		assertEqual(matchesByOverlapScore.size(),1,"Number of SDR A matches does not match expectation");
		
		SDR sdrC = new SDR(100);
		sdrC.randomize(2);
		System.out.println("SDR C: " + sdrC.toStringBuilder());

		matchesByOverlapScore = sdrMap.getMatches(sdrC);
		System.out.println("Number of SDR C matches in SDR map: " + matchesByOverlapScore.size());
		assertEqual(matchesByOverlapScore.size(),0,"Number of SDR C matches does not match expectation");
		
		SDRMap sdrMapCopy = new SDRMap(100,2);
		sdrMapCopy.fromStringBuilder(sdrMap.toStringBuilder());
		assertEqual(sdrMapCopy.toStringBuilder(),sdrMap.toStringBuilder(),"SDR map copy does not match expectation");
	}
}
