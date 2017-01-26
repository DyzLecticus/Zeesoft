package nl.zeesoft.zspr.test;

import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zspr.pattern.PatternManager;

public class TestPatternManagerScan extends TestObject {
	public static void main(String[] args) {
		(new TestPatternManagerScan()).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to create and initialzie a *PatternManager* instance and then use it to translate a sentence to its value and back.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");

		System.out.println("// Create pattern manager");
		System.out.println("PatternManager manager = new PatternManager();");
		System.out.println("// Initialize patterns");
		System.out.println("manager.initializePatterns();");
		System.out.println("// Get patterns for string");
		System.out.println("List<PatternObject> patterns = manager.getMatchingPatternsForString(\"one hour and fourtyfive minutes\");");
		System.out.println("// Translate string to pattern value");
		System.out.println("String value = patterns.get(0).getValueForString(\"one hour and fourtyfive minutes\");");
		System.out.println("~~~~");
		System.out.println();
		Tester.getInstance().describeMock(MockPatternManager.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + Tester.getInstance().getLinkForClass(TestPatternManagerScan.class));
		System.out.println(" * " + Tester.getInstance().getLinkForClass(PatternManager.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows some test strings and their corresponding value translation and back to string translation.  ");
		System.out.println("Please note how the date of the request is inferred automatically by the pattern translation mechanism.  ");
	}

	@Override
	protected void test(String[] args) {
		PatternManager manager = (PatternManager) Tester.getInstance().getMockedObject(MockPatternManager.class.getName());
				
		System.out.println("==> Test English");
		testScanAndTranslate(manager,
			"I want to book a room for five people on december twentysecond at twentyfive minutes past four for one hour and thirtythree minutes.",
			"ALPHABETIC_UNI:I ALPHABETIC_UNI:want ALPHABETIC_UNI:to ALPHABETIC_UNI:book ALPHABETIC_UNI:a ALPHABETIC_UNI:room ALPHABETIC_UNI:for NUMBER_ENG:5|ALPHABETIC_UNI:five ALPHABETIC_UNI:people ALPHABETIC_UNI:on DATE_ENG:2017-12-22 ALPHABETIC_UNI:at TIME_ENG:16:25:00 ALPHABETIC_UNI:for DURATION_ENG:01:33 .",
			"I want to book a room for five people on december twentysecond twothousandseventeen at twentyfive past four for one hour and thirtythree minutes ."
			);
		System.out.println();
		System.out.println("==> Test Dutch");
		testScanAndTranslate(manager,
			"Ik wil een kamer boeken voor vijf personen op dertig december om vijfentwintig minuten over vier voor twee uur.",
			"ALPHABETIC_UNI:Ik ALPHABETIC_UNI:wil NUMBER_NED:1|ALPHABETIC_UNI:een ALPHABETIC_UNI:kamer ALPHABETIC_UNI:boeken ALPHABETIC_UNI:voor NUMBER_NED:5|ALPHABETIC_UNI:vijf ALPHABETIC_UNI:personen ALPHABETIC_UNI:op DATE_NED:2017-12-30 ALPHABETIC_UNI:om TIME_NED:16:25:00 ALPHABETIC_UNI:voor TIME_NED:14:00:00|DURATION_NED:02:00 .",
			"Ik wil een kamer boeken voor vijf personen op dertig december tweeduizendzeventien om vier uur vijfentwintig voor twee uur ."
			);
	}

	private void testScanAndTranslate(PatternManager manager,String from,String expectedTo,String expectedBack) {
		System.out.println("Input: " + from);
		StringBuilder to = manager.scanAndTranslateInput(new StringBuilder(from),null);
		assertEqual(to.toString(),expectedTo,"String to value translation does not meet expectation");
		System.out.println("Values: " + to);
		StringBuilder back = manager.scanAndTranslateOutput(new StringBuilder(to));
		assertEqual(back.toString(),expectedBack,"Value to string does not meet expectation");
		System.out.println("String: " + back);
	}
}
