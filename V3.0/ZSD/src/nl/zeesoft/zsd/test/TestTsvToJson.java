package nl.zeesoft.zsd.test;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zsd.sequence.Analyzer;
import nl.zeesoft.zsd.util.TsvToJson;

public class TestTsvToJson extends TestObject {
	public TestTsvToJson(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestTsvToJson(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to use a *TsvToJson* instance to convert a TSV file into a JSON file.");
		System.out.println("Both formats can be used to initialize *Analyzer* instances.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the TsvToJson instance");
		System.out.println("TsvToJson convertor = new TsvToJson();");
		System.out.println("// Convert some TSV data");
		System.out.println("JsFile json = convertor.parseTsv(new ZStringBuilder(\"Question\\tAnswer\\tContext\\nWhat?\\tThat!\\tWTF\\n\"));");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestTsvToJson.class));
		System.out.println(" * " + getTester().getLinkForClass(TsvToJson.class));
		System.out.println(" * " + getTester().getLinkForClass(Analyzer.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the input TSV data and the converted JSON.  ");
	}
	
	@Override
	protected void test(String[] args) {
		TsvToJson convertor = new TsvToJson();
		ZStringBuilder tsv = new ZStringBuilder();
		tsv.append("Question\tAnswer\tContext\n");
		tsv.append("What is that?\tIt is a plant!\tcontextWhatQuestions\n");
		tsv.append("Why is that?\tBecause it can!\tcontextWhyQuestions\n");
		System.out.println(tsv);
		JsFile json = convertor.parseTsv(tsv);
		System.out.println(json.toStringBuilderReadFormat());
		assertEqual(json.rootElement.children.size(),1,"The number of children does not match expectation");
		if (json.rootElement.children.size()>0) {
			assertEqual(json.rootElement.children.get(0).children.size(),2,"The number of sequence elements does not match expectation");
		}
		Analyzer analyzer = new Analyzer();
		analyzer.addSequence(new ZStringSymbolParser(json.toStringBuilder()));
		assertEqual(analyzer.getKnownSymbols().size(),28,"The number of known symbols does not match expectation");
	}
}
