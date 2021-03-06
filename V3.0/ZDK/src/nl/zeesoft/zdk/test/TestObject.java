package nl.zeesoft.zdk.test;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;

/**
 * Classes that extend this class can be executed by a Tester
 */
public abstract class TestObject {
	private	Tester			tester		= null;
	private int 			assertions	= 0;
	private List<String> 	failures	= new ArrayList<String>();
	private int 			sleepMs		= 0;
	
	public TestObject(Tester tester) {
		this.tester = tester;
	}
	
	/**
	 * Print the description of this test to System.out.
	 */
	protected abstract void describe();
	
	/**
	 * Execute the test code and print the results to System.out.
	 * 
	 * Use the assertEqual methods to check variables in order to ensure assertion statistics and failures are fed back to the Tester.
	 * 
	 * @param args The arguments
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
	 * @return True if the assertion is correct
	 */
	protected boolean assertEqual(String s1, String s2,String failMessage) {
		return handleAssertionResult(s1.equals(s2),failMessage + " ('" + s1 + "' <> '" + s2 + "')");
	}

	/**
	 * Handles errors if the StringBuilders are not equal.
	 * 
	 * @param sb1 StringBuilder 1
	 * @param sb2 StringBuilder 2
	 * @param failMessage The message to log if the assertion fails
	 * @return True if the assertion is correct
	 */
	protected boolean assertEqual(StringBuilder sb1, StringBuilder sb2,String failMessage) {
		ZStringBuilder zsb1 = new ZStringBuilder(sb1);
		ZStringBuilder zsb2 = new ZStringBuilder(sb2);
		return assertEqual(zsb1,zsb2,failMessage);
	}

	/**
	 * Handles errors if the ZStringBuilders are not equal.
	 * 
	 * @param zsb1 ZStringBuilder 1
	 * @param zsb2 ZStringBuilder 2
	 * @param failMessage The message to log if the assertion fails
	 * @return True if the assertion is correct
	 */
	protected boolean assertEqual(ZStringBuilder zsb1, ZStringBuilder zsb2,String failMessage) {
		return handleAssertionResult(zsb1.equals(zsb2),failMessage + " ('" + zsb1 + "' <> '" + zsb2 + "')");
	}
	
	/**
	 * Handles errors if the integers are not equal.
	 * 
	 * @param i1 Integer 1
	 * @param i2 Integer 2
	 * @param failMessage The message to log if the assertion fails
	 * @return True if the assertion is correct
	 */
	protected boolean assertEqual(int i1, int i2,String failMessage) {
		return handleAssertionResult(i1 == i2,failMessage + " (" + i1 + " <> " + i2 + ")");
	}

	/**
	 * Handles errors if the booleans are not equal.
	 * 
	 * @param b1 Boolean 1
	 * @param b2 Boolean 2
	 * @param failMessage The message to log if the assertion fails
	 * @return True if the assertion is correct
	 */
	protected boolean assertEqual(boolean b1, boolean b2,String failMessage) {
		return handleAssertionResult(b1 == b2,failMessage + " (" + b1 + " <> " + b2 + ")");
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
	 * @return True if the assertion is correct
	 */
	protected boolean handleAssertionResult(boolean result,String failMessage) {
		assertions++;
		if (!result) {
			System.err.println("ERROR: " + failMessage);
			failures.add(this.getClass().getName() + ": " + failMessage);
		}
		return result;
	}

	/**
	 * Returns the number of milliseconds spent sleeping.
	 * 
	 * @return the number of milliseconds spent sleeping
	 */
	protected int getSleeptMs() {
		return sleepMs;
	}

	/**
	 * Returns the Tester
	 * 
	 * @return The Tester
	 */
	public Tester getTester() {
		return tester;
	}
}
