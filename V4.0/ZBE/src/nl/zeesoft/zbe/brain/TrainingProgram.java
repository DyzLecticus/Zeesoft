package nl.zeesoft.zbe.brain;

public class TrainingProgram {
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
				Brain variation = r.copy(true,learningRate);
				TestCycleSet tcs = baseTestCycleSet.copy();
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
}
