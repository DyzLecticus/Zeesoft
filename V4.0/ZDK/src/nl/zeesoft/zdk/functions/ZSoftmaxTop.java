package nl.zeesoft.zdk.functions;

public class ZSoftmaxTop implements ZActivator {
	protected ZSoftmaxTop() {
		
	}

	@Override
	public ZFunction getDerivative() {
		return StaticFunctions.SOFTMAX_DER;
	}
	
	@Override
	public float applyFunction(float v) {
		return v > 0 ? (float) Math.pow(Math.E,v) : 0;
	}
}
