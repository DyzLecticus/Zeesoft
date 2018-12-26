package nl.zeesoft.zsc.test;

import java.io.File;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zsc.confab.Context;
import nl.zeesoft.zsc.confab.SymbolClusterer;

public class TestSymbolClusterer extends TestObject {
	public TestSymbolClusterer(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestSymbolClusterer(new Tester())).test(args);
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
			SymbolClusterer clust = new SymbolClusterer(new Config(),"clusterer",4);
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
							clust.learnSequence(sequence,context);
						}
					}
					num++;
				}
				clust.calculateProbabilities();
				
				Context def = clust.getContext("");
				assertEqual(def.totalSymbols,5507,"Total symbols for default context does not match expectation");
				assertEqual(def.totalLinks,91902,"Total links for default context does not match expectation");
				
				System.out.println("Clustering ...");
				clust.clusterSymbols("",true);
				System.out.println("Done");
			}
		}
	}
}
