package nl.zeesoft.zenn.network;

import java.util.List;
import java.util.Random;

public class TrainingProgram {
	private Random 						random				= new Random();
	
	private NN 							baseNN				= null;
	private	TestCycleSet				baseTestCycleSet	= null;
	
	private int							method				= 0;
	private int							minSuccessLevel		= 0;
	private int							trainCycles			= 10000;
	private float						minLearningRate		= 0.05F;
	private float						maxLearningRate		= 0.3F;
	
	private int							trainedCycles		= 0;
	private TestCycleSet				initialResults		= null;
	private TestCycleSet				finalResults		= null;

	public TrainingProgram(NN baseNN, TestCycleSet baseTestCycleSet) {
		this.baseNN = baseNN;
		this.baseTestCycleSet = baseTestCycleSet;
	}

	public TrainingProgram copy(NN baseNN, TestCycleSet baseTestCycleSet) {
		TrainingProgram r = getCopyTrainingProgram(baseNN,baseTestCycleSet);
		r.setMethod(method);
		r.setMinSuccessLevel(minSuccessLevel);
		r.setTrainCycles(trainCycles);
		r.setMinMaxLearningRate(minLearningRate,maxLearningRate);
		return r;
	}
	
	public void setMethod(int method) {
		this.method = method;
	}

	public void setMinSuccessLevel(int minSuccessLevel) {
		this.minSuccessLevel = minSuccessLevel;
	}
	
	public void setTrainCycles(int trainCycles) {
		this.trainCycles = trainCycles;
	}

	public void setMinMaxLearningRate(float min,float max) {
		this.minLearningRate = min;
		this.maxLearningRate = max;
	}

	public NN runProgram() {
		NN r = baseNN.copy();
		trainedCycles = 0;
		initialResults = baseTestCycleSet.copy();
		
		r.runTestCycleSet(initialResults);
		float err = initialResults.averageError;
				
		NN variation = r.copy();
		r = null;
		for (int c = 0; c < trainCycles; c++) {
			trainedCycles++;
			TestCycleSet tcs = baseTestCycleSet.copy(random);
			float learningRate = minLearningRate + err;
			if (learningRate>maxLearningRate) {
				learningRate = maxLearningRate;
			}
			trainVariation(variation,method,tcs.copy(),learningRate);
			err = tcs.averageError;
		}
		
		finalResults = baseTestCycleSet.copy(random);
		variation.runTestCycleSet(finalResults);
		if (finalResults.compareTo(initialResults) > 0) {
			baseNN = variation;
			r = variation;
		}
		
		return r;
	}
	
	public int getTrainedCycles() {
		return trainedCycles;
	}
	
	public TestCycleSet getInitialResults() {
		return initialResults;
	}
	
	public TestCycleSet getFinalResults() {
		return finalResults;
	}
	
	public boolean isSuccess() {
		return finalResults.isSuccess(minSuccessLevel);
	}
	
	protected void trainVariation(NN nn, int method, TestCycleSet tcs, float learningRate) {
		trainVariationBackPropagate(nn,tcs,learningRate);
	}

	protected TrainingProgram getCopyTrainingProgram(NN baseNN, TestCycleSet baseTestCycleSet) {
		return new TrainingProgram(baseNN,baseTestCycleSet);
	}

	protected float getRandomFloat(float max) {
		return random.nextFloat() * max;
	}
	
	protected List<NeuronLayer> getLayers(NN nn) {
		return nn.getLayers();
	}

	private void trainVariationBackPropagate(NN nn, TestCycleSet tcs, float learningRate) {
		boolean debug = false;
		
		if (debug) {
			learningRate = 0.1F;
			trainCycles = 1;
		}
		
		nn.runTestCycleSet(tcs);
		//if (trainedCycles % 1000==0) {
			//System.out.println("Average error: " + tcs.averageError);
		//}
		
		List<NeuronLayer> layers = getLayers(nn);
		List<TestCycle> cycles = tcs.getCycles();
		for (TestCycle tc: cycles) {
			if (debug) {
				System.out.println(nn.getSummary());
				System.out.println("BEFORE " + tc.getSummary(1));
			}
			
			float[] errors = tc.errors;
			for (int l = layers.size() - 1; l>0 ; l--) {
				NeuronLayer layer = layers.get(l);
				NeuronLayer pLayer = layers.get(l - 1);

				// Calculate errors
				float[] biasErrors = new float[layer.neurons.size()];
				for (int be = 0; be < biasErrors.length; be++) {
					biasErrors[be] = 0.0F; 
				}
				float[] pErrors = new float[pLayer.neurons.size()];
				for (int pe = 0; pe < pErrors.length; pe++) {
					pErrors[pe] = 0.0F; 
				}
				for (int e = 0; e < errors.length; e++) {
					if (errors[e]!=0.0F) {
						Neuron neuron = layer.neurons.get(e);
						float totalWeight = neuron.getTotalSourceWeight();
						for (int pe = 0; pe < pErrors.length; pe++) {
							Neuron pNeuron = pLayer.neurons.get(pe);
							NeuronLink link = neuron.getSourceByNeuron(pNeuron);
							float ratio = link.weight / totalWeight;
							pErrors[pe] = ratio * errors[e];
						}
						float ratio = neuron.bias / totalWeight;
						biasErrors[e] = ratio * errors[e];
					}
				}
				if (debug) {
					System.out.println();
					System.out.println("Layer: " + l + ", bias errors; ");
					for (int be = 0; be < biasErrors.length; be++) {
						System.out.println("  " + biasErrors[be]);
					}
					System.out.println("Previous layer: " + (l - 1) + ", errors; ");
					for (int pe = 0; pe < pErrors.length; pe++) {
						System.out.println("  " + pErrors[pe]);
					}
				}
				
				// Calculate deltas
				float[] biasDeltas = new float[layer.neurons.size()];
				for (int be = 0; be < biasDeltas.length; be++) {
					biasDeltas[be] = 0.0F; 
				}
				float[] pDeltas = new float[pLayer.neurons.size()];
				for (int pe = 0; pe < pDeltas.length; pe++) {
					pDeltas[pe] = 0.0F; 
				}
				for (int e = 0; e < errors.length; e++) {
					Neuron neuron = layer.neurons.get(e);
					if (errors[e]!=0.0F) {
						for (int pe = 0; pe < pErrors.length; pe++) {
							if (pErrors[pe]!=0.0F) {
								Neuron pNeuron = pLayer.neurons.get(pe);
								float gradient = NN.derivativeOfSigmoided(neuron.value) * pErrors[pe] * learningRate;
								pDeltas[pe] = gradient * pNeuron.value;
								
								// Apply
								NeuronLink link = neuron.getSourceByNeuron(pNeuron);
								if (pDeltas[pe]>0.0F) {
									link.weight += pDeltas[pe];
								} else if (pDeltas[pe]<0.0F) {
									link.weight -= pDeltas[pe] * -1.0F;
								}
							}
						}
						if (biasErrors[e]!=0.0F) {
							float gradient = biasErrors[e] * learningRate;
							biasDeltas[e] = gradient * neuron.bias;
							
							// Apply
							if (biasDeltas[e]>0.0F) {
								neuron.bias += biasDeltas[e];
							} else if (biasDeltas[e]<0.0F) {
								neuron.bias -= biasDeltas[e] * -1.0F;
							}
						}
					}
				}
				if (debug) {
					System.out.println();
					System.out.println("Layer: " + l + ", bias deltas; ");
					for (int be = 0; be < biasDeltas.length; be++) {
						System.out.println("  " + biasDeltas[be]);
					}
					System.out.println("Previous layer: " + (l - 1) + ", deltas; ");
					for (int pe = 0; pe < pDeltas.length; pe++) {
						System.out.println("  " + pDeltas[pe]);
					}
				}
				
				errors = pErrors;
			}
			
			nn.runCycle(tc);
			
			if (debug) {
				System.out.println("AFTER " + tc.getSummary(1));
				break;
			}
			
			if (!tc.success) {
				break;
			}
		}
	}
}
