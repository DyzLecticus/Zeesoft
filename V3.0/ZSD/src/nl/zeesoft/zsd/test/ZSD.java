package nl.zeesoft.zsd.test;

import java.io.File;
import java.util.List;

import nl.zeesoft.zdk.test.LibraryObject;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdk.test.impl.ZDK;

/**
 * Documents and tests the ZSD.
 */
public class ZSD extends LibraryObject {
	public ZSD(Tester tester) {
		super(tester);
		setNameAbbreviated("ZSD");
		setNameFull("Zeesoft Smart Dialogs");
		setBaseProjectUrl("https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZSD/");
		setBaseReleaseUrl("https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZSD/releases/");
		setBaseSrcUrl("https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSD/");
		getDependencies().add(new ZDK(null));
	}

	public static void main(String[] args) {
		(new ZSD(new Tester())).describeAndTest(args);
	}

	@Override
	public void describe() {
		System.out.println("Zeesoft Smart Dialogs");
		System.out.println("=====================");
		System.out.println("The Zeesoft Smart Dialogs (ZSD) is an open source library for Java application development.  ");
		System.out.println();
		System.out.println("It provides support for;  ");
		System.out.println(" * Context sensitive symbolic corrections; word spelling corrections  ");
		System.out.println(" * Sequence context classification; sentence context classification  ");
		System.out.println(" * Context sensitive sequence matching; find matching sentences  ");
		System.out.println(" * Symbolic entity value translation; find variable values  ");
		System.out.println();
		describeRelease();
		System.out.println();
		describeTesting(ZSD.class);
		System.out.println();
	}

	@Override
	public void addTests(List<TestObject> tests) {
		File qna = new File(TestSequenceClassifier.QNA_FILE_NAME);
		tests.add(new TestTsvToJson(getTester()));
		tests.add(new TestSymbolCorrector(getTester()));

		if (qna.exists()) {
			tests.add(new TestSequenceClassifier(getTester()));
			tests.add(new TestSequenceMatcher(getTester()));
		}
		
		tests.add(new TestEntityValueTranslator(getTester()));

		tests.add(new TestBaseConfigurationToJson(getTester()));
		tests.add(new TestEntityToJson(getTester()));
		tests.add(new TestDialogToJson(getTester()));
		tests.add(new TestDialogSetToJson(getTester()));
		
		if (qna.exists()) {
			tests.add(new TestInitializer(getTester()));
		}
		
		tests.add(new TestInterpreterConfiguration(getTester()));
		tests.add(new TestLanguageClassifier(getTester()));
		tests.add(new TestLanguageMasterContextClassifier(getTester()));
		tests.add(new TestLanguageContextClassifier(getTester()));
		tests.add(new TestSequenceInterpreter(getTester()));

		tests.add(new TestDialogHandlerConfiguration(getTester()));
		
		if (!qna.exists()) {
			System.out.println("Some tests were skipped because the NL QnA input file was not found: " + TestSequenceClassifier.QNA_FILE_NAME);
		}
	}
}
