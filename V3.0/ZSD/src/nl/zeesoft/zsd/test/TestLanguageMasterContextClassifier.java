package nl.zeesoft.zsd.test;

import java.util.List;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.SequenceClassifier;
import nl.zeesoft.zsd.interpret.InterpreterConfiguration;
import nl.zeesoft.zsd.sequence.SequenceClassifierResult;

public class TestLanguageMasterContextClassifier extends TestSequenceClassifier {
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
		InterpreterConfiguration config = TestInterpreterConfiguration.getConfig(getTester());
		if (config==null) {
			System.out.println("This test has been skipped due to configuration initialization failure");
		} else {
			SequenceClassifier scEng = config.getLanguageMasterContextClassifiers().get(BaseConfiguration.LANG_ENG);
			SequenceClassifier scNld = config.getLanguageMasterContextClassifiers().get(BaseConfiguration.LANG_NLD);
			
			assertEqual(scEng.getKnownLinks().size(),176,"The total number of english links does not match expectation");
			assertEqual(scNld.getKnownLinks().size(),198,"The total number of dutch links does not match expectation");

			testSequenceClassification(scEng,new ZStringSymbolParser("Who are you?"),0.01D,"Generic",1);
			System.out.println();
			testSequenceClassification(scNld,new ZStringSymbolParser("Wie ben jij?"),0.01D,"Generic",1);
		}
	}
	
	protected void testSequenceClassification(SequenceClassifier sc, ZStringSymbolParser sequence, double threshold, String expectedContext, int expectedContexts) {
		testClassification(sc,sequence,false,expectedContext);
		System.out.println();
		testClassification(sc,sequence,true,expectedContext);
		System.out.println();
		List<SequenceClassifierResult> contexts = sc.getContexts(sequence,true,threshold);
		System.out.println("Context probabilities for '" + sequence + "', threshold: " + threshold);
		for (SequenceClassifierResult context: contexts) {
			System.out.println("'" + context.symbol + "': " + context.prob + " / " + context.probNormalized);
		}
		assertEqual(contexts.size(),expectedContexts,"The classifier did not return the expected number of contexts");
	}
}
