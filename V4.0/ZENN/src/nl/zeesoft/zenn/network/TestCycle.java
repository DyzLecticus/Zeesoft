package nl.zeesoft.zenn.network;

import java.text.DecimalFormat;

import nl.zeesoft.zdk.ZStringBuilder;

public class TestCycle extends Cycle {
	public int					level				= 0;
	public float[]				expectedOutputs		= null;
	public float				errorTolerance		= 0.2F;  
	
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
				errors[n] = expectedOutputs[n] - outputs[n];
				float diff = errors[n];
				if (diff>errorTolerance) {
					success	= false;
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
				float diff = errors[n];
				if (diff>errorTolerance) {
					r.append("  Output: ");
					r.append("" + n);
					r.append(", value: ");
					r.append("" + df.format(outputs[n]));
					r.append(", expected: ");
					r.append("" + df.format(expectedOutputs[n]));
					r.append(", error: ");
					r.append("" + df.format(errors[n]));
					r.append("\n");
				}
			}
		}
		return r;
	}
}
