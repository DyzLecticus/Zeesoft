package nl.zeesoft.zdk.functions;

public class ZWeightDefault implements ZWeightFunction {
	protected static final float	SQRT_2		= (float) Math.sqrt(2);	
	
	@Override
	public float applyFunction(float v, float p) {
		return v * (SQRT_2 / (float)Math.sqrt(p));
	}

	@Override
	public float getParameterValue(float fanIn, float fanOut) {
		return fanIn;
	}
}
