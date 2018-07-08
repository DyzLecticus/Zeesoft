package nl.zeesoft.zsd.test;

import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zsd.SymbolCorrector;

public class TestSymbolCorrector extends TestObject {
	public static final String TEST_SEQUENCE = 
		"I am the kind of stuff that a nice pizza is made of. I am a friendly person. We are the coolest kelvin. " + 
		"It was a rainy day with a cold wind. The fun we have is amazing. It is nice to meet you. " + 
		"There are not enough mice to rule the world. Why is there a mouse in the hat? " +
		"What is an apple? Is it an exception? What is an integer? This is not an argument. " + 
		"If it rings, it's antidisestablishmentarianism, not an anaconda. What is the green bit of an orange?"; 

	public TestSymbolCorrector(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestSymbolCorrector(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to use the *SymbolCorrector* to correct word spelling.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the SymbolCorrector");
		System.out.println("SymbolCorrector corrector = new SymbolCorrector();");
		System.out.println("// Initialize the SymbolCorrector");
		System.out.println("corrector.initialize(new ZStringSymbolParser(\"Some text containing correctly spelled words and complete meaningfull sentences.\"));");
		System.out.println("// Use SymbolCorrector to correct a word");
		System.out.println("String correction = corrector.correct(\"contaning\");");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestSymbolCorrector.class));
		System.out.println(" * " + getTester().getLinkForClass(SymbolCorrector.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the number of generated variations and corrections for certain words.");
	}
	
	@Override
	protected void test(String[] args) {
		Date started = new Date();
		SymbolCorrector sc = new SymbolCorrector();
		if (args!=null && args.length>1 && args[1].length()>0) {
			String err = sc.initialize(args[1]);
			assertEqual(err.length(),0,"Reading the file produced an unexpected error");
			System.out.println("Initializing the SymbolCorrector took: " + ((new Date()).getTime() - started.getTime()) + " ms");
		} else {
			sc.initialize(new ZStringSymbolParser(TEST_SEQUENCE));
			assertEqual(sc.getSymbolContextCounts().get(""),110,"The total number of symbols does not match expectation");
			assertEqual(sc.getLinkContextCounts().get(""),109,"The total number of links does not match expectation");
			System.out.println("Initializing the SymbolCorrector took: " + ((new Date()).getTime() - started.getTime()) + " ms");
			assertEqual("" + sc.getKnownSymbol("the","").prob,"0.045454545454545456","The probability of the word 'the' does not match expectation");
			List<ZStringBuilder> variations = null;
			variations = testVariations(sc,"a",1,80);
			variations = testVariations(sc,"an",1,135);
			variations = testVariations(sc,"the",1,189);
			variations = testVariations(sc,"piza",1,243);
			testVariationsContains(variations,"piza","pizza");
			variations = testVariations(sc,"stfu",2,5);
			testVariationsContains(variations,"stfu","stuff");
			variations = testVariations(sc,"pizza",2,348);
			System.out.println();
			started = new Date();
			testCorrection(sc,"pizzas","pizza");
			testCorrection(sc,"piza","pizza");
			testCorrection(sc,"stuf","stuff");
			testCorrection(sc,"staf","stuff");
			testCorrection(sc,"coolset","coolest");
			testCorrection(sc,"klevin","kelvin");
			testCorrection(sc,"klevni","kelvin");
			testCorrection(sc,"tenfold","tenfold");
			testCorrection(sc,"maus","mouse");
			testCorrection(sc,"excpton","exception");
			testCorrection(sc,"amzaingg","amazing");
			testCorrection(sc,"antidisestablishmantarianims","antidisestablishmentarianism");
			System.out.println("Average correction time: " + (((new Date()).getTime() - started.getTime()) / 12) + " ms");
			ZStringSymbolParser sentence = new ZStringSymbolParser("I am a mouse in the hat.");
			ZStringSymbolParser corrected = sc.correct(sentence);
			assertEqual(corrected,sentence,"The corrected sentence does not match the original");
			corrected = sc.correct(new ZStringSymbolParser("I am na musee in teh hat."));
			assertEqual(corrected,sentence,"The corrected sentence does not match expectation");
		}
	}
	
	private void testVariationsContains(List<ZStringBuilder> variations,String test,String expected) {
		boolean contains = false;
		for (ZStringBuilder var: variations) {
			if (var.toString().equals(expected)) {
				contains = true;
				break;
			}
		}
		assertEqual(contains,true,"The the variations for '" + test + "' do not contain the value '" + expected + "'");
	}

	private List<ZStringBuilder> testVariations(SymbolCorrector sc,String test, int num, int expected) {
		return testVariations(sc,test,num,expected,false);
	}
	
	private List<ZStringBuilder> testVariations(SymbolCorrector sc,String test, int num, int expected, boolean debug) {
		List<ZStringBuilder> variations = sc.generateVariations(test,"");
		String single = "Single";
		if (num==2) {
			variations = sc.addVariations(variations,"");
			single = "Double";
		}
		System.out.println(single + " variations for '" + test + "'; " + variations.size());
		if (debug) {
			for (ZStringBuilder var: variations) {
				System.out.println("- " + var);
			}
		}
		assertEqual(variations.size(),expected,"The generated number of variations not match expectation");
		return variations;
	}
	
	private String testCorrection(SymbolCorrector sc, String test, String expected) {
		String correction = sc.correct(test);
		System.out.println("Correction for '" + test + "'; " + correction);
		assertEqual(correction,expected,"The correction does not match expectation");
		return correction;
	}
}
