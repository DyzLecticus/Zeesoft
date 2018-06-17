package nl.zeesoft.zsmc.test;

import java.io.File;
import java.util.List;

import nl.zeesoft.zdk.test.LibraryObject;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdk.test.impl.ZDK;

/**
 * Documents and tests the ZSMC.
 */
public class ZSMC extends LibraryObject {
	public ZSMC(Tester tester) {
		super(tester);
		setNameAbbreviated("ZSMC");
		setNameFull("Zeesoft Symbolic Multithreaded Confabulation");
		setBaseProjectUrl("https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZSMC/");
		setBaseReleaseUrl("https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZSMC/releases/");
		setBaseSrcUrl("https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSMC/");
		getDependencies().add(new ZDK(null));
	}

	public static void main(String[] args) {
		(new ZSMC(new Tester())).describeAndTest(args);
	}

	@Override
	public void describe() {
		System.out.println("Zeesoft Symbolic Multithreaded Confabulation");
		System.out.println("============================================");
		System.out.println("The Zeesoft Symbolic Multithreaded Confabulation (ZSMC) is an open source library for Java application development.  ");
		System.out.println();
		System.out.println("It provides support for;  ");
		System.out.println(" * Context sensitive symbolic corrections; word spelling corrections  ");
		System.out.println(" * Sequence context classification; sentence context classification  ");
		System.out.println(" * Context sensitive sequence matching; find matching sentences  ");
		System.out.println(" * Symbolic entity value translation; find variable values  ");
		System.out.println();
		describeRelease();
		System.out.println();
		describeTesting(ZSMC.class);
		System.out.println();
	}

	@Override
	public void addTests(List<TestObject> tests) {
		File f = new File(TestSequenceClassifier.QNA_FILE_NAME);
		tests.add(new TestSymbolCorrector(getTester()));
		if (f.exists()) {
			tests.add(new TestSequenceClassifier(getTester()));
			tests.add(new TestSequenceMatcher(getTester()));
		}
		tests.add(new TestEntityValueTranslator(getTester()));
		
		//tests.add(new TestKnowledgeBases(getTester()));
		if (f.exists()) {
			//tests.add(new TestNLQnAKnowledgeBases(getTester()));
		} else {
			System.out.println("NL QnA input file not found: " + TestSequenceClassifier.QNA_FILE_NAME);
		}
	}
}
