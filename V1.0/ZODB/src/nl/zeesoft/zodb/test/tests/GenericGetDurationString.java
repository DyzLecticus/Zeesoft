package nl.zeesoft.zodb.test.tests;

import nl.zeesoft.zodb.Generic;

public class GenericGetDurationString {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long hours = 10;
		long minutes = 11;
		long seconds = 22;
		long milliSeconds = 333;
		
		long total = (hours * 3600000) + (minutes * 60000) + (seconds * 1000) + milliSeconds;
		
		System.out.println("Total: " + total);
		System.out.println("Time duration string: " + Generic.getDurationString(total,true,true));

		total = (125280L * 60000L);
		
		System.out.println("Total: " + total);
		System.out.println("Time duration string: " + Generic.getDurationString(total,false,false));
	}

}
