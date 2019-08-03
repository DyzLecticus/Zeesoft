package nl.zeesoft.zdk.functions;

import nl.zeesoft.zdk.matrix.ZActivator;
import nl.zeesoft.zdk.matrix.ZFunction;

public class ZSigmoid implements ZActivator {
	protected ZSigmoid() {
		// Use StaticFunctions class to get instance
	}
	
	@Override
	public float applyFunction(float v) {
		return sigmoid(v);
	}

	@Override
	public ZFunction getDerivative() {
		return StaticFunctions.SIGMOID_DER;
	}
	
	public static float sigmoid(float x) {
		return (float) (1 / (1 + Math.pow(Math.E,(x * -1))));
	}
}
