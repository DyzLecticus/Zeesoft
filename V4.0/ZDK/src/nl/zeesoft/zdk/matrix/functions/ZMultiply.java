package nl.zeesoft.zdk.matrix.functions;

import nl.zeesoft.zdk.matrix.ZMatrixParameterizedFunction;

public class ZMultiply implements ZMatrixParameterizedFunction {
	@Override
	public float applyFunction(float v,float p) {
		return v * p;
	}
}
