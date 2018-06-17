package nl.zeesoft.zsmc.test;

import java.util.Date;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zsmc.SequenceClassifier;

public class TestSequenceClassifier extends TestObject {
	public static final String QNA_FILE_NAME = "resources/nl-qna.txt";
	
	public TestSequenceClassifier(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestSequenceClassifier(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to use the *SequenceClassifier* to classify a sequence.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the SequenceClassifier");
		System.out.println("SequenceClassifier classifier = new SequenceClassifier();");
		System.out.println("// Initialize the SequenceClassifier");
		System.out.println("classifier.initialize(\"tab-separated-file-name.tsv\");");
		System.out.println("// Use SequenceClassifier to classify a sequence");
		System.out.println("String context = classifier.classify(new ZStringSymbolParser(\"some sequence\"));");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestSequenceClassifier.class));
		System.out.println(" * " + getTester().getLinkForClass(SequenceClassifier.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows;  ");
		System.out.println(" * The time it takes to initialize the classifier  ");
		System.out.println(" * The classification results including the time it takes, for a set of input sequences  ");
	}
	
	@Override
	protected void test(String[] args) {
		Date started = new Date();
		SequenceClassifier sc = new SequenceClassifier();
		String err = sc.initialize(QNA_FILE_NAME);
		assertEqual(err.length(),0,"Reading the file produced an unexpected error");
		if (err.length()==0) {
			System.out.println("Initializing the SequenceClassifier took: " + ((new Date()).getTime() - started.getTime()) + " ms");

			assertEqual(sc.getLinkContextCounts().get(""),204474,"The total number of links does not match expectation");
			
			started = new Date();
			ZStringSymbolParser sequence = new ZStringSymbolParser("Wat kost dat?");
			testClassification(sc,sequence,false,"nlPriveBetalen");
			testClassification(sc,sequence,true,"nlPrivatebankingUwvermogen");
			sequence = new ZStringSymbolParser("Waar kan ik mijn transacties zien?");
			testClassification(sc,sequence,false,"nlGrootzakelijkProducten");
			sequence = new ZStringSymbolParser("Heeft de ABN AMRO Rechtsbijstandverzekering");
			testClassification(sc,sequence,false,"nlPriveVerzekeren");
		}
	}
	
	private void testClassification(SequenceClassifier sc,ZStringSymbolParser sequence,boolean caseInsensitvie, String expectedContext) {
		System.out.println();
		Date started = new Date();
		String context = sc.classify(sequence,caseInsensitvie);
		System.out.println("Classified sequence: '" + sequence + "' -> " + context);
		System.out.println("Classifying the input sequence took: " + ((new Date()).getTime() - started.getTime()) + " ms");
		assertEqual(context,expectedContext,"The classifier did not return the expected context");
	}
}
