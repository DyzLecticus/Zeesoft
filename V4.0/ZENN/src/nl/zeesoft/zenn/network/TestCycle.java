package nl.zeesoft.zenn.network;

import java.text.DecimalFormat;

import nl.zeesoft.zdk.ZStringBuilder;

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
		success = true;
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
	protected void prepare() {
		for (int i = 0; i < errors.length; i++) {
			errors[i] = 0.0F;
		}
		success = true;
	}
	
	@Override
	protected void finalize(NN nn) {
		super.finalize(nn);
		for (int n = 0; n < outputs.length; n++) {
			if (outputs[n]!=expectedOutputs[n]) {
				if (roundOutput) {
					success	= false;
					Neuron output = nn.getOutputLayer().neurons.get(n);
					errors[n] = expectedOutputs[n] - output.value;
				} else {
					float diff = expectedOutputs[n] - outputs[n];
					if (diff<0.0F) {
						diff = diff * -1.0F;
					}
					if (diff>errorTolerance) {
						success	= false;
						errors[n] = expectedOutputs[n] - outputs[n];
					}
				}
			}
		}
	}

	protected ZStringBuilder getSummary(int num) {
		DecimalFormat df = new DecimalFormat("0.00");

		ZStringBuilder r = new ZStringBuilder();
		r.append("Test cycle: ");
		r.append("" + num);
		r.append(", level: ");
		r.append("" + level);
		r.append(", fired neurons: ");
		r.append("" + firedNeurons.size());
		r.append( ", success: ");
		r.append("" + success);
		r.append("\n");
		for (int n = 0; n < outputs.length; n++) {
			if (outputs[n]!=expectedOutputs[n]) {
				r.append("  Output: ");
				r.append("" + n);
				r.append(": ");
				if (roundOutput) {
					if (errors[n] > 0.0F) {
						r.append("" + df.format(expectedOutputs[n] - errors[n]));
					} else {
						r.append("" + df.format(expectedOutputs[n] + (errors[n] * -1.0F)));
					}
				} else {
					r.append("" + df.format(outputs[n]));
				}
				r.append(", expected: ");
				r.append("" + df.format(expectedOutputs[n]));
				r.append(", error: ");
				r.append("" + df.format(errors[n]));
				r.append("\n");
			}
		}
		return r;
	}
}
