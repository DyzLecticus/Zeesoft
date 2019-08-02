package nl.zeesoft.zdk.neural;

public class Training extends Prediction {
	public float[] 			expectations	= null;

	public float[] 			errors			= null;
	
	protected Training() {
		
	}
	
	protected Training(NeuralNet nn) {
		super(nn);
		expectations = new float[nn.outputNeurons];
		initializeValues(expectations);
		errors = new float[nn.outputNeurons];
		initializeValues(errors);
	}
	
	@Override
	public Prediction copy() {
		Training r = new Training();
		copyTo(r);
		return r;
	}
	
	@Override
	protected void prepare(NeuralNet nn) {
		super.prepare(nn);
		initializeValues(errors);
	}

	@Override
	protected void copyTo(Prediction p) {
		super.copyTo(p);
		if (p instanceof Training) {
			Training t = (Training) p;
			t.expectations = new float[expectations.length];
			copyValues(t.expectations,expectations);
			t.errors = new float[errors.length];
			copyValues(t.errors,errors);
		}
	}
}
