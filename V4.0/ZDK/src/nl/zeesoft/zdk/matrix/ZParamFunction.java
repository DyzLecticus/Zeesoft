package nl.zeesoft.zdk.matrix;

public interface ZParamFunction {
	/**
	 * Should return the new value for the current value and parameter value.
	 * 
	 * @param v The current value
	 * @param p The input value
	 * @return The new value
	 */
	public float applyFunction(float v,float p);
}
