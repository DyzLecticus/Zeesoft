package nl.zeesoft.zdk.functions;

public class ZWeightXavier implements ZWeightFunction {
	protected static final float	SQRT_6		= (float) Math.sqrt(6);	
	
	@Override
	public float applyFunction(float v, float p) {
		return v * (SQRT_6 / (float)Math.sqrt(p));
	}

	@Override
	public float getParameterValue(float fanIn, float fanOut) {
		return fanIn + fanOut;
	}
}
