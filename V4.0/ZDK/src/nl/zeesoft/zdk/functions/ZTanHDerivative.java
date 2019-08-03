package nl.zeesoft.zdk.functions;

public class ZTanHDerivative implements ZFunction {
	protected ZTanHDerivative() {
		// Use StaticFunctions class to get instance
	}
	
	@Override
	public float applyFunction(float v) {
		return tanhDerived(v);
	}

	public static float tanhDerived(float y) {
		return (1F - (float) Math.pow(Math.tanh(y),2));
	}
}
