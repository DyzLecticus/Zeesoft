package nl.zeesoft.zdk.matrix;

public interface ZMatrixFunction {
	/**
	 * Should return the new value for the current value.
	 * 
	 * @param v The current value
	 * @return The new value
	 */
	public float applyFunction(float v);
}
