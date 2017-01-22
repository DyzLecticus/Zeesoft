package nl.zeesoft.zdk.test;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Generic;

/**
 * Classes that extend this class can be executed by the Tester singleton
 */
public abstract class TestObject {
	private int 			assertions	= 0;
	private List<String> 	failures	= new ArrayList<String>();
	private int 			sleepMs		= 0;
	
	/**
	 * Print the description of this test to System.out.
	 */
	protected abstract void describe();
	
	/**
	 * Execute the test code and print the results to System.out.
	 * 
	 * Use the assertEquals methods to check variables in order to ensure assertion statistics and failures are fed back to the Tester.
	 */
	protected abstract void test(String[] args);
	
	/**
	 * Increments the number of milliseconds spent sleeping.
	 * Calls Thread.sleep().
	 * 
	 * @param ms The number of milliseconds to sleep
	 */
	protected final void sleep(int ms) {
		sleepMs += ms;
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Handles errors if the Strings are not equal.
	 * 
	 * @param s1 String 1
	 * @param s2 String 2
	 * @param failMessage The message to log if the assertion fails
	 */
	protected final void assertEqual(String s1, String s2,String failMessage) {
		handleAssertionResult(s1.equals(s2),failMessage + " ('" + s1 + "' <> '" + s2 + "')");
	}

	/**
	 * Handles errors if the StringBuilders are not equal.
	 * 
	 * @param sb1 StringBuilder 1
	 * @param sb2 StringBuilder 2
	 * @param failMessage The message to log if the assertion fails
	 */
	protected final void assertEqual(StringBuilder sb1, StringBuilder sb2,String failMessage) {
		handleAssertionResult(Generic.stringBuilderEquals(sb1, sb2),failMessage + " ('" + sb1 + "' <> '" + sb2 + "')");
	}

	/**
	 * Handles errors if the integers are not equal.
	 * 
	 * @param i1 Integer 1
	 * @param i2 Integer 2
	 * @param failMessage The message to log if the assertion fails
	 */
	protected final void assertEqual(int i1, int i2,String failMessage) {
		handleAssertionResult(i1 == i2,failMessage + " (" + i1 + " <> " + i2 + ")");
	}

	/**
	 * Handles errors if the booleans are not equal.
	 * 
	 * @param b1 Boolean 1
	 * @param b2 Boolean 2
	 * @param failMessage The message to log if the assertion fails
	 */
	protected final void assertEqual(boolean b1, boolean b2,String failMessage) {
		handleAssertionResult(b1 == b2,failMessage + " (" + b1 + " <> " + b2 + ")");
	}

	/**
	 * Returns the number of assertions to the Tester.
	 * 
	 * @return the number of assertions
	 */
	protected int getAssertions() {
		return assertions;
	}

	/**
	 * Returns failures to the Tester.
	 * 
	 * @return A list of failure messages
	 */
	protected final List<String> getFailures() {
		return failures;
	}
	
	/**
	 * Increments the total number of assertions and logs a failure message if the result is false.
	 * 
	 * @param result The result of the assertion
	 * @param failMessage The message used to log if the result is false
	 */
	protected final void handleAssertionResult(boolean result,String failMessage) {
		assertions++;
		if (!result) {
			System.err.println("ERROR: " + failMessage);
			failures.add(this.getClass().getName() + ": " + failMessage);
		}
	}

	/**
	 * Returns the number of milliseconds spent sleeping.
	 * 
	 * @return the number of milliseconds spent sleeping
	 */
	protected int getSleeptMs() {
		return sleepMs;
	}
}
