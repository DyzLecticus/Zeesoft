package nl.zeesoft.zdk.matrix.functions;

import nl.zeesoft.zdk.matrix.ZActivator;

public class ZSigmoid implements ZActivator {
	@Override
	public float applyFunction(float v) {
		return sigmoid(v);
	}

	@Override
	public float applyDerivativeFunction(float v) {
		return sigmoidDerived(v);
	}
	
	public static float sigmoid(float x) {
		return (float) (1 / (1 + Math.pow(Math.E,(x * -1))));
	}

	public static float sigmoidDerived(float y) {
		return y * (1.0F - y);
	}
}
