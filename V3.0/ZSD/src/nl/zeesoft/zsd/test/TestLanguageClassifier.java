package nl.zeesoft.zsd.test;

import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zsd.SequenceClassifier;
import nl.zeesoft.zsd.sequence.AnalyzerSymbol;
import nl.zeesoft.zsd.util.LanguageClassifierJsonGenerator;

public class TestLanguageClassifier extends TestObject {
	public static final String LANGUAGE_FILE_NAME = "resources/" + LanguageClassifierJsonGenerator.FILE_NAME;
	
	public TestLanguageClassifier(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestLanguageClassifier(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test uses a large JSON dataset to test a *SequenceClassifier* instance intended to be used a language classifier.");
	}
	
	@Override
	protected void test(String[] args) {
		Date started = new Date();
		SequenceClassifier sc = new SequenceClassifier();
		String err = sc.initialize(LANGUAGE_FILE_NAME);
		assertEqual(err.length(),0,"Reading the file produced an unexpected error");
		if (err.length()==0) {
			System.out.println("Initializing the language SequenceClassifier took: " + ((new Date()).getTime() - started.getTime()) + " ms");

			assertEqual(sc.getLinkContextCounts().get(""),488551,"The total number of links does not match expectation");
			
			ZStringSymbolParser sequence = new ZStringSymbolParser("Wie ben jij?");
			testClassification(sc,sequence,false,"NLD");
			testClassification(sc,sequence,true,"NLD");
			sequence = new ZStringSymbolParser("what is name?");
			testClassification(sc,sequence,false,"ENG");
			sequence = new ZStringSymbolParser("twothousand");
			testClassification(sc,sequence,false,"ENG");

			List<AnalyzerSymbol> contexts = null;
			double t = 0D;
			sequence = new ZStringSymbolParser("wat is your name?");

			System.out.println();
			t = 0.3D;
			contexts = sc.getContexts(sequence,true,t);
			System.out.println("Context probabilities for '" + sequence + "', threshold: " + t);
			for (AnalyzerSymbol context: contexts) {
				System.out.println("'" + context.symbol + "': " + context.prob);
			}
			assertEqual(contexts.size(),2,"The classifier did not return the expected number of contexts");

			System.out.println();
			t = 0.7D;
			contexts = sc.getContexts(sequence,true,t);
			System.out.println("Context probabilities for '" + sequence + "', threshold: " + t);
			for (AnalyzerSymbol context: contexts) {
				System.out.println("'" + context.symbol + "': " + context.prob);
			}
			assertEqual(contexts.size(),1,"The classifier did not return the expected number of contexts");
		}
	}
	
	private void testClassification(SequenceClassifier sc,ZStringSymbolParser sequence,boolean caseInsensitive, String expectedContext) {
		System.out.println();
		Date started = new Date();
		String context = sc.classify(sequence,caseInsensitive);
		String ci = "";
		if (caseInsensitive) {
			ci = " (case insensitive)";
		}
		System.out.println("Classified sequence" + ci + ": '" + sequence + "' -> " + context);
		System.out.println("Classifying the input sequence took: " + ((new Date()).getTime() - started.getTime()) + " ms");
		assertEqual(context,expectedContext,"The classifier did not return the expected context");
	}
}
