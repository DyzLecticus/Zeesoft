package nl.zeesoft.zsd.test;

import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zsd.SequenceClassifier;
import nl.zeesoft.zsd.entity.EntityObject;
import nl.zeesoft.zsd.sequence.AnalyzerSymbol;
import nl.zeesoft.zsd.util.LanguageMasterContextJsonGenerator;

public class TestLanguageMasterContextClassifier extends TestSequenceClassifier {
	public static final String LANGUAGE_FILE_NAME_ENG = "resources/" + LanguageMasterContextJsonGenerator.FILE_NAME_PREFIX + EntityObject.LANG_ENG + ".json";
	public static final String LANGUAGE_FILE_NAME_NLD = "resources/" + LanguageMasterContextJsonGenerator.FILE_NAME_PREFIX + EntityObject.LANG_NLD + ".json";
	
	public TestLanguageMasterContextClassifier(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestLanguageMasterContextClassifier(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test uses two JSON datasets to test language specific *SequenceClassifier* instances intended to be used as master context classifiers.");
	}
	
	@Override
	protected void test(String[] args) {
		Date started = new Date();
		SequenceClassifier scEng = new SequenceClassifier();
		SequenceClassifier scNld = new SequenceClassifier();
		String err = "";
		err = scEng.initialize(LANGUAGE_FILE_NAME_ENG);
		assertEqual(err.length(),0,"Reading " + LANGUAGE_FILE_NAME_ENG + " produced an unexpected error");
		if (err.length()==0) {
			err = scNld.initialize(LANGUAGE_FILE_NAME_NLD);
			assertEqual(err.length(),0,"Reading " + LANGUAGE_FILE_NAME_NLD + " produced an unexpected error");
		}
		if (err.length()==0) {
			System.out.println("Sequentially initializing the master context SequenceClassifier instances took: " + ((new Date()).getTime() - started.getTime()) + " ms");

			assertEqual(scEng.getLinkContextCounts().get(""),156,"The total number of english links does not match expectation");
			assertEqual(scNld.getLinkContextCounts().get(""),184,"The total number of dutch links does not match expectation");

			testSequenceClassification(scEng,new ZStringSymbolParser("Who are you?"),0.01D,"Generic",1);
			testSequenceClassification(scNld,new ZStringSymbolParser("Wie ben jij?"),0.01D,"Generic",1);
		}
	}
	
	protected void testSequenceClassification(SequenceClassifier sc, ZStringSymbolParser sequence, double threshold, String expectedContext, int expectedContexts) {
		testClassification(sc,sequence,false,expectedContext);
		testClassification(sc,sequence,true,expectedContext);
		System.out.println();
		List<AnalyzerSymbol> contexts = sc.getContexts(sequence,true,threshold);
		System.out.println("Context probabilities for '" + sequence + "', threshold: " + threshold);
		for (AnalyzerSymbol context: contexts) {
			System.out.println("'" + context.symbol + "': " + context.prob);
		}
		assertEqual(contexts.size(),expectedContexts,"The classifier did not return the expected number of contexts");
	}
}
