package nl.zeesoft.zdk.matrix.functions;

import nl.zeesoft.zdk.matrix.ZFunction;

public class ZTanHDerivative implements ZFunction {
	@Override
	public float applyFunction(float v) {
		return tanhDerived(v);
	}

	public static float tanhDerived(float y) {
		return (1F - (float) Math.pow(Math.tanh(y),2));
	}
}
