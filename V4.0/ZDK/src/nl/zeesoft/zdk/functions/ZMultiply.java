package nl.zeesoft.zdk.functions;

import nl.zeesoft.zdk.matrix.ZParamFunction;

public class ZMultiply implements ZParamFunction {
	protected ZMultiply() {
		// Use StaticFunctions class to get instance
	}
	
	@Override
	public float applyFunction(float v,float p) {
		return v * p;
	}
}
