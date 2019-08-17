package nl.zeesoft.zdk.functions;

public interface ZWeightFunction extends ZParamFunction {
	/**
	 * Should return the parameter value for the function depending on the fan-in and fan-out.
	 * 
	 * @param fanIn The number of incoming connections
	 * @param fanOut The number of outgoing connections
	 * @return The parameter value to be used in the weight function
	 */
	public float getParameterValue(float fanIn,float fanOut);
}
