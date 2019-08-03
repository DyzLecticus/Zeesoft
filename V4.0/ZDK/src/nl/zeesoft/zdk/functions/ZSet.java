package nl.zeesoft.zdk.functions;

public class ZSet implements ZParamFunction {
	protected ZSet() {
		// Use StaticFunctions class to get instance
	}
	
	@Override
	public float applyFunction(float v,float p) {
		return p;
	}
}
