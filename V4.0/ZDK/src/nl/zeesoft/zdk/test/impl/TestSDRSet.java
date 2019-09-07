package nl.zeesoft.zdk.test.impl;

import java.util.List;
import java.util.SortedMap;

import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.htm.sdr.SDRSet;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestSDRSet extends TestObject {	
	public TestSDRSet(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestSDRSet(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to use an *SDRSet* to maintain sets of Sparse Distributed Representations.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the SDR set");
		System.out.println("SDRSet sdrSet = new SDRSet(100);");
		System.out.println("// Create an SDR");
		System.out.println("SDR sdrA = new SDR(100);");
		System.out.println("sdrA.randomize(2);");
		System.out.println("// Create another SDR");
		System.out.println("SDR sdrB = new SDR(100);");
		System.out.println("sdrB.randomize(2);");
		System.out.println("// Add the SDRs to the SDR set");
		System.out.println("sdrSet.add(sdrA);");
		System.out.println("sdrSet.add(sdrB);");
		System.out.println("// Create a third SDR");
		System.out.println("SDR sdrC = new SDR(100);");
		System.out.println("sdrC.randomize(2);");
		System.out.println("// Get matches from the SDR set");
		System.out.println("SortedMap<Integer,List<SDR>> matchesByOverlapScore = sdrSet.getMatches(sdrC);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestSDRSet.class));
		System.out.println(" * " + getTester().getLinkForClass(SDRSet.class));
		System.out.println(" * " + getTester().getLinkForClass(SDR.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the SDR set, a third SDR and the number of matches for the that SDR in the set.");
	}
	
	@Override
	protected void test(String[] args) {
		SDRSet sdrSet = new SDRSet(100);
		SDR sdrA = new SDR(100);
		sdrA.randomize(2);
		SDR sdrB = new SDR(100);
		sdrB.randomize(2);
		sdrSet.add(sdrA);
		sdrSet.add(sdrB);
		System.out.println("SDR set: " + sdrSet.toStringBuilder());
		
		SDR sdrC = new SDR(100);
		sdrC.randomize(2);
		System.out.println("SDR C: " + sdrC.toStringBuilder());
		
		SortedMap<Integer,List<SDR>> matchesByOverlapScore = sdrSet.getMatches(sdrC);
		System.out.println("Number of SDR C matches in SDR set: " + matchesByOverlapScore.size());
		assertEqual(matchesByOverlapScore.size(),0,"Number of matches does not match expectation");
		
		SDRSet sdrSetCopy = new SDRSet(100);
		sdrSetCopy.fromStringBuilder(sdrSet.toStringBuilder());
		assertEqual(sdrSetCopy.toStringBuilder(),sdrSet.toStringBuilder(),"SDR set copy does not match expectation");
	}
}
