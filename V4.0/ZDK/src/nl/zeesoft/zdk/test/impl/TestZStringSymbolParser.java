package nl.zeesoft.zdk.test.impl;

import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
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
		System.out.println("This test shows how to use the *ZStringSymbolParser* to parse symbols (words and punctuation) from a certain text.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the ZStringSymbolParser");
		System.out.println("ZStringSymbolParser parser = new ZStringSymbolParser(\"Example text.\");");
		System.out.println("// Parse the string");
		System.out.println("List<String> symbols = parser.toSymbolsPunctuated();");
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
		ZStringSymbolParser splitter = new ZStringSymbolParser(":-).");
		List<String> symbols = splitter.toSymbolsPunctuated();
		assertEqual(symbols.size(),2,"Smiley 1 was not parsed correctly");
		
		splitter = new ZStringSymbolParser("(-:.");
		symbols = splitter.toSymbolsPunctuated();
		assertEqual(symbols.size(),2,"Smiley 2 was not parsed correctly");

		splitter = new ZStringSymbolParser(":-D.");
		symbols = splitter.toSymbolsPunctuated();
		assertEqual(symbols.size(),2,"Smiley 3 was not parsed correctly");

		splitter = new ZStringSymbolParser(":-(.");
		symbols = splitter.toSymbolsPunctuated();
		assertEqual(symbols.size(),2,"Frowny 1 was not parsed correctly");
		
		splitter = new ZStringSymbolParser(")-:.");
		symbols = splitter.toSymbolsPunctuated();
		assertEqual(symbols.size(),2,"Frowny 2 was not parsed correctly");
		
		splitter = new ZStringSymbolParser(TestZStringEncoder.getTestText());
		System.out.println("Input text: " + splitter);
		symbols = splitter.toSymbolsPunctuated();
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
		assertEqual(symbols.size(),28,"Total parsed symbols does not match expectation");
		ZStringSymbolParser parser = new ZStringSymbolParser();
		parser.fromSymbols(symbols,true,true);
		assertEqual(parser.toString(),TestZStringEncoder.getTestText(),"Merged string does not match expectation");
		assertEqual(splitter,parser,"The splitter does not equal the parser");
		
		ZStringBuilder sb1 = new ZStringBuilder("Zeesoft provides cool software products for application development.");
		ZStringBuilder sb2 = new ZStringBuilder("Zeesoft provides cool software products for application development!");
		ZStringBuilder sb3 = new ZStringBuilder("zeesoft provides cool software products for application development.");
		ZStringBuilder sb4 = new ZStringBuilder("Zeesoft provides cool softwarf products for application development.");
		ZStringBuilder sb5 = new ZStringBuilder("a");
		ZStringBuilder sb6 = new ZStringBuilder("b");
		assertEqual("" + sb1.calculateCRC(),"383798","Calculated CRC does not match expectation (1)");
		assertEqual("" + sb2.calculateCRC(),"383564","Calculated CRC does not match expectation (2)");
		assertEqual("" + sb3.calculateCRC(),"337415","Calculated CRC does not match expectation (3)");
		assertEqual("" + sb4.calculateCRC(),"376085","Calculated CRC does not match expectation (4)");
		assertEqual("" + sb5.calculateCRC(),"776","Calculated CRC does not match expectation (5)");
		assertEqual("" + sb6.calculateCRC(),"784","Calculated CRC does not match expectation (6)");
	}
}
