package nl.zeesoft.zdk.functions;

public interface ZFunction {
	/**
	 * Should return the new value for the current value.
	 * 
	 * @param v The current value
	 * @return The new value
	 */
	public float applyFunction(float v);
}
