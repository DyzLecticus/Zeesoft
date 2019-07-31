package nl.zeesoft.zdk.matrix.functions;

import nl.zeesoft.zdk.matrix.ZActivator;
import nl.zeesoft.zdk.matrix.ZFunction;

public class ZLeakyReLU implements ZActivator {
	protected ZLeakyReLU() {
		// Use StaticFunctions class to get instance
	}
	
	@Override
	public float applyFunction(float v) {
		return relu(v);
	}

	@Override
	public ZFunction getDerivative() {
		return new ZLeakyReLUDerivative();
	}
	
	public static float relu(float x) {
		float low = x * 0.01F;
		return x > low ? x : low;
	}
}
