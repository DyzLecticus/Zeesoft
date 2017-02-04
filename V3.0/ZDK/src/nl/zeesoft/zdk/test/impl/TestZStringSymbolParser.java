package nl.zeesoft.zdk.test.impl;

import java.util.List;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestZStringSymbolParser extends TestObject {
	public TestZStringSymbolParser(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestZStringSymbolParser(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to use the *SymbolParser* class to parse symbols (words and punctuation) from a certain text.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("List<String> symbols = SymbolParser.parseSymbolsFromText(new StringBuilder(\"Example text.\"));");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestZStringSymbolParser.class));
		System.out.println(" * " + getTester().getLinkForClass(ZStringSymbolParser.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the input text and the parsed symbols separated by spaces.");
	}
	
	@Override
	protected void test(String[] args) {
		ZStringSymbolParser parser = new ZStringSymbolParser(TestZStringEncoder.getTestText());
		System.out.println("Input text: " + parser);
		List<String> symbols = parser.toSymbolsPunctuated();
		System.out.print("Parsed symbols: ");
		int i = 0;
		for (String symbol: symbols) {
			if (i>0) {
				System.out.print(" ");
			}
			i++;
			System.out.print(symbol);
		}
		System.out.println();
		assertEqual(symbols.size(),14,"Total parsed symbols does not match expectation");
		parser.fromSymbols(symbols,true,true);
		assertEqual(parser.toString(),TestZStringEncoder.getTestText(),"Merged string does not match expectation");
	}
}
