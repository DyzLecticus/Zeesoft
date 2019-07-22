package nl.zeesoft.zbe.brain;

import java.util.ArrayList;
import java.util.List;

public class TestCycle extends Cycle {
	public int					level				= 0;
	public float[]				expectedOutputs		= null;
	public float				errorTolerance		= 0.1F; // Used for non normalized output  
	
	public float[]				errors				= null;
	public boolean				success				= true;
	public List<TestCyclePath>	paths				= new ArrayList<TestCyclePath>();
	
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
			tc.level = level;
		}
	}
	
	@Override
	protected void finalize(Brain brain) {
		super.finalize(brain);
		for (int n = 0; n < outputs.length; n++) {
			if (outputs[n]!=expectedOutputs[n]) {
				Neuron output = brain.getOutputLayer().neurons.get(n);
				float diff = 0.0F;
				if (normalizeOutput) {
					diff = output.threshold - output.value;
				} else {
					diff = expectedOutputs[n] - outputs[n];
					if (diff<errorTolerance) {
						diff = 0.0F;
					}
				}
				if (diff!=0.0F) {
					success	= false;
					errors[n] = diff;
					if (errors[n]>0.0F) {
						for (int in = 0; in < inputs.length; in++) {
							TestCyclePath path = new TestCyclePath();
							paths.add(path);
							path.inputNeuron = brain.getInputLayer().neurons.get(in);
							path.outputNeuron = output;
							findPath(brain, path);
						}
					}
				}
			}
		}
	}
	
	private void findPath(Brain brain,TestCyclePath path) {
		findPath(brain,path,path.inputNeuron);
	}
	
	private void findPath(Brain brain,TestCyclePath path,Neuron workingNeuron) {
		// Find nearby in next layer 
		List<NeuronLink> candidates = new ArrayList<NeuronLink>();
		for (NeuronLink link: workingNeuron.targets) {
			
		}
		if (candidates.size()>0) {
			// Select random candidate
			// Add candidate target to middleNeurons and self call
		}
	}
}
