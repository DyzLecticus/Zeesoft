package nl.zeesoft.zsd.test;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.SequenceClassifier;
import nl.zeesoft.zsd.dialog.dialogs.Generic;
import nl.zeesoft.zsd.interpret.InterpreterConfiguration;

public class TestLanguageContextClassifier extends TestLanguageMasterContextClassifier {
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
		InterpreterConfiguration config = TestInterpreterConfiguration.getConfig(getTester());
		if (config==null) {
			System.out.println("This test has been skipped due to configuration initialization failure");
		} else {
			SequenceClassifier scEng = config.getLanguageContextClassifiers().get(BaseConfiguration.LANG_ENG + Generic.MASTER_CONTEXT_GENERIC);
			SequenceClassifier scNld = config.getLanguageContextClassifiers().get(BaseConfiguration.LANG_NLD + Generic.MASTER_CONTEXT_GENERIC);
			
			assertEqual(scEng.getKnownLinks().size(),356,"The total number of english links does not match expectation");
			assertEqual(scNld.getKnownLinks().size(),418,"The total number of dutch links does not match expectation");

			testSequenceClassification(scEng,new ZStringSymbolParser("What is your goal?"),0D,"QuestionAndAnswer",2);
			System.out.println("");
			testSequenceClassification(scNld,new ZStringSymbolParser("Wat is jouw doel?"),0D,"QuestionAndAnswer",2);
			System.out.println("");
			testSequenceClassification(scEng,new ZStringSymbolParser("What is your name?"),0D,"Handshake",2);
			System.out.println("");
			testSequenceClassification(scNld,new ZStringSymbolParser("Wat is jouw naam?"),0D,"Handshake",2);
			System.out.println("");
			testSequenceClassification(scNld,new ZStringSymbolParser("Wat ben jij?"),0D,"QuestionAndAnswer",2);
		}
	}
}
