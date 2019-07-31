package nl.zeesoft.zdk.matrix.functions;

import nl.zeesoft.zdk.matrix.ZParamFunction;

public class ZSet implements ZParamFunction {
	protected ZSet() {
		// Use StaticFunctions class to get instance
	}
	
	@Override
	public float applyFunction(float v,float p) {
		return p;
	}
}
