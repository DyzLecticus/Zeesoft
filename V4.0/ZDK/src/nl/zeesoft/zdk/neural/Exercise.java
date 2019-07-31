package nl.zeesoft.zdk.neural;

public class Exercise extends Cycle {
	public float[] 			expectations	= null;
	public float			errorTolerance	= 0.1F;

	public float[] 			errors			= null;
	public boolean			success			= true;
	
	protected Exercise() {
		
	}
	
	protected Exercise(NeuralNet nn) {
		super(nn);
		expectations = new float[nn.outputNeurons];
		initializeValues(expectations);
		errors = new float[nn.outputNeurons];
		initializeValues(errors);
	}
	
	@Override
	public Cycle copy() {
		Exercise r = new Exercise();
		copyTo(r);
		return r;
	}
	
	@Override
	protected void prepare(NeuralNet nn) {
		super.prepare(nn);
		initializeValues(errors);
		success = true;
	}
		
	@Override
	protected void finalize(NeuralNet nn) {
		super.finalize(nn);
		for (int i = 0; i < errors.length; i++) {
			errors[i] = expectations[i] - outputs[i];
			if (errors[i]!=0.0) {
				float diff = errors[i];
				if (diff<0) {
					diff = diff * -1;
				}
				if (diff>errorTolerance) {
					success = false;
				}
			}
		}
	}

	@Override
	protected void copyTo(Cycle c) {
		super.copyTo(c);
		if (c instanceof Exercise) {
			Exercise ex = (Exercise) c;
			ex.expectations = new float[expectations.length];
			copyValues(ex.expectations,expectations);
			ex.errorTolerance = errorTolerance;
			ex.errors = new float[errors.length];
			copyValues(ex.errors,errors);
			ex.success = success;
		}
	}
}
