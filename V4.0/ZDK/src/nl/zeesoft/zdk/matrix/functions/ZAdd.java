package nl.zeesoft.zdk.matrix.functions;

import nl.zeesoft.zdk.matrix.ZParamFunction;

public class ZAdd implements ZParamFunction {
	@Override
	public float applyFunction(float v,float p) {
		return v + p;
	}
}
