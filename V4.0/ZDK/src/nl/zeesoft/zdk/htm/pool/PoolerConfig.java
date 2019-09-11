package nl.zeesoft.zdk.htm.pool;

public class PoolerConfig {
	protected int			inputSize				= 0;
	protected int			outputSize				= 0;
	protected int			outputBits				= 0;
	
	protected float			potentialConnections	= 0.85F;
	protected float			connectionThreshold		= 0.1F;
	
	public PoolerConfig(int inputSize, int outputSize) {
		this.inputSize = inputSize;
		this.outputSize = outputSize;
	}

	public void setPotentialConnections(float potentialConnections) {
		this.potentialConnections = potentialConnections;
	}

	public void setConnectionThreshold(float connectionThreshold) {
		this.connectionThreshold = connectionThreshold;
	}
}
