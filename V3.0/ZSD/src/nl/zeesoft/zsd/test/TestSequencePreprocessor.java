package nl.zeesoft.zsd.test;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zsd.SequencePreprocessor;

public class TestSequencePreprocessor extends TestEntityToJson {
	public TestSequencePreprocessor(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestSequencePreprocessor(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to use a *SequencePreprocessor* instance preprocess a sequence.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the preprocessor");
		System.out.println("SequencePreprocessor sp = new SequencePreprocessor();");
		System.out.println("// Initialize the preprocessor");
		System.out.println("sp.initialize();");
		System.out.println("// Use the processor to process a sequence");
		System.out.println("ZStringSymbolParser sequence sp.process(new ZStringSymbolParser(\"Symbol sequence\"));");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestSequencePreprocessor.class));
		System.out.println(" * " + getTester().getLinkForClass(SequencePreprocessor.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows some sequences and their corresponding preprocessed form.  ");
	}
	
	@Override
	protected void test(String[] args) {
		SequencePreprocessor sp = new SequencePreprocessor();
		sp.initialize();
		JsFile json = sp.toJson();
		ZStringBuilder strA = json.toStringBuilderReadFormat();
		
		sp = new SequencePreprocessor();
		sp.fromJson(json);
		ZStringBuilder strB = json.toStringBuilderReadFormat();
		assertEqual(strA.length(),strB.length(),"The sequence processor data was not correctly parsed from the JSON");
		
		ZStringSymbolParser sequence = new ZStringSymbolParser("I'd have done it but it wouldn't have been fun");
		System.out.println("<- '" + sequence + "'");
		sequence = sp.process(sequence);
		assertEqual(sequence.toString(),"I would have done it but it would not have been fun.","The preprocessed english sequence does not match expectation.");
		System.out.println("-> '" + sequence + "'");

		System.out.println();

		sequence = new ZStringSymbolParser("Ik moet 's morgensvroeg opstaan");
		System.out.println("<- '" + sequence + "'");
		sequence = sp.process(sequence);
		assertEqual(sequence.toString(),"Ik moet smorgens vroeg opstaan.","The preprocessed dutch sequence does not match expectation.");
		System.out.println("-> '" + sequence + "'");
	}
}
