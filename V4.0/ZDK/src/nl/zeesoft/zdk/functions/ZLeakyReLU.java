package nl.zeesoft.zdk.functions;

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
		return StaticFunctions.L_RELU_DER;
	}
	
	public static float relu(float x) {
		float low = x * 0.01F;
		return x > low ? x : low;
	}
}
