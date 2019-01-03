package nl.zeesoft.zsc.test;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zsc.analyze.AnalyzerVectorizer;
import nl.zeesoft.zsc.analyze.SymbolAnalyzer;
import nl.zeesoft.zsc.confab.Confabulator;
import nl.zeesoft.zsc.confab.Context;

public class TestSymbolAnalyzer extends TestObject {
	public TestSymbolAnalyzer(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestSymbolAnalyzer(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		/*
		 * TODO: Describe
		System.out.println("This test shows how to train a *Confabulator* to and use it to correct sequences and determine context.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the confabulator");
		System.out.println("Confabulator conf = new Confabulator(new Config(),\"MockConfabulator\",4);");
		System.out.println("// Train the confabulator");
		System.out.println("conf.learnSequence(\"A sequence to learn.\",\"OptionalContextSymbolToAssociate\");");
		System.out.println("conf.calculateProbabilities();");
		System.out.println("// Create a correction confabulation");
		System.out.println("CorrectionConfabulation confab1 = new CorrectionConfabulation();");
		System.out.println("confab1.input.append(\"A sequence to correct\");");
		System.out.println("// Confabulate the correction");
		System.out.println("conf.confabulate(confab1);");
		System.out.println("// Create a context confabulation");
		System.out.println("ContextConfabulation confab2 = new ContextConfabulation();");
		System.out.println("confab2.input.append(\"A sequence to determine context for\");");
		System.out.println("// Confabulate the context");
		System.out.println("conf.confabulate(confab2);");
		System.out.println("~~~~");
		System.out.println();
		getTester().describeMock(MockConfabulator.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestSymbolClusterer.class));
		System.out.println(" * " + getTester().getLinkForClass(MockConfabulator.class));
		System.out.println(" * " + getTester().getLinkForClass(Confabulator.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows;  ");
		System.out.println(" * Some details about the trained confabulator  ");
		System.out.println(" * The result of some confabulations  ");
		*/
	}
	
	@Override
	protected void test(String[] args) {
		File file = new File("resources/nl-qna.txt");
		if (file.exists()) {
			Confabulator confab = new Confabulator(new Config(),"nl-qna",4);
			ZStringBuilder content = new ZStringBuilder();
			ZStringBuilder err = content.fromFile(file.getAbsolutePath());
			
			if (err.length()==0) {
				System.out.println("Training ...");
				List<ZStringBuilder> lines = content.split("\n");
				int num = 0;
				for(ZStringBuilder line: lines) {
					if (num>0) {
						List<ZStringBuilder> elems = line.split("\t");
						if (elems.size()>=3) {
							ZStringSymbolParser sequence = new ZStringSymbolParser(elems.get(0));
							ZStringSymbolParser context = new ZStringSymbolParser(elems.get(2));
							confab.learnSequence(sequence,context);
						}
					}
					num++;
				}
				confab.calculateProbabilities();
				
				Context def = confab.getContext("");
				assertEqual(def.totalSymbols,5507,"Total symbols for default context does not match expectation");
				assertEqual(def.totalLinks,91902,"Total links for default context does not match expectation");
				
				SymbolAnalyzer analyzer = new SymbolAnalyzer(confab,"",true);
				
				Date started = new Date();
				
				
				AnalyzerVectorizer vectorizer = new AnalyzerVectorizer(analyzer,10);

				System.out.println("Creating vectors ...");
				vectorizer.vectorize();
				while (!vectorizer.isDone()) {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("Vectors: " + analyzer.getVectors());
				}
				System.out.println("Creating " + analyzer.getVectors() + " vectors took: " + ((new Date()).getTime() - started.getTime()) + " ms");
				
				listCloseRelativesForSymbol(analyzer,"Heeft");
				listCloseRelativesForSymbol(analyzer,"verzekering");
			} else {
				System.err.println(err);
			}
		}
	}
	
	protected void listCloseRelativesForSymbol(SymbolAnalyzer analyzer, String symbol) {
		Date started = new Date();
		System.out.println("Creating differences for '" + symbol + "' ...");
		SortedMap<Double,String> differences = analyzer.getDifferencesForSymbolByDifference(symbol);
		System.out.println("Creating differences for '" + symbol + "' took: " + ((new Date()).getTime() - started.getTime()) + " ms");
		int i = 0;
		System.out.println("Most similar symbols for '" + symbol + "':");
		for (Entry<Double,String> entry: differences.entrySet()) {
			System.out.println(" - '" + entry.getKey() + "': " + entry.getValue());
			if (i >= 4) {
				break;
			}
			i++;
		}
	}
}
