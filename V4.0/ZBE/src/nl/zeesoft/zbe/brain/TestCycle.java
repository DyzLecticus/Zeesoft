package nl.zeesoft.zbe.brain;

public class TestCycle extends Cycle {
	public float[]		expectedOutputs		= null;
	public float		errorTolerance		= 0.1F; // Used for non normalized output  
	
	public float[]		errors				= null;
	public boolean		success				= true;
	
	@Override
	public void initialize(Brain brain) {
		super.initialize(brain);
		expectedOutputs = new float[brain.getOutputLayer().neurons.size()];
		for (int i = 0; i < expectedOutputs.length; i++) {
			expectedOutputs[i] = 0.0F;
		}
		errors = new float[brain.getOutputLayer().neurons.size()];
		for (int i = 0; i < errors.length; i++) {
			errors[i] = 0.0F;
		}
	}
	
	@Override
	public void copy(Cycle toCycle) {
		super.copy(toCycle);
		if (toCycle instanceof TestCycle) {
			TestCycle tc = (TestCycle) toCycle;
			tc.expectedOutputs = new float[expectedOutputs.length];
			for (int i = 0; i < tc.expectedOutputs.length; i++) {
				tc.expectedOutputs[i] = expectedOutputs[i];
			}
			tc.errors = new float[errors.length];
			for (int i = 0; i < tc.errors.length; i++) {
				tc.errors[i] = 0.0F;
			}
			tc.errorTolerance = errorTolerance;
		}
	}
	
	@Override
	protected void finalize(Brain brain) {
		super.finalize(brain);
		for (int n = 0; n < outputs.length; n++) {
			if (n<brain.getOutputLayer().neurons.size()) {
				Neuron output = brain.getOutputLayer().neurons.get(n);
				if (outputs[n]!=expectedOutputs[n]) {
					float diff = expectedOutputs[n] - output.value;
					if (diff<0.0F) {
						diff = diff * -1;
					}
					if (!normalizeOutput && diff<errorTolerance) {
						diff = 0.0F;
					}
					if (diff!=0.0F) {
						success	= false;
						if (normalizeOutput) {
							errors[n] = output.threshold - output.value;
						} else {
							errors[n] = expectedOutputs[n] - output.value;
						}
					}
				}
			}
		}
	}
}
