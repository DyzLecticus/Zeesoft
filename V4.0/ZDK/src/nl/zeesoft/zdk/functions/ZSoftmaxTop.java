package nl.zeesoft.zdk.functions;

public class ZSoftmaxTop implements ZActivator {
	protected ZSoftmaxTop() {
		
	}

	@Override
	public ZFunction getDerivative() {
		// TODO: Softmax derivative
		//return StaticFunctions.SOFTMAX_DER;
		return StaticFunctions.SIGMOID_DER;
	}
	
	@Override
	public float applyFunction(float v) {
		// TODO: Check implementation
		return v > 0 ? (float) Math.pow(Math.E,v) : 0;
	}
}
