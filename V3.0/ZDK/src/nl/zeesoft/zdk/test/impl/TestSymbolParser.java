package nl.zeesoft.zdk.test.impl;

import java.util.List;

import nl.zeesoft.zdk.Generic;
import nl.zeesoft.zdk.SymbolParser;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestSymbolParser extends TestObject {
	public static void main(String[] args) {
		(new TestSymbolParser()).test(args);
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
		System.out.println(" * " + Tester.getInstance().getLinkForClass(TestSymbolParser.class));
		System.out.println(" * " + Tester.getInstance().getLinkForClass(SymbolParser.class));
		System.out.println(" * " + Tester.getInstance().getLinkForClass(Generic.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the input text and the parsed symbols separated by spaces.");
	}
	
	@Override
	protected void test(String[] args) {
		StringBuilder text = new StringBuilder(TestEncoderDecoder.getTestText());
		System.out.println("Input text: " + text);
		List<String> symbols = SymbolParser.parseSymbolsFromText(text);
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
		text = SymbolParser.textFromSymbols(symbols,true,true);
		assertEqual(text.toString(),TestEncoderDecoder.getTestText(),"Merged string does not match expectation");
	}
}
