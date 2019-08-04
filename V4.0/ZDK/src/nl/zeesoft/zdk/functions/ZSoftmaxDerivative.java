package nl.zeesoft.zdk.functions;

public class ZSoftmaxDerivative implements ZFunction {
	protected ZSoftmaxDerivative() {
		
	}
	
	@Override
	public float applyFunction(float y) {
		// TODO: Replace this shortcut with a real implementation
		return ZSigmoidDerivative.sigmoidDerived(y);
	}
}
