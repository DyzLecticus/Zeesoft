package nl.zeesoft.zdk.test.impl;

import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.build.ManifestWriter;
import nl.zeesoft.zdk.test.LibraryObject;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

/**
 * Documents and tests the ZDK.
 */
public class ZDK extends LibraryObject {
	public static final String WRITE_MANIFEST		= "WRITE_MANIFEST";
	
	public ZDK(Tester tester) {
		super(tester);
		setNameAbbreviated("ZDK");
		setNameFull("Zeesoft Development Kit");
		setBaseProjectUrl("https://github.com/DyzLecticus/Zeesoft/tree/master/V4.0/ZDK/");
		setBaseReleaseUrl("https://github.com/DyzLecticus/Zeesoft/raw/master/V4.0/ZDK/releases/");
		setBaseSrcUrl("https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZDK/");
	}

	public static void main(String[] args) {
		if (args!=null && args.length>4 && args[0].equals(WRITE_MANIFEST)) {
			ManifestWriter mw = new ManifestWriter();
			String jars = "";
			if (args.length>5) {
				jars = args[5];
			}
			ZStringBuilder err = mw.writeManifest(args[1],args[2],args[3],args[4],jars);
			if (err.length()>0) {
				System.err.println(err);
				System.exit(1);
			}
		} else {
			(new ZDK(new Tester())).describeAndTest(args);
		}
	}

	@Override
	public void describe() {
		System.out.println("Zeesoft Development Kit");
		System.out.println("=======================");
		System.out.println("The Zeesoft Development Kit (ZDK) is an open source library for Java application development.  ");
		System.out.println();
		System.out.println("It provides support for;  ");
		System.out.println(" * Extended StringBuilder manipulation and validation  ");
		System.out.println(" * Basic file writing and reading  ");
		System.out.println(" * HTTP requests  ");
		System.out.println(" * CSV data  ");
		System.out.println(" * JSON data  ");
		System.out.println(" * Multi threading  ");
		System.out.println(" * Application message handling");
		System.out.println(" * Basic matrix mathematics  ");
		System.out.println(" * Genetic algorithms  ");
		System.out.println(" * Neural networks  ");
		System.out.println(" * Sparse distributed representations  ");
		System.out.println(" * Spatial pooling  ");
		System.out.println(" * Temporal memory  ");
		System.out.println(" * Self documenting and testing libraries  ");
		System.out.println();
		describeRelease();
		System.out.println();
		describeTesting(ZDK.class);
		System.out.println();
	}

	@Override
	public void addTests(List<TestObject> tests) {
		tests.add(new TestZStringEncoder(getTester()));
		tests.add(new TestZStringSymbolParser(getTester()));
		tests.add(new TestCsv(getTester()));
		tests.add(new TestJson(getTester()));
		tests.add(new TestZHttpRequest(getTester()));
		tests.add(new TestMessenger(getTester()));
		tests.add(new TestZMatrix(getTester()));
		tests.add(new TestGeneticCode(getTester()));
		tests.add(new TestNeuralNet(getTester()));
		tests.add(new TestGeneticNN(getTester()));
		tests.add(new TestEvolver(getTester()));
		tests.add(new TestSDR(getTester()));
		tests.add(new TestSDRSet(getTester()));
		tests.add(new TestScalarEncoder(getTester()));
		tests.add(new TestRDScalarEncoder(getTester()));
		tests.add(new TestDateTimeEncoder(getTester()));
		tests.add(new TestDateTimeValueEncoder(getTester()));
		tests.add(new TestPooler(getTester()));
		tests.add(new TestMemory(getTester()));
	}
}
