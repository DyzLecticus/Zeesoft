package nl.zeesoft.zsc.test;

import java.io.File;
import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zsc.confab.Confabulator;
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
				
				SymbolClusterer clust = new SymbolClusterer(confab,"",true);
				
				Date started = new Date();

				System.out.println("Creating vectors ...");
				clust.createVectors();
				System.out.println("Creating vectors took: " + ((new Date()).getTime() - started.getTime()) + " ms");
				writeVectorDataToTsv(def,clust.getVectors(),"resources/nl-qna-vec.txt");
				
				started = new Date();
				System.out.println("Calculating differences ...");
				clust.calculateDifferences();
				System.out.println("Calculating differences took: " + ((new Date()).getTime() - started.getTime()) + " ms");
				writeMinMaxDifferenceToTsv(def,clust.getDifferences(),"resources/nl-qna-diff.txt");

				System.out.println("Done");
			} else {
				System.err.println(err);
			}
		}
	}
	
	protected void writeVectorDataToTsv(Context context, List<Integer[]> vectors, String fileName) {
		ZStringBuilder tsv = new ZStringBuilder();
		int i = 0;
		for (String symbol: context.knownSymbols) {
			tsv.append(symbol);
			Integer[] vec = vectors.get(i);
			for (int d = 0; d < vec.length; d++) {
				tsv.append("\t");
				tsv.append("" + vec[d]);
			}
			tsv.append("\n");
			i++;
		}
		ZStringBuilder err = tsv.toFile(fileName);
		if (err.length()>0) {
			System.err.println(err);
		}
	}

	protected void writeMinMaxDifferenceToTsv(Context context, List<Double[]> differences, String fileName) {
		ZStringBuilder tsv = new ZStringBuilder();
		int i = 0;
		for (String symbolA: context.knownSymbols) {
			if (!ZStringSymbolParser.isLineEndSymbol(symbolA) &&
				!ZStringSymbolParser.isPunctuationSymbol(symbolA)
				) {
				Double[] difference = differences.get(i);
				double minDiff = 1.0D;
				double maxDiff = 0.0D;
				String minDiffSymbol = "";
				String maxDiffSymbol = "";
				for (int d = 0; d < difference.length; d++) {
					String symbolB = context.knownSymbols.get(d);
					if (!ZStringSymbolParser.isLineEndSymbol(symbolB) &&
						!ZStringSymbolParser.isPunctuationSymbol(symbolB)
						) {
						if (!symbolA.equals(symbolB)) {
							if (difference[d]>=0.0D && difference[d]<minDiff) {
								minDiff = difference[d];
								minDiffSymbol = symbolB;
							}
							if (difference[d]<1.0D && difference[d]>maxDiff) {
								maxDiff = difference[d];
								maxDiffSymbol = symbolB;
							}
						}
					}
				}
				tsv.append(symbolA);
				tsv.append("\t");
				tsv.append(minDiffSymbol);
				tsv.append("\t");
				if (minDiffSymbol.length()>0) {
					tsv.append("" + minDiff);
				}
				tsv.append("\t");
				tsv.append(maxDiffSymbol);
				tsv.append("\t");
				if (maxDiffSymbol.length()>0) {
					tsv.append("" + maxDiff);
				}
				tsv.append("\n");
				i++;
			}
		}
		ZStringBuilder err = tsv.toFile(fileName);
		if (err.length()>0) {
			System.err.println(err);
		}
	}
}
