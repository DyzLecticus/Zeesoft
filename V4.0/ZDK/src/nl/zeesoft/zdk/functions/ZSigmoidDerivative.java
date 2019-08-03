package nl.zeesoft.zdk.functions;

import nl.zeesoft.zdk.matrix.ZFunction;

public class ZSigmoidDerivative implements ZFunction {
	protected ZSigmoidDerivative() {
		// Use StaticFunctions class to get instance
	}
	
	@Override
	public float applyFunction(float v) {
		return sigmoidDerived(v);
	}

	public static float sigmoidDerived(float y) {
		return y * (1.0F - y);
	}
}
