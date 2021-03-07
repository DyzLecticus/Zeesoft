package nl.zeesoft.zdk;

import java.util.Random;

public class Rand {
	private static Random 	random		= new Random();
	
	public static float getRandomFloat(float min, float max) {
		float r = min;
		if (min<max) {
			r = min + random.nextFloat() * (max - min);
		}
		return r;
	}

	public static int getRandomInt(int min, int max) {
		int r = min;
		if (min<max) {
			r = Math.round(getRandomFloat(min,max));
		}
		return r;
	}
}
