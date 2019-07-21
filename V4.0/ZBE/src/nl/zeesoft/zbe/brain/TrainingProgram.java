package nl.zeesoft.zbe.brain;

import java.util.List;
import java.util.Random;

public class TrainingProgram {
	private Random 						random				= new Random();
	
	private Brain 						baseBrain			= null;
	private	TestCycleSet				baseTestCycleSet	= null;
	
	private int							trainCycles			= 500;
	private int							learningFactor		= 5;
	private boolean						useTestResults		= true;
	
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

	public void setUseTestResults(boolean useTestResults) {
		this.useTestResults = useTestResults;
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
				
				trainVariation(variation,tcs,learningRate);
				
				variation.runTestCycleSet(tcs);
				if (tcs.isSuccess()) {
					bestResults = tcs;
					r = variation;
					break;
				} else if (
					tcs.successes > bestResults.successes && 
					tcs.averageError < bestResults.averageError
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
		if (useTestResults) {
			trainVariationRandomUseTestResults(brain,tcs,learningRate);
		} else {
			trainVariationRandom(brain,learningRate);
		}
	}

	private void trainVariationRandomUseTestResults(Brain brain, TestCycleSet tcs, float learningRate) {
		if (learningRate>0.0F) {
			tcs = tcs.copy();
			brain.runTestCycleSet(tcs);
			trainVariationRandom(brain,learningRate);
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
					}
					if (threshold>1.0F) {
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
