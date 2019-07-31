package nl.zeesoft.zdk.matrix.functions;

import nl.zeesoft.zdk.matrix.ZFunction;

public class ZLeakyReLUDerivative implements ZFunction {
	protected ZLeakyReLUDerivative() {
		// Use StaticFunctions class to get instance
	}
	
	@Override
	public float applyFunction(float v) {
		return reulDerived(v);
	}

	public static float reulDerived(float y) {
		return y <= 0.01F ? 0.01F : 1;
	}
}
