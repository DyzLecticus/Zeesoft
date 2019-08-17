package nl.zeesoft.zdk.functions;

public class ZWeightZero implements ZWeightFunction {
	@Override
	public float applyFunction(float v, float p) {
		return 0;
	}

	@Override
	public float getParameterValue(float fanIn, float fanOut) {
		return 0;
	}
}
