package nl.zeesoft.zdk.functions;

import java.util.Random;

/**
 * Provides randomness in the form of a function so it can be used among the other static functions.
 */
public class ZRandomize implements ZFunction {
	private static Random 	random		= new Random();
	
	public float			min			= -1F; 
	public float			max			= 1F; 
	
	@Override
	public float applyFunction(float v) {
		return getRandomFloat();
	}
	
	public float getRandomFloat() {
		return getRandomFloat(min,max);
	}

	public static float getRandomFloat(float min, float max) {
		float r = 0;
		if (min<max) {
			r = min + random.nextFloat() * (max - min);
		}
		return r;
	}

	public static int getRandomInt(int min, int max) {
		return (int) getRandomFloat(min,max);
	}
}
