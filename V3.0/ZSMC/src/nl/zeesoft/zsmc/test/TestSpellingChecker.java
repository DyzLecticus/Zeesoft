package nl.zeesoft.zsmc.test;

import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zsmc.SpellingChecker;

public class TestSpellingChecker extends TestObject {
	public static final String TEST_SEQUENCE = "I am the kind of stuff that a nice pizza is made of. We are the coolest kelvin. It was a rainy day with a cold wind. The fun we have is amazing. It is nice to meet you. There are not enough mice to rule the world. Why is there a mouse in the hat?"; 

	public TestSpellingChecker(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestSpellingChecker(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to use the *SpellingChecker* to correct word spelling.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the SpellingChecker");
		System.out.println("SpellChecker checker = new SpellingChecker();");
		System.out.println("// Initialize the SpellingChecker");
		System.out.println("checker.initialize(new ZStringSymbolParser(\"Some text containing correctly spelled words.\"));");
		System.out.println("// Use SpellingChecker to correct a word");
		System.out.println("String correction = checker.correct(\"contaning\");");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestSpellingChecker.class));
		System.out.println(" * " + getTester().getLinkForClass(SpellingChecker.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the number of generated variations and corrections for certain words.");
	}
	
	@Override
	protected void test(String[] args) {
		Date started = new Date();
		SpellingChecker sc = new SpellingChecker();
		if (args!=null && args.length>0 && args[1].length()>0) {
			String err = sc.initialize(args[1]);
			assertEqual(err.length(),0,"Reading the file produced an unexpected error");
			System.out.println("Initializing the SpellingChecker took: " + ((new Date()).getTime() - started.getTime()) + " ms");
		} else {
			sc.initialize(new ZStringSymbolParser(TEST_SEQUENCE));
			assertEqual(sc.getTotalSymbols(),63,"The total number of symbols does not match expectation");
			System.out.println("Initializing the SpellingChecker took: " + ((new Date()).getTime() - started.getTime()) + " ms");
			assertEqual("" + sc.getKnownSymbols().get("the").prob,"0.07936507936507936","The probability of the word 'the' does not match expectation");
			List<ZStringBuilder> variations = null;
			variations = testVariations(sc,"a",1,78);
			variations = testVariations(sc,"an",1,133);
			variations = testVariations(sc,"the",1,187);
			variations = testVariations(sc,"piza",1,241);
			testVariationsContains(variations,"piza","pizza");
			variations = testVariations(sc,"stfu",2,6271);
			testVariationsContains(variations,"stfu","stuff");
			variations = testVariations(sc,"pizza",2,8014);
			System.out.println();
			started = new Date();
			testCorrection(sc,"pizza","pizza");
			testCorrection(sc,"piza","pizza");
			testCorrection(sc,"stuf","stuff");
			testCorrection(sc,"staf","stuff");
			testCorrection(sc,"coolset","coolest");
			testCorrection(sc,"klevin","kelvin");
			testCorrection(sc,"klevni","kelvin");
			testCorrection(sc,"tenfold","tenfold");
			testCorrection(sc,"maus","mouse");
			testCorrection(sc,"rny","rainy");
			testCorrection(sc,"amzaingg","amazing");
			System.out.println("Average correction time: " + (((new Date()).getTime() - started.getTime()) / 11) + " ms");
			ZStringSymbolParser sentence = new ZStringSymbolParser("I am a mouse in the hat.");
			ZStringSymbolParser corrected = sc.correct(sentence);
			assertEqual(corrected,sentence,"The corrected sentence does not match the original");
			corrected = sc.correct(new ZStringSymbolParser("I am a musee in teh hat."));
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

	private List<ZStringBuilder> testVariations(SpellingChecker sc,String test, int num, int expected) {
		return testVariations(sc,test,num,expected,false);
	}
	
	private List<ZStringBuilder> testVariations(SpellingChecker sc,String test, int num, int expected, boolean debug) {
		List<ZStringBuilder> variations = sc.generateVariations(test);
		String single = "Single";
		if (num==2) {
			variations = sc.addVariations(variations);
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
	
	private String testCorrection(SpellingChecker sc, String test, String expected) {
		String correction = sc.correct(test);
		System.out.println("Correction for '" + test + "'; " + correction);
		assertEqual(correction,expected,"The correction does not match expectation");
		return correction;
	}
}
