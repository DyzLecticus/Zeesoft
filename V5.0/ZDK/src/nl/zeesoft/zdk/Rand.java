package nl.zeesoft.zdk;

import java.util.Random;

public class Rand {
	private static Random 	random		= new Random();

	public static float getRandomFloat(float min, float max) {
		float r = 0;
		if (min==max) {
			r = min;
		} else {
			if (min>max) {
				float t = max;
				max = min;
				min = t;
			}
			r = min + random.nextFloat() * (max - min);
		}
		return r;
	}

	public static int getRandomInt(int min, int max) {
		return Math.round(getRandomFloat(min,max));
	}
}
