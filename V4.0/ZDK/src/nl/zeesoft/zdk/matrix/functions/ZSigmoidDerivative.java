package nl.zeesoft.zdk.matrix.functions;

import nl.zeesoft.zdk.matrix.ZFunction;

public class ZSigmoidDerivative implements ZFunction {
	@Override
	public float applyFunction(float v) {
		return sigmoidDerived(v);
	}

	public static float sigmoidDerived(float y) {
		return y * (1.0F - y);
	}
}
