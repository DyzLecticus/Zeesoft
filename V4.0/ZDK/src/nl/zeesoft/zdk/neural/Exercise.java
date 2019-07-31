package nl.zeesoft.zdk.neural;

public class Exercise extends Cycle {
	public float[] 			expectations	= null;
	public float			errorTolerance	= 0.1F;

	public float[] 			errors			= null;
	public boolean			success			= true;
	
	protected Exercise(NeuralNet nn) {
		super(nn);
		expectations = new float[nn.outputNeurons];
		initializeValues(expectations);
		errors = new float[nn.outputNeurons];
		initializeValues(errors);
	}
	
	@Override
	protected void prepare(NeuralNet nn) {
		super.prepare(nn);
		initializeValues(errors);
		success = true;
	}
		
	protected void finalize(NeuralNet nn) {
		super.finalize(nn);
		// TODO: Calculate errors and determine success
	}
}
