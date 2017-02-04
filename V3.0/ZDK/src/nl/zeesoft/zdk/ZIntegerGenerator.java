package nl.zeesoft.zdk;

import java.util.Random;

/**
 * Zeesoft Random.
 * 
 * Generates random non-negative integers.
 */
public class ZIntegerGenerator {
	private int min		= 0;
	private int max		= 1;
	
	/**
	 * @param min The minimum (non-negative) value
	 * @param max The maximum value
	 */
	public ZIntegerGenerator(int min,int max) {
		if (min<0) {
			min = 0;
		}
		if (min>max) {
			max = min + 1;
		}
		this.min = min;
		this.max = max;
		if (min<max) {
			this.min = min;
			this.max = max;
			Random rand = new Random();
			int num = -1;
			while (num < min) {
				 num = (int) (rand.nextDouble() * (max + 1));
			}
		}
	}
	
	/**
	 * Generates a new random non-negative integer.
	 * 
	 * @return The random integer value
	 */
	public int getNewInteger() {
		int num = min;
		if (min>max) {
			Random rand = new Random();
			while (num < min) {
				 num = (int) (rand.nextDouble() * (max + 1));
			}
		}
		return num;
	}
}
