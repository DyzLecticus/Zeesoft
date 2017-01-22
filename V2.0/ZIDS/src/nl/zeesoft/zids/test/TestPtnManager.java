package nl.zeesoft.zids.test;

import java.util.List;

import nl.zeesoft.zids.dialog.pattern.PtnManager;
import nl.zeesoft.zids.dialog.pattern.PtnObject;

public class TestPtnManager {
	public static void main(String[] args) {
		PtnManager manager = new PtnManager();
		System.out.println("Initializing pattern manager ...");
		manager.initializePatterns();
		System.out.println("Initialized pattern manager");

		System.out.println("==> Test english order");
		testStringForPattern(manager,"first");
		testStringForPattern(manager,"fiftythird");
		testStringForPattern(manager,"threehundredandsixtyninth");

		System.out.println("==> Test dutch order");
		testStringForPattern(manager,"eerste");
		testStringForPattern(manager,"drieenvijftigste");
		testStringForPattern(manager,"driehonderdzesennegentigste");

		System.out.println("==> Test english time");
		testStringForPattern(manager,"12 o'clock");
		testStringForPattern(manager,"twelve o'clock");
		testStringForPattern(manager,"twelve oclock");
		testStringForPattern(manager,"twelve fifteen");
		testStringForPattern(manager,"fifteen past twelve");
		testStringForPattern(manager,"fifteen minutes past twelve");
		testStringForPattern(manager,"a quarter past twelve");
		testStringForPattern(manager,"one minute past one");
		testStringForPattern(manager,"one minute to twelve");
		testStringForPattern(manager,"twelve thirty");
		testStringForPattern(manager,"half past twelve");
		testStringForPattern(manager,"twelve fourtyfive");
		
		System.out.println("==> Test dutch time");
		testStringForPattern(manager,"12 uur");
		testStringForPattern(manager,"twaalf uur");
		testStringForPattern(manager,"vijftien over twaalf");
		testStringForPattern(manager,"vijftien minuten over twaalf");
		testStringForPattern(manager,"kwart over twaalf");
		testStringForPattern(manager,"een minuut over een");
		testStringForPattern(manager,"een minuut voor twaalf");
		testStringForPattern(manager,"twaalf uur dertig");
		testStringForPattern(manager,"half een");
		testStringForPattern(manager,"twaalf uur vijfenveertig");

		System.out.println("==> Test english number");
		testStringForPattern(manager,"onehundredandeightyone");

		System.out.println("==> Test dutch number");
		testStringForPattern(manager,"driehonderdzevenenzestig");

		System.out.println("==> Test universal time");
		testStringForPattern(manager,"12:14");

		System.out.println("==> Test universal number");
		testStringForPattern(manager,"5348");

		System.out.println("==> Test english date");
		testStringForPattern(manager,"december twentysecond");
		testStringForPattern(manager,"january fifth twothousandseventeen");
		testStringForPattern(manager,"the fifth of january");
		testStringForPattern(manager,"the 12th of august");
		testStringForPattern(manager,"october 2nd");
		testStringForPattern(manager,"now");
		testStringForPattern(manager,"today");
		testStringForPattern(manager,"tomorrow");

		System.out.println("==> Test dutch date");
		testStringForPattern(manager,"tweeentwintig december");
		testStringForPattern(manager,"vijf januari tweeduizendzeventien");
		testStringForPattern(manager,"nu");
		testStringForPattern(manager,"vandaag");
		testStringForPattern(manager,"morgen");
		
		System.out.println("==> Test english duration");
		testStringForPattern(manager,"two hours");
		testStringForPattern(manager,"3 hours and 4 minutes");
		testStringForPattern(manager,"one hour and fourtyfive minutes");
		testStringForPattern(manager,"one hour and thirtythree minutes");

		System.out.println("==> Test dutch duration");
		testStringForPattern(manager,"twee uur");
		testStringForPattern(manager,"3 uur en 4 minuten");
		testStringForPattern(manager,"een uur en vijfenveertig minuten");
				
		System.out.println("==> Test scanAndTranslateOutput");
		String test = "I want to book a room for five people on december twentysecond at twentyfive minutes past four for one hour and thirtythree minutes.";
		System.out.println(test);
		StringBuilder result = manager.scanAndTranslateInput(new StringBuilder(test),null);
		System.out.println(result);
		System.out.println(manager.scanAndTranslateOutput(result));
		
		System.out.println("==> Test scanAndTranslateOutput");
		test = "Ik wil een kamer boeken voor vijf personen op dertig december om vijfentwintig minuten over vier voor twee uur.";
		System.out.println(test);
		result = manager.scanAndTranslateInput(new StringBuilder(test),null);
		System.out.println(result);
		System.out.println(manager.scanAndTranslateOutput(result));
		
	}
	
	private static void testStringForPattern(PtnManager manager, String testString) {
		List<PtnObject> ptns = manager.getMatchingPatternsForString(testString);
		if (ptns.size()==0) {
			System.err.println("No pattern found for string: " + testString);
		} else {
			PtnObject pattern = ptns.get(0);
			//System.out.println("Found pattern: " + pattern.getTypeSpecifier() + " (" + ptns.size() + ") = " + pattern.getValueForString(testString));
			System.out.println(testString + " = " + pattern.getValueForString(testString));
		}
	}
}
