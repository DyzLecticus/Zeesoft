package nl.zeesoft.zdk.matrix;

public interface ZActivator extends ZFunction {
	/**
	 * Should return the derivative value for the current value.
	 * 
	 * @param v The current value
	 * @return The new value
	 */
	public float applyDerivativeFunction(float v);
}
