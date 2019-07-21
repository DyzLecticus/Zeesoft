package nl.zeesoft.zbe.brain;

import java.util.List;
import java.util.Random;

public class TrainingProgram {
	private Random 						random				= new Random();
	
	private Brain 						baseBrain			= null;
	private	TestCycleSet				baseTestCycleSet	= null;
	
	private int							trainCycles			= 500;
	private int							learningFactor		= 3;
	
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
			for (int c = 0; c < trainCycles; c++) {
				trainedCycles++;
				float learningRate = bestResults.averageError * (float)learningFactor;
				Brain variation = r.copy();
				TestCycleSet tcs = baseTestCycleSet.copy();
				trainVariation(variation,tcs,learningRate);
				variation.runTestCycleSet(tcs);
				if (tcs.isSuccess()) {
					bestResults = tcs;
					r = variation;
					break;
				} else if (tcs.successes > bestResults.successes) {
					bestResults = tcs;
					r = variation;
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
		//tcs = tcs.copy();
		//brain.runTestCycleSet(tcs);

		List<NeuronLayer> layers = brain.getLayers();
		for (NeuronLayer layer: layers) {
			for (Neuron neuron: layer.neurons) {
				float threshold = neuron.threshold;
				if (learningRate!=0.0F) {
					if (getRandomBoolean()) {
						threshold -= getRandomFloat(learningRate);
					} else {
						threshold += getRandomFloat(learningRate);
					}
				}
				neuron.threshold = threshold;
				for (NeuronLink link: neuron.targets) {
					float weight = link.weight;
					if (learningRate!=0.0F) {
						if (getRandomBoolean()) {
							weight -= getRandomFloat(learningRate);
						} else {
							weight += getRandomFloat(learningRate);
						}
					}
					link.weight = weight;
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
