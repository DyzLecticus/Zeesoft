package nl.zeesoft.zspr.test;

import java.util.List;

import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zspr.pattern.PatternManager;
import nl.zeesoft.zspr.pattern.PatternObject;

public class TestPatternManager extends TestObject {
	public TestPatternManager(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestPatternManager(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to create and initialzie a *PatternManager* instance and then use it to find and translate some patterns.");
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
		getTester().describeMock(MockPatternManager.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestPatternManager.class));
		System.out.println(" * " + getTester().getLinkForClass(MockPatternManager.class));
		System.out.println(" * " + getTester().getLinkForClass(PatternManager.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows some test strings and their corresponding pattern values.  ");
		System.out.println("Please note how some test strings trigger multiple patterns that translate to different value types.  ");
	}

	@Override
	protected void test(String[] args) {
		PatternManager manager = (PatternManager) getTester().getMockedObject(MockPatternManager.class.getName());
		assertEqual(manager.getPatterns().size(),20,"Number of patterns does not meet expectation");
		
		System.out.println("==> Test English order");
		testStringForPattern(manager,"first",2,"ORDER_ENG:1");
		testStringForPattern(manager,"fiftythird",2,"ORDER_ENG:53");
		testStringForPattern(manager,"threehundredandsixtyninth",2,"ORDER_ENG:369");

		System.out.println();
		System.out.println("==> Test Dutch order");
		testStringForPattern(manager,"eerste",2,"ORDER_NLD:1");
		testStringForPattern(manager,"drieenvijftigste",2,"ORDER_NLD:53");
		testStringForPattern(manager,"driehonderdzesennegentigste",2,"ORDER_NLD:396");

		System.out.println();
		System.out.println("==> Test English time");
		testStringForPattern(manager,"12 o'clock",1,"TIME_ENG:12:00:00");
		testStringForPattern(manager,"twelve o'clock",1,"TIME_ENG:12:00:00");
		testStringForPattern(manager,"twelve oclock",1,"TIME_ENG:12:00:00");
		testStringForPattern(manager,"twelve fifteen",1,"TIME_ENG:12:15:00");
		testStringForPattern(manager,"fifteen past twelve",1,"TIME_ENG:12:15:00");
		testStringForPattern(manager,"fifteen minutes past twelve",1,"TIME_ENG:12:15:00");
		testStringForPattern(manager,"a quarter past twelve",1,"TIME_ENG:12:15:00");
		testStringForPattern(manager,"one minute past one",1,"TIME_ENG:13:01:00");
		testStringForPattern(manager,"one minute to twelve",1,"TIME_ENG:11:59:00");
		testStringForPattern(manager,"twelve thirty",1,"TIME_ENG:12:30:00");
		testStringForPattern(manager,"half past twelve",1,"TIME_ENG:12:30:00");
		testStringForPattern(manager,"twelve fourtyfive",1,"TIME_ENG:12:45:00");
		
		System.out.println();
		System.out.println("==> Test Dutch time");
		testStringForPattern(manager,"12 uur",2,"TIME_NLD:12:00:00","DURATION_NLD:12:00");
		testStringForPattern(manager,"twaalf uur",2,"TIME_NLD:12:00:00","DURATION_NLD:12:00");
		testStringForPattern(manager,"vijftien over twaalf",1,"TIME_NLD:12:15:00");
		testStringForPattern(manager,"vijftien minuten over twaalf",1,"TIME_NLD:12:15:00");
		testStringForPattern(manager,"kwart over twaalf",1,"TIME_NLD:12:15:00");
		testStringForPattern(manager,"een minuut over een",1,"TIME_NLD:13:01:00");
		testStringForPattern(manager,"een minuut voor twaalf",1,"TIME_NLD:11:59:00");
		testStringForPattern(manager,"twaalf uur dertig",1,"TIME_NLD:12:30:00");
		testStringForPattern(manager,"half een",1,"TIME_NLD:12:30:00");
		testStringForPattern(manager,"twaalf uur vijfenveertig",1,"TIME_NLD:12:45:00");

		System.out.println();
		System.out.println("==> Test English number");
		testStringForPattern(manager,"onehundredandeightyone",2,"NUMBER_ENG:181");

		System.out.println();
		System.out.println("==> Test Dutch number");
		testStringForPattern(manager,"driehonderdzevenenzestig",2,"NUMBER_NLD:367");

		System.out.println();
		System.out.println("==> Test Universal time");
		testStringForPattern(manager,"12:14",1,"TIME_UNI:12:14:00");

		System.out.println();
		System.out.println("==> Test Universal number");
		testStringForPattern(manager,"5348",1,"NUMBER_UNI:5348");

		System.out.println();
		System.out.println("==> Test English date");
		testStringForPattern(manager,"december twentysecond",1,"DATE_ENG:2017-12-22");
		testStringForPattern(manager,"january fifth",1,"DATE_ENG:2017-01-05");
		testStringForPattern(manager,"the fifth of january",1,"DATE_ENG:2017-01-05");
		testStringForPattern(manager,"the 12th of august",1,"DATE_ENG:2017-08-12");
		testStringForPattern(manager,"october 2nd",1,"DATE_ENG:2017-10-02");
		testStringForPattern(manager,"now",3,"DATE_ENG:2017-01-01");
		testStringForPattern(manager,"today",2,"DATE_ENG:2017-01-01");
		testStringForPattern(manager,"tomorrow",2,"DATE_ENG:2017-01-02");

		System.out.println();
		System.out.println("==> Test Dutch date");
		testStringForPattern(manager,"tweeentwintig december",1,"DATE_NLD:2017-12-22");
		testStringForPattern(manager,"vijf januari",1,"DATE_NLD:2017-01-05");
		testStringForPattern(manager,"nu",2,"DATE_NLD:2017-01-01");
		testStringForPattern(manager,"vandaag",2,"DATE_NLD:2017-01-01");
		testStringForPattern(manager,"morgen",2,"DATE_NLD:2017-01-02");
		
		System.out.println();
		System.out.println("==> Test English duration");
		testStringForPattern(manager,"two hours",1,"DURATION_ENG:02:00");
		testStringForPattern(manager,"3 hours and 4 minutes",1,"DURATION_ENG:03:04");
		testStringForPattern(manager,"one hour and fourtyfive minutes",1,"DURATION_ENG:01:45");
		testStringForPattern(manager,"one hour and thirtythree minutes",1,"DURATION_ENG:01:33");

		System.out.println();
		System.out.println("Test Dutch duration");
		testStringForPattern(manager,"twee uur",2,"TIME_NLD:14:00:00","DURATION_NLD:02:00");
		testStringForPattern(manager,"3 uur en 4 minuten",1,"DURATION_NLD:03:04");
		testStringForPattern(manager,"een uur en vijfenveertig minuten",1,"DURATION_NLD:01:45");
	}

	private void testStringForPattern(PatternManager manager, String testString, int expectedPatterns, String expectedValue1) {
		testStringForPattern(manager,testString,expectedPatterns,expectedValue1,"");
	}

	private void testStringForPattern(PatternManager manager, String testString, int expectedPatterns, String expectedValue1, String expectedValue2) {
		List<PatternObject> ptns = manager.getMatchingPatternsForString(testString);
		assertEqual(ptns.size(),expectedPatterns,"Number of patterns found for '" + testString + "' does not meet expectation");
		int i = 0;
		for (PatternObject pattern: ptns) {
			i++;
			String value = pattern.getValueForString(testString);
			if (ptns.size()>1) {
				System.out.println(testString + " = " + value + " (" + i + ")");
			} else {
				System.out.println(testString + " = " + value);
			}
			if (i == 1 && expectedValue1.length()>0) {
				assertEqual(value,expectedValue1,"Value does not meet expectation");
			} else if (i == 2 && expectedValue2.length()>0) {
				assertEqual(value,expectedValue2,"Value does not meet expectation");
			}
		}
	}
}
