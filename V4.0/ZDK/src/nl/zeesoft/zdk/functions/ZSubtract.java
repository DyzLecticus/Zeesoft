package nl.zeesoft.zdk.functions;

public class ZSubtract implements ZParamFunction {
	protected ZSubtract() {
		// Use StaticFunctions class to get instance
	}
	
	@Override
	public float applyFunction(float v,float p) {
		return v - p;
	}
}
