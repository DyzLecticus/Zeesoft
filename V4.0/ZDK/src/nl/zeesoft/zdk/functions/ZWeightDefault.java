package nl.zeesoft.zdk.functions;

public class ZWeightDefault implements ZWeightFunction {
	@Override
	public float applyFunction(float v, float p) {
		return v;
	}
	
	@Override
	public float getParameterValue(float fanIn, float fanOut) {
		return 0;
	}
}
