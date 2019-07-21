package nl.zeesoft.zbe.brain;

import java.util.List;
import java.util.Random;

public class TrainingProgram {
	private Random 						random				= new Random();
	
	private Brain 						baseBrain			= null;
	private	TestCycleSet				baseTestCycleSet	= null;
	
	private int							trainCycles			= 1000;
	private int							learningFactor		= 5;
	
	private int							trainedCycles		= 0;
	private int							learnedTests		= 0;

	public TrainingProgram(Brain baseBrain, TestCycleSet baseTestCycleSet) {
		this.baseBrain = baseBrain;
		this.baseTestCycleSet = baseTestCycleSet;
	}

	public void setTrainCycles(int trainCycles) {
		this.trainCycles = trainCycles;
	}

	public void setLearningFactor(int learningFactor) {
		this.learningFactor = learningFactor;
	}

	public Brain runProgram() {
		Brain r = baseBrain.copy();
		trainedCycles = 0;
		TestCycleSet bestResults = baseTestCycleSet.copy();
		r.runTestCycleSet(bestResults);
		learnedTests = bestResults.successes;
		if (!bestResults.isSuccess()) {
			int factor = learningFactor;
			int factorChange = 0;
			for (int c = 0; c < trainCycles; c++) {
				trainedCycles++;
				
				Brain variation = r.copy();
				TestCycleSet tcs = baseTestCycleSet.copy();
				float learningRate = bestResults.averageError * (float)factor;
				if (learningRate>1.0F) {
					learningRate = 1.0F;
				}
				trainVariation(variation,tcs,learningRate);
				
				variation.runTestCycleSet(tcs);
				if (tcs.isSuccess()) {
					bestResults = tcs;
					r = variation;
					break;
				} else if (
					tcs.successes > bestResults.successes || 
					(tcs.successes == bestResults.successes && tcs.averageError < bestResults.averageError)
					) {
					if (factor>1) {
						factor--;
					}
					bestResults = tcs;
					r = variation;
				} else if (factor<learningFactor) {
					factorChange++;
					if (factorChange>=100) {
						factorChange = 0;
						factor++;
					}
				}
			}
		}
		learnedTests = bestResults.successes - learnedTests;
		return r;
	}
	
	public int getTrainedCycles() {
		return trainedCycles;
	}
	
	public int getLearnedTests() {
		return learnedTests;
	}
	
	public void trainVariation(Brain brain, TestCycleSet tcs, float learningRate) {
		if (trainedCycles % 2 == 0) {
			trainVariationRandomUseTestResults(brain,tcs,learningRate);
		} else {
			trainVariationRandom(brain,learningRate);
		}
	}

	private void trainVariationRandomUseTestResults(Brain brain, TestCycleSet tcs, float learningRate) {
		if (learningRate>0.0F) {
			tcs = tcs.copy();
			for (Cycle cycle: tcs.cycles) {
				if (cycle instanceof TestCycle) {
					brain.runCycle(cycle);
					TestCycle tc = (TestCycle) cycle;
					if (!tc.success) {
						for (int i = 0; i < tc.errors.length; i++) {
							if (tc.errors[i]!=0.0F) {
								for (Neuron neuron: tc.firedNeurons) {
									if (getRandomBoolean()) {
										if (tc.errors[i]>0.0F) {
											neuron.threshold += getRandomFloat(learningRate);
											if (neuron.threshold>1.0F) {
												neuron.threshold = 1.0F;
											}
										} else {
											neuron.threshold -= getRandomFloat(learningRate);
											if (neuron.threshold<0.0F) {
												neuron.threshold = 0.0F;
											}
										}
									}
								}
								for (NeuronLink link: tc.firedLinks) {
									//if (getRandomBoolean()) {
										if (tc.errors[i]>0.0F) {
											link.weight += getRandomFloat(learningRate);
										} else {
											link.weight -= getRandomFloat(learningRate);
										}
									//}
								}
							}
						}
					}
				}
			}
			//trainVariationRandom(brain,learningRate);
		}
	}
	
	private void trainVariationRandom(Brain brain, float learningRate) {
		if (learningRate>0.0F) {
			List<NeuronLayer> layers = brain.getLayers();
			for (NeuronLayer layer: layers) {
				for (Neuron neuron: layer.neurons) {
					float threshold = neuron.threshold;
					if (getRandomBoolean()) {
						threshold -= getRandomFloat(learningRate);
					} else {
						threshold += getRandomFloat(learningRate);
					}
					if (threshold<0.0F) {
						threshold = 0.0F;
					} else if (threshold>1.0F) {
						threshold = 1.0F;
					}
					neuron.threshold = threshold;
					for (NeuronLink link: neuron.targets) {
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

	private float getRandomFloat(float max) {
		return random.nextFloat() * max;
	}
	
	private boolean getRandomBoolean() {
		return random.nextFloat() > 0.5F;
	}
}
