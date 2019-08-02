package nl.zeesoft.zdk.neural;

public class Test extends Prediction {
	public float[] 			expectations	= null;

	public float[] 			errors			= null;
	
	protected Test() {
		
	}
	
	protected Test(NeuralNet nn) {
		super(nn);
		expectations = new float[nn.outputNeurons];
		initializeValues(expectations);
		errors = new float[nn.outputNeurons];
		initializeValues(errors);
	}
	
	@Override
	public Prediction copy() {
		Test r = new Test();
		copyTo(r);
		return r;
	}
	
	@Override
	protected void prepare(NeuralNet nn) {
		super.prepare(nn);
		initializeValues(errors);
	}
		
	@Override
	protected void finalize(NeuralNet nn) {
		super.finalize(nn);
		for (int i = 0; i < errors.length; i++) {
			errors[i] = expectations[i] - outputs[i];
		}
	}

	@Override
	protected void copyTo(Prediction p) {
		super.copyTo(p);
		if (p instanceof Test) {
			Test t = (Test) p;
			t.expectations = new float[expectations.length];
			copyValues(t.expectations,expectations);
			t.errors = new float[errors.length];
			copyValues(t.errors,errors);
		}
	}
}
