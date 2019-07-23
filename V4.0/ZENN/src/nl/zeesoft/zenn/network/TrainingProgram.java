package nl.zeesoft.zenn.network;

import java.util.List;
import java.util.Random;

public class TrainingProgram {
	private Random 						random				= new Random();
	
	private NN 							baseNN				= null;
	private	TestCycleSet				baseTestCycleSet	= null;
	
	private int							trainCycles			= 1000;
	private float						minLearningRate		= 0.1F;
	private float						maxLearningRate		= 0.5F;
	
	private int							trainedCycles		= 0;
	private TestCycleSet				initialResults		= null;
	private TestCycleSet				finalResults		= null;

	public TrainingProgram(NN baseNN, TestCycleSet baseTestCycleSet) {
		this.baseNN = baseNN;
		this.baseTestCycleSet = baseTestCycleSet;
	}

	public void setTrainCycles(int trainCycles) {
		this.trainCycles = trainCycles;
	}

	public void setMinMaxLearningRate(int min,int max) {
		this.minLearningRate = min;
		this.maxLearningRate = max;
	}

	public NN runProgram(int minSuccessLevel) {
		NN r = baseNN.copy();
		trainedCycles = 0;
		TestCycleSet bestResults = baseTestCycleSet.copy();
		r.runTestCycleSet(bestResults);
		initialResults = bestResults;
		if (!bestResults.isSuccess(minSuccessLevel)) {
			for (int c = 0; c < trainCycles; c++) {
				trainedCycles++;
				
				NN variation = r.copy();
				TestCycleSet tcs = baseTestCycleSet.copy();
				float learningRate = bestResults.averageError;
				if (learningRate<minLearningRate) {
					learningRate = minLearningRate;
				} else if (learningRate>maxLearningRate) {
					learningRate = maxLearningRate;
				}
				trainVariation(variation,tcs,learningRate);
				
				variation.runTestCycleSet(tcs);
				if (tcs.isSuccess(minSuccessLevel)) {
					bestResults = tcs;
					r = variation;
					break;
				} else if (
					tcs.successes > bestResults.successes || 
					(tcs.successes == bestResults.successes && tcs.averageError < bestResults.averageError)
					) {
					bestResults = tcs;
					r = variation;
				}
			}
		}
		finalResults = bestResults;
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
	
	protected void trainVariation(NN nn, TestCycleSet tcs, float learningRate) {
		if (trainedCycles % 5 == 0) {
			trainVariationRandom(nn,learningRate);
		} else {
			trainVariationRandomUseTestResults(nn,tcs,learningRate);
		}
	}

	protected float getRandomFloat(float max) {
		return random.nextFloat() * max;
	}
	
	protected boolean getRandomBoolean() {
		return random.nextFloat() > 0.5F;
	}

	private void trainVariationRandomUseTestResults(NN nn, TestCycleSet tcs, float learningRate) {
		if (learningRate>0.0F) {
			tcs = tcs.copy();
			int layers = nn.getLayers().size();
			for (TestCycle tc: tcs.cycles) {
				nn.runCycle(tc);
				if (!tc.success) {
					for (int i = 0; i < tc.errors.length; i++) {
						if (tc.errors[i]!=0.0F) {
							for (NeuronLink link: tc.firedLinks) {
								if (getRandomBoolean()) {
									float diff = tc.errors[i];
									if (diff<0.0F) {
										diff = diff * -1.0F;
									}
									float rate = (learningRate + (diff / (float)layers)) / 2.0F;
									if (rate<minLearningRate) {
										rate = minLearningRate;
									}
									if (tc.errors[i]>0.0F) {
										link.weight += getRandomFloat(rate);
									} else {
										link.weight -= getRandomFloat(rate);
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	private void trainVariationRandom(NN nn, float learningRate) {
		if (learningRate>0.0F) {
			List<NeuronLayer> layers = nn.getLayers();
			for (NeuronLayer layer: layers) {
				for (Neuron neuron: layer.neurons) {
					if (getRandomBoolean()) {
						float threshold = neuron.threshold;
						float diff = neuron.threshold - neuron.value;
						if (diff<0.0F) {
							diff = diff * -1.0F;
						}
						float rate = (learningRate + diff) / 2.0F;
						if (getRandomBoolean()) {
							threshold -= getRandomFloat(rate);
						} else {
							threshold += getRandomFloat(rate);
						}
						if (threshold<0.0F) {
							threshold = 0.0F;
						} else if (threshold>1.0F) {
							threshold = 1.0F;
						}
						neuron.threshold = threshold;
					}
					for (NeuronLink link: neuron.sources) {
						if (getRandomBoolean()) {
							float weight = link.weight;
							if (getRandomBoolean()) {
								weight -= getRandomFloat(learningRate);
							} else {
								weight += getRandomFloat(learningRate);
							}
							link.weight = weight;
						}
					}
				}
			}
		}
	}
}
