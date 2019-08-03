package nl.zeesoft.zdk.functions;

import nl.zeesoft.zdk.matrix.ZActivator;
import nl.zeesoft.zdk.matrix.ZFunction;

public class ZReLU implements ZActivator {
	protected ZReLU() {
		// Use StaticFunctions class to get instance
	}
	
	@Override
	public float applyFunction(float v) {
		return relu(v);
	}

	@Override
	public ZFunction getDerivative() {
		return StaticFunctions.RELU_DER;
	}
	
	public static float relu(float x) {
		return x > 0 ? x : 0;
	}
}
