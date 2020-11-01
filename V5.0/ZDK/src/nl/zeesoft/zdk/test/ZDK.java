package nl.zeesoft.zdk.test;

import java.util.List;

import nl.zeesoft.zdk.test.collection.TestCollections;
import nl.zeesoft.zdk.test.grid.TestGrid;
import nl.zeesoft.zdk.test.http.TestHttpServer;
import nl.zeesoft.zdk.test.neural.TestCellGrid;
import nl.zeesoft.zdk.test.neural.TestClassifier;
import nl.zeesoft.zdk.test.neural.TestScalarEncoder;
import nl.zeesoft.zdk.test.neural.TestMerger;
import nl.zeesoft.zdk.test.neural.TestNetwork;
import nl.zeesoft.zdk.test.neural.TestProcessorFactory;
import nl.zeesoft.zdk.test.neural.TestSDR;
import nl.zeesoft.zdk.test.neural.TestSpatialPooler;
import nl.zeesoft.zdk.test.neural.TestTemporalMemory;
import nl.zeesoft.zdk.test.thread.TestCodeRunnerChain;
import nl.zeesoft.zdk.test.thread.TestRunCode;
import nl.zeesoft.zdk.test.util.LibraryObject;
import nl.zeesoft.zdk.test.util.TestObject;
import nl.zeesoft.zdk.test.util.Tester;

/**
 * Documents and tests the ZDK.
 */
public class ZDK extends LibraryObject {
	public ZDK(Tester tester) {
		super(tester);
		setNameAbbreviated("ZDK");
		setNameFull("Zeesoft Development Kit");
		setBaseProjectUrl("https://github.com/DyzLecticus/Zeesoft/tree/master/V5.0/ZDK/");
		setBaseReleaseUrl("https://github.com/DyzLecticus/Zeesoft/raw/master/V5.0/ZDK/releases/");
		setBaseSrcUrl("https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDK/");
	}

	public static void main(String[] args) {
		(new ZDK(new Tester())).describeAndTest(args);
	}

	@Override
	public void describe() {
		System.out.println("Zeesoft Development Kit");
		System.out.println("=======================");
		System.out.println("The Zeesoft Development Kit (ZDK) is an open source library for Java application development.  ");
		System.out.println("It provides support for;  ");
		System.out.println(" * Self documenting and testing libraries  ");
		System.out.println(" * (Mock) File writing and reading  ");
		System.out.println(" * Dynamic class instantiation and reflection  ");
		System.out.println(" * Extended StringBuilder manipulation and validation  ");
		System.out.println(" * TimeStamped logging  ");
		System.out.println(" * Multi threading  ");
		System.out.println(" * Collection queries and persistence  ");
		System.out.println(" * HTTP servers and requests  ");
		System.out.println(" * Advanced MIDI instrument and sequence pattern design  ");
		System.out.println(" * High performance manipulation of large multi dimensional data structures  ");
		System.out.println(" * [Hierarchical Temporal Memory](https://numenta.com/)  ");
		System.out.println("   * Sparse distributed representations  ");
		System.out.println("   * Detailed neural cell modeling  ");
		System.out.println("   * Spatial pooling  ");
		System.out.println("   * Temporal memory  ");
		System.out.println("   * Value classification and prediction  ");
		System.out.println("   * Neural networks  ");
		System.out.println("  ");
		System.out.println("HTM implementation notes;  ");
		System.out.println(" * SDRs have two dimensions by default in order to retain topographical properties throughout networks  ");
		System.out.println(" * Individual SDR processors and networks use multi threading to maximize performance  ");
		System.out.println(" * The spatial pooler does not support local inhibition  ");
		System.out.println(" * The temporal memory does not generate any initial segments/synapses  ");
		System.out.println(" * The temporal memory supports optional apical feedback  ");
		System.out.println(" * The classifier can be configured to slowly forget old SDR associations  ");
		System.out.println(" * The implementation allows for SDR processor and network customization via configuration and/or code extension   ");
		System.out.println("  ");
		describeRelease();
		System.out.println();
		describeTesting(ZDK.class);
		System.out.println();
	}

	@Override
	public void addTests(List<TestObject> tests) {
		tests.add(new TestStr(getTester()));
		tests.add(new TestRunCode(getTester()));
		tests.add(new TestCodeRunnerChain(getTester()));
		tests.add(new TestCollections(getTester()));
		tests.add(new TestHttpServer(getTester()));
		tests.add(new TestGrid(getTester()));
		tests.add(new TestSDR(getTester()));
		tests.add(new TestCellGrid(getTester()));
		tests.add(new TestScalarEncoder(getTester()));
		tests.add(new TestSpatialPooler(getTester()));
		tests.add(new TestTemporalMemory(getTester()));
		tests.add(new TestClassifier(getTester()));
		tests.add(new TestMerger(getTester()));
		tests.add(new TestProcessorFactory(getTester()));
		tests.add(new TestNetwork(getTester()));
	}
}
