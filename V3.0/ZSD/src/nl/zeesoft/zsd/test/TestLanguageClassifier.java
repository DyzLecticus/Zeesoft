package nl.zeesoft.zsd.test;

import java.util.List;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.SequenceClassifier;
import nl.zeesoft.zsd.interpret.InterpreterConfiguration;
import nl.zeesoft.zsd.sequence.SequenceClassifierResult;

public class TestLanguageClassifier extends TestSequenceClassifier {
	private static final int	EXPECTED_LINKS	= 592670;
	
	public TestLanguageClassifier(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestLanguageClassifier(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test uses a large JSON dataset to test a *SequenceClassifier* instance intended to be used as a language classifier.");
	}
	
	@Override
	protected void test(String[] args) {
		InterpreterConfiguration config = TestInterpreterConfiguration.getConfig(getTester());
		if (config==null) {
			System.out.println("This test has been skipped due to configuration initialization failure");
		} else {
			SequenceClassifier sc = config.getLanguageClassifier();
			
			assertEqual(sc.getKnownLinks().size(),EXPECTED_LINKS,"The total number of links does not match expectation");
			
			ZStringSymbolParser sequence = new ZStringSymbolParser("Wat is your name?");
			ZStringSymbolParser corrected = null; 
			corrected = sc.correct(sequence,BaseConfiguration.LANG_ENG);
			System.out.println("'" + sequence + "' (ENG) => '" + corrected + "'");
			assertEqual(corrected.toString(),"What is your name?","The correction does not match expectation");

			corrected = sc.correct(sequence,BaseConfiguration.LANG_NLD);
			System.out.println("'" + sequence + "' (NLD) => '" + corrected + "'");
			assertEqual(corrected.toString(),"Wat is jouw naam?","The correction does not match expectation");
			
			sequence = new ZStringSymbolParser("Wie ben jij?");
			System.out.println();
			testClassification(sc,sequence,false,BaseConfiguration.LANG_NLD);
			System.out.println();
			testClassification(sc,sequence,true,BaseConfiguration.LANG_NLD);
			sequence = new ZStringSymbolParser("what is name?");
			System.out.println();
			testClassification(sc,sequence,true,BaseConfiguration.LANG_ENG);
			sequence = new ZStringSymbolParser("twothousand");
			System.out.println();
			testClassification(sc,sequence,true,BaseConfiguration.LANG_ENG);

			List<SequenceClassifierResult> contexts = null;
			double t = 0D;
			sequence = new ZStringSymbolParser("wat is your name?");

			System.out.println();
			t = 0.1D;
			contexts = sc.getContexts(sequence,true,t);
			System.out.println("Context probabilities for '" + sequence + "', threshold: " + t);
			for (SequenceClassifierResult context: contexts) {
				System.out.println("'" + context.symbol + "': " + context.prob + " / " + context.probNormalized);
			}
			assertEqual(contexts.size(),2,"The classifier did not return the expected number of contexts");

			System.out.println();
			t = 0.6D;
			contexts = sc.getContexts(sequence,true,t);
			System.out.println("Context probabilities for '" + sequence + "', threshold: " + t);
			for (SequenceClassifierResult context: contexts) {
				System.out.println("'" + context.symbol + "': " + context.prob + " / " + context.probNormalized);
			}
			assertEqual(contexts.size(),1,"The classifier did not return the expected number of contexts");
		}
	}
}
