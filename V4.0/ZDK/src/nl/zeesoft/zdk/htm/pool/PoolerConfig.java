package nl.zeesoft.zdk.htm.pool;

public class PoolerConfig {
	protected int			inputSize				= 0;
	protected int			outputSize				= 0;
	protected int			outputBits				= 0;
	
	protected float			potentialConnections	= 0.85F;
	protected float			connectionThreshold		= 0.1F;
	protected float			connectionDecrement		= 0.008F;
	protected float			connectionIncrement		= 0.05F;
	
	public PoolerConfig(int inputSize, int outputSize, int outputBits) {
		this.inputSize = inputSize;
		this.outputSize = outputSize;
		this.outputBits = outputBits;
	}

	public void setPotentialConnections(float potentialConnections) {
		this.potentialConnections = potentialConnections;
	}

	public void setConnectionThreshold(float connectionThreshold) {
		this.connectionThreshold = connectionThreshold;
	}
}
