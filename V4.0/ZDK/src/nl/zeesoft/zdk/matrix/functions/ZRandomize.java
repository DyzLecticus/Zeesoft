package nl.zeesoft.zdk.matrix.functions;

import java.util.Random;

import nl.zeesoft.zdk.matrix.ZFunction;

public class ZRandomize implements ZFunction {
	private Random 	random		= new Random();
	
	public float	min			= -1F; 
	public float	max			= 1F; 
	
	@Override
	public float applyFunction(float v) {
		return getRandomFloat();
	}
	
	public float getRandomFloat() {
		return getRandomFloat(min,max);
	}

	public float getRandomFloat(float min, float max) {
		float r = 0;
		if (min<max) {
			r = min + random.nextFloat() * (max - min);
		}
		return r;
	}

	public int getRandomInt(int min, int max) {
		return (int) getRandomFloat();
	}
}
