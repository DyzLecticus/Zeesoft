package nl.zeesoft.zsd.test;

import java.util.Date;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zsd.SequenceClassifier;
import nl.zeesoft.zsd.entity.EntityObject;
import nl.zeesoft.zsd.util.LanguageContextJsonGenerator;

public class TestLanguageContextClassifier extends TestLanguageMasterContextClassifier {
	public static final String LANGUAGE_FILE_NAME_ENG	= "resources/" + LanguageContextJsonGenerator.FILE_NAME_PREFIX + EntityObject.LANG_ENG + "Generic.json";
	public static final String LANGUAGE_FILE_NAME_NLD	= "resources/" + LanguageContextJsonGenerator.FILE_NAME_PREFIX + EntityObject.LANG_NLD + "Generic.json";
	
	public TestLanguageContextClassifier(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestLanguageContextClassifier(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test uses two JSON datasets to test language specific *SequenceClassifier* instances intended to be used as context classifiers.");
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
			System.out.println("Sequentially initializing the context SequenceClassifier instances took: " + ((new Date()).getTime() - started.getTime()) + " ms");

			assertEqual(scEng.getLinkContextCounts().get(""),156,"The total number of english links does not match expectation");
			assertEqual(scNld.getLinkContextCounts().get(""),184,"The total number of dutch links does not match expectation");

			testSequenceClassification(scEng,new ZStringSymbolParser("What is your goal?"),0.01D,"QuestionAndAnswer",2);
			testSequenceClassification(scEng,new ZStringSymbolParser("What is your name?"),0.01D,"Handshake",2);
			testSequenceClassification(scNld,new ZStringSymbolParser("Wat is jouw doel?"),0.01D,"QuestionAndAnswer",2);
			testSequenceClassification(scNld,new ZStringSymbolParser("Wat is jouw naam?"),0.01D,"Handshake",2);
		}
	}
}
