package nl.zeesoft.zdk.functions;

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
