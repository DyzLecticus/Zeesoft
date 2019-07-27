package nl.zeesoft.zenn.network;

public class TestCycle extends Cycle {
	public int					level				= 0;
	public float[]				expectedOutputs		= null;
	public float				errorTolerance		= 0.1F; // Used for non normalized output  
	
	public float[]				errors				= null;
	public boolean				success				= true;
	
	@Override
	public void initialize(NN nn) {
		super.initialize(nn);
		expectedOutputs = new float[nn.getOutputLayer().neurons.size()];
		for (int i = 0; i < expectedOutputs.length; i++) {
			expectedOutputs[i] = 0.0F;
		}
		errors = new float[nn.getOutputLayer().neurons.size()];
		for (int i = 0; i < errors.length; i++) {
			errors[i] = 0.0F;
		}
	}
	
	@Override
	public void copy(Cycle toCycle) {
		super.copy(toCycle);
		if (toCycle instanceof TestCycle) {
			TestCycle tc = (TestCycle) toCycle;
			tc.level = level;
			tc.expectedOutputs = new float[expectedOutputs.length];
			for (int i = 0; i < tc.expectedOutputs.length; i++) {
				tc.expectedOutputs[i] = expectedOutputs[i];
			}
			tc.errorTolerance = errorTolerance;
			tc.errors = new float[errors.length];
			for (int i = 0; i < tc.errors.length; i++) {
				tc.errors[i] = errors[i];
			}
			tc.success = success;
		}
	}
	
	@Override
	protected void finalize(NN nn) {
		super.finalize(nn);
		success = true;
		for (int n = 0; n < outputs.length; n++) {
			errors[n] = 0.0F;
			if (outputs[n]!=expectedOutputs[n]) {
				Neuron output = nn.getOutputLayer().neurons.get(n);
				if (normalizeOutput) {
					success	= false;
					errors[n] = output.threshold - output.value;
					if (errors[n]==0.0F) {
						errors[n] = 0.0001F;
					}
				} else {
					float diff = expectedOutputs[n] - outputs[n];
					if (diff<0.0F) {
						diff = diff * -1.0F;
					}
					if (diff>errorTolerance) {
						success	= false;
						errors[n] = (diff - errorTolerance);
					}
				}
			}
		}
	}
}
