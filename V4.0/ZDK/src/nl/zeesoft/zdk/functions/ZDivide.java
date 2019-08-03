package nl.zeesoft.zdk.functions;

public class ZDivide implements ZParamFunction {
	protected ZDivide() {
		// Use StaticFunctions class to get instance
	}
	
	@Override
	public float applyFunction(float v,float p) {
		return v / p;
	}
}
