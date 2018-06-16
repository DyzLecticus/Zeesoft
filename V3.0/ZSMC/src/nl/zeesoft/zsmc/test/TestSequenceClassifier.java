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
		System.out.println("// Use SequenceClassifier to correct a word");
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
		System.out.println(" * The time it takes to correct the spelling of the input sequence  ");
		System.out.println(" * The time it takes to classify the input sequence  ");
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
			ZStringSymbolParser corrected = sc.correct(sequence);
			System.out.println("Correcting the input sequence took: " + ((new Date()).getTime() - started.getTime()) + " ms");
			assertEqual(corrected,sequence,"The corrected sequence does not equal the original sequence");
			
			started = new Date();
			String context = sc.classify(corrected);
			System.out.println("Classifying the input sequence took: " + ((new Date()).getTime() - started.getTime()) + " ms");
			assertEqual(context,"nlPriveBetalen","The classification did not produce the expected output");

			context = sc.classify(corrected,true);
			assertEqual(context,"nlPrivatebankingUwvermogen","The classification did not produce the expected output");

			context = sc.classify(new ZStringSymbolParser("Waar kan ik mijn transacties zien?"));
			assertEqual(context,"nlGrootzakelijkProducten","The classification did not produce the expected output");
			
			context = sc.classify(new ZStringSymbolParser("Heeft de ABN AMRO Rechtsbijstandverzekering"));
			assertEqual(context,"nlPriveVerzekeren","The classification did not produce the expected output");
		}
		
	}
}
