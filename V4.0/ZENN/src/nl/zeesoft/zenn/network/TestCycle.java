package nl.zeesoft.zenn.network;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestCycle extends Cycle {
	public int					level				= 0;
	public float[]				expectedOutputs		= null;
	public float				errorTolerance		= 0.1F; // Used for non normalized output  
	
	public float[]				errors				= null;
	public boolean				success				= true;
	public List<TestCyclePath>	paths				= new ArrayList<TestCyclePath>();
	
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
	protected void finalize(NN nn) {
		super.finalize(nn);
		Random random = new Random();
		for (int n = 0; n < outputs.length; n++) {
			if (outputs[n]!=expectedOutputs[n]) {
				Neuron output = nn.getOutputLayer().neurons.get(n);
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
							path.inputNeuron = nn.getInputLayer().neurons.get(in);
							path.outputNeuron = output;
							findPath(nn,path,random);
						}
					}
				}
			}
		}
	}
	
	private void findPath(NN nn,TestCyclePath path,Random random) {
		findPath(nn,path,path.inputNeuron,random);
	}
	
	private void findPath(NN nn,TestCyclePath path,Neuron workingNeuron,Random random) {
		List<NeuronLink> candidates = new ArrayList<NeuronLink>();
		for (NeuronLink link: workingNeuron.targets) {
			if (link.target==path.outputNeuron) {
				candidates.clear();
				break;
			} else if (
				link.target.posX > workingNeuron.posX - 2 &&
				link.target.posX < workingNeuron.posX + 2
				) {
				candidates.add(link);
			}
		}
		if (candidates.size()>0) {
			if (candidates.size()>1) {
				int index = random.nextInt() % candidates.size();
				if (index<0) {
					index = index * -1;
				}
				workingNeuron = candidates.get(index).target;
			} else {
				workingNeuron = candidates.get(0).target;
			}
			path.middleNeurons.add(workingNeuron);
			findPath(nn,path,workingNeuron,random);
		}
	}
}
