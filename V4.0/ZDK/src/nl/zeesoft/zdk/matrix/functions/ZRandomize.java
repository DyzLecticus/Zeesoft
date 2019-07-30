package nl.zeesoft.zdk.matrix.functions;

import java.util.Random;

import nl.zeesoft.zdk.matrix.ZMatrixFunction;

public class ZRandomize implements ZMatrixFunction {
	private Random 	random		= new Random();
	
	public float	min			= -1F; 
	public float	max			= 1F; 
	
	@Override
	public float applyFunction(float v) {
		return getRandomFloat(min,max);
	}

	public float getRandomFloat(float min, float max) {
		float r = 0;
		if (min<max) {
			r = min + random.nextFloat() * (max - min);
		}
		return r;
	}
}
